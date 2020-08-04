package com.alonsorios.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.inflate
import com.alonsorios.finalapp.models.Medicina
import kotlinx.android.synthetic.main.fragment_medicina_item.view.*

class MedicamentosAdapter(val items: List<Medicina>) : RecyclerView.Adapter<MedicamentosAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_medicina_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(medicina: Medicina) = with(itemView) {
            textViewCantidad.text = medicina.Dosis
            textViewNombreMedicamento.text = medicina.Nombre_medicamento
            textViewFechaMedicamento.text = medicina.Fecha
        }
    }




}