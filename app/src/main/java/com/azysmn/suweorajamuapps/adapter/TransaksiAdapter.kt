package com.azysmn.suweorajamuapps.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azysmn.suweorajamuapps.data.model.TransaksiBarang
import com.azysmn.suweorajamuapps.databinding.ItemTransaksiBinding
import com.bumptech.glide.Glide

class TransaksiAdapter(
    private val onClick: (TransaksiBarang) -> Unit
): ListAdapter<TransaksiBarang, TransaksiAdapter.ViewHolder> (DiffCallback()) {

    class ViewHolder(private val binding: ItemTransaksiBinding):
    RecyclerView.ViewHolder(binding.root) {
        fun bind(transaksi: TransaksiBarang, onClick: (TransaksiBarang) -> Unit) {

            val totalBayar = transaksi.barang.harga * transaksi.transaksi.jumlah

            binding.textNamaBarang.text = transaksi.barang.nama
            binding.textHargaBarang.text = "Harga: ${transaksi.barang.harga}"
            binding.textJumlah.text = "Jumlah: ${transaksi.transaksi.jumlah}"
            binding.textTanggal.text = "Tanggal: ${transaksi.transaksi.tanggal}"

            val formattedTotal = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id", "ID")).format(totalBayar)
            binding.textTotalBayar.text = "Total: $formattedTotal"

            Glide.with(binding.root)
                .load(transaksi.barang.imageUrl)
                .into(binding.imageBarang)
            binding.root.setOnClickListener {
                onClick(transaksi)
            }
        }
    }

    fun getTotalBiaya(): Double {
        return currentList.sumOf { it.barang.harga * it.transaksi.jumlah }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransaksiBinding.inflate(
            android.view.LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = getItem(position)
        holder.bind(transaksi, onClick)
    }

    class DiffCallback: DiffUtil.ItemCallback<TransaksiBarang>() {
        override fun areItemsTheSame(
            oldItem: TransaksiBarang,
            newItem: TransaksiBarang
        ): Boolean {
            return oldItem.transaksi.id == newItem.transaksi.id
        }

        override fun areContentsTheSame(
            oldItem: TransaksiBarang,
            newItem: TransaksiBarang
        ): Boolean {
            return oldItem == newItem
        }
    }
}