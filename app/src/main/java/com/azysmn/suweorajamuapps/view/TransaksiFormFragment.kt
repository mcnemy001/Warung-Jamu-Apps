package com.azysmn.suweorajamuapps.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.azysmn.suweorajamuapps.R
import com.azysmn.suweorajamuapps.data.model.Transaksi
import com.azysmn.suweorajamuapps.data.model.TransaksiDisplay
import com.azysmn.suweorajamuapps.databinding.FragmentTransaksiFormBinding
import com.azysmn.suweorajamuapps.utils.DateTimePickerUtil
import com.azysmn.suweorajamuapps.viewmodel.TransaksiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransaksiFormFragment : Fragment() {

    private var _binding : FragmentTransaksiFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransaksiViewModel by viewModels()

    private var transaksiDisplay: TransaksiDisplay? = null

    private var selectedBarangId: Int? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transaksiDisplay = arguments?.getParcelable("transaksi")

        // spinner
        viewModel.barangList.observe(viewLifecycleOwner) { barangList ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                barangList.map { it.nama }
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_item)
            }
            binding.spinnerBarang.adapter = adapter


            transaksiDisplay?.let { transaksiItem ->
                val index = barangList.indexOfFirst {
                    it.id == transaksiItem.barangId
                }
                if (index >= 0) {
                    binding.spinnerBarang.setSelection(index)
                    binding.editJumlah.setText(transaksiItem.jumlah.toString())
                    binding.editTanggal.setText(transaksiItem.tanggal)
                    binding.buttonHapusTransaksi.visibility = View.VISIBLE
                }
            }

            //datetimepicker
            binding.editTanggal.setOnClickListener {
                DateTimePickerUtil.showDateTimePicker(requireContext()) { dateTime ->
                    binding.editTanggal.setText(dateTime)
                }
            }

            //simpan
            binding.buttonSimpanTransaksi.setOnClickListener {
                val jumlahStr = binding.editJumlah.text.toString()
                val tanggal = binding.editTanggal.text.toString()
                val jumlah = jumlahStr.toIntOrNull()
                selectedBarangId =
                    barangList.getOrNull(binding.spinnerBarang.selectedItemPosition)?.id

                if (selectedBarangId == null || selectedBarangId == 0) {
                    Toast.makeText(
                        requireContext(),
                        "Pilih barang terlebih dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (jumlahStr.isEmpty() || jumlah == null || jumlah <= 0) {
                    Toast.makeText(
                        requireContext(),
                        "Jumlah harus lebih dari 0",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (tanggal.isEmpty()) {
                    Toast.makeText(requireContext(), "Tanggal harus diisi", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val newTransaksi = Transaksi(
                    id = transaksiDisplay?.id ?: 0,
                    barangId = selectedBarangId ?: 0,
                    jumlah = jumlah,
                    tanggal = tanggal
                )

                binding.progressBar.visibility = View.VISIBLE
                binding.buttonHapusTransaksi.isEnabled = false

                lifecycleScope.launch {
                    if (transaksiDisplay == null) {
                        viewModel.addTransaksi(newTransaksi)
                        delay(300)
                        viewModel.loadTransaksi()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.updateTransaksi(newTransaksi)
                        delay(300)
                        viewModel.loadTransaksi()
                        Toast.makeText(
                            requireContext(),
                            "Transaksi berhasil diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.buttonSimpanTransaksi.isEnabled = true
                    parentFragmentManager.popBackStack()
                }

            }

            binding.buttonHapusTransaksi.setOnClickListener {
                transaksiDisplay?.let { transaksiToDelete ->
                    AlertDialog.Builder(requireContext())
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Yakin ingin menghapus transaksi ini ?")
                        .setPositiveButton("Hapus") { _, _ ->
                            lifecycleScope.launch {
                                viewModel.deleteTransaksi(transaksiToDelete.toTransaksi())
                                Toast.makeText(
                                    requireContext(),
                                    "Transaksi berhasil dihapus",
                                    Toast.LENGTH_SHORT
                                ).show()
                                delay(300)
                                viewModel.loadTransaksi()
                                parentFragmentManager.popBackStack()
                            }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }
        }

        viewModel.loadBarang()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}