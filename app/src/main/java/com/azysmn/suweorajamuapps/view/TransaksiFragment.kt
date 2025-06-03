package com.azysmn.suweorajamuapps.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azysmn.suweorajamuapps.R
import com.azysmn.suweorajamuapps.adapter.TransaksiAdapter
import com.azysmn.suweorajamuapps.data.model.TransaksiDisplay
import com.azysmn.suweorajamuapps.databinding.FragmentTransaksiBinding
import com.azysmn.suweorajamuapps.viewmodel.TransaksiViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransaksiFragment : Fragment() {

    private var _binding: FragmentTransaksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransaksiViewModel
    private lateinit var adapter: TransaksiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[TransaksiViewModel::class.java]

        adapter = TransaksiAdapter { transaksi ->
            val display = TransaksiDisplay(
                id = transaksi.transaksi.id,
                barangId = transaksi.barang.id,
                jumlah = transaksi.transaksi.jumlah,
                tanggal = transaksi.transaksi.tanggal
            )
            val bundle = Bundle().apply {
                putParcelable("transaksi", display)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment::class.java, bundle)
                .addToBackStack(null)
                .commit()
        }

        //conf recyclerview
        binding.recyclerViewTransaksi.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTransaksi.adapter = adapter

        //viewmodel list transaksi
        viewModel.transaksiList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.toList())

            //empty logic
            if (list.isNullOrEmpty()) {
                binding.textEmptyTransaksi.visibility = View.VISIBLE
                binding.recyclerViewTransaksi.visibility = View.GONE
            } else {
                binding.textEmptyTransaksi.visibility = View.GONE
                binding.recyclerViewTransaksi.visibility = View.VISIBLE
            }

            //total biaya
            val totalBiaya = adapter.getTotalBiaya()
            val formattedTotal = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID")).format(totalBiaya)
            binding.textTotalBiaya.text = "Total Biaya: $formattedTotal"

        }

        binding.buttonTambahTransaksi.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TransaksiFormFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        viewModel.loadTransaksi()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}