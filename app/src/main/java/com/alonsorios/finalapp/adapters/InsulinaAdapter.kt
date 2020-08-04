package com.alonsorios.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.inflate
import com.alonsorios.finalapp.models.Insulina
import kotlinx.android.synthetic.main.fragment_insulina_item.view.*

class InsulinaAdapter ( val items: List<Insulina>) : RecyclerView.Adapter<InsulinaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_insulina_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(insulina: Insulina) = with(itemView) {
            textViewFecha.text = insulina.Fecha
            textViewInsulina.text = insulina.Registro
        }
    }


}