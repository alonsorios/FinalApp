package com.alonsorios.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.inflate
import com.alonsorios.finalapp.models.Alimentos
import kotlinx.android.synthetic.main.fragment_alimentos_item.view.*

class AlimentosAdapter (private  val items: List<Alimentos>) : RecyclerView.Adapter<AlimentosAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_alimentos_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(alimentos: Alimentos) = with(itemView) {
            textViewNombre.text = alimentos.Nombre
            textViewCalorias.text = alimentos.Calorias
        }
    }


}