package com.alonsorios.finalapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.inflate
import com.alonsorios.finalapp.models.Profile
import kotlinx.android.synthetic.main.fragment_profile_item.view.*

class ProfileAdapter (private  val items: List<Profile>) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.fragment_profile_item))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(perfil: Profile) = with(itemView) {
            textViewReciveNombre.text = perfil.Nombre
            textViewReciveApelldos.text = perfil.Apellido
            textViewReciveEdad.text = perfil.Edad
            textViewReciveAltura.text = perfil.Altura
            textViewRecivePeso.text = perfil.peso
        }
    }
}