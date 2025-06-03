package com.azysmn.suweorajamuapps.view

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.azysmn.suweorajamuapps.R
import com.azysmn.suweorajamuapps.data.model.Barang
import com.azysmn.suweorajamuapps.databinding.FragmentBarangFormBinding
import com.azysmn.suweorajamuapps.utils.ImageUtils.copyImageToInternalStorage
import com.azysmn.suweorajamuapps.viewmodel.BarangViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class BarangFormFragment : Fragment() {

    private var _binding: FragmentBarangFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BarangViewModel by viewModels()

    private var barang: Barang? = null
    private var imageUrl: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            val safeUri = copyImageToInternalStorage(requireContext(), uri)
            safeUri?.let { newUri ->
                imageUrl = newUri
                binding.imagePreview.setImageURI(newUri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarangFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barang = arguments?.getParcelable("barang")

        barang?.let {
            binding.editNama.setText(it.nama)
            binding.editHarga.setText(it.harga.toString())
            imageUrl = Uri.parse(it.imageUrl)

            val file = File(imageUrl?.path ?: "")
            if(file.exists()) {
                binding.imagePreview.setImageURI(imageUrl)
            } else {
                binding.imagePreview.setImageResource(R.drawable.placeholder)
            }
            binding.buttonHapusBarang.visibility = View.VISIBLE
        }

        binding.imagePreview.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonPilihDariGaleri.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.buttonSimpanBarang.setOnClickListener {
            val nama = binding.editNama.text.toString()
            val harga = binding.editHarga.text.toString().toDoubleOrNull()
            if (nama.isBlank() || harga == null || imageUrl == null) {
                Toast.makeText(requireContext(), "Semua field harus diisi dan pilih gambar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.buttonSimpanBarang.isEnabled = false
            lifecycleScope.launch {
                val barangBaru = Barang(
                    id = barang?.id ?: 0,
                    nama = nama,
                    harga = harga,
                    stok = 0,
                    imageUrl = imageUrl.toString()
                )

                if (barang == null) {
                    viewModel.insertBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateBarang(barangBaru)
                    Toast.makeText(requireContext(), "Barang berhasil diupdate", Toast.LENGTH_SHORT).show()
                }

                delay(500)
                binding.progressBar.visibility = View.GONE
                binding.buttonSimpanBarang.isEnabled = true
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonHapusBarang.setOnClickListener {
            barang?.let {
                AlertDialog.Builder(requireContext())
                    .setTitle("Konfirmasi")
                    .setMessage("Apakah Anda yakin ingin menghapus barang ini?")
                    .setPositiveButton("Hapus") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteBarang(it)
                            Toast.makeText(requireContext(), "Barang berhasil dihapus", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}