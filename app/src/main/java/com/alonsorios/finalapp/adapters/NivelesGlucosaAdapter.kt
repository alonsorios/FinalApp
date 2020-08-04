package com.alonsorios.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.inflate
import com.alonsorios.finalapp.models.NivelGlucosa
import kotlinx.android.synthetic.main.fragment_niveles_g_item.view.*
import java.text.SimpleDateFormat

class NivelesGlucosaAdapter ( val items: List<NivelGlucosa>) : RecyclerView.Adapter<NivelesGlucosaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_niveles_g_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nivelesGlucosa: NivelGlucosa) = with(itemView) {
            textViewFecha.text = nivelesGlucosa.Fecha
            textViewNivel.text = nivelesGlucosa.Nivel
        }
    }


}