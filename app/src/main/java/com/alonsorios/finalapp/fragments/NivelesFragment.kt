package com.alonsorios.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.adapters.NivelesGlucosaAdapter
import com.alonsorios.finalapp.models.NivelGlucosa
import com.alonsorios.finalapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_niveles.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NivelesFragment : Fragment() {

    private lateinit var _view : View

    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var nivelesDBRef: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private lateinit var adapter : NivelesGlucosaAdapter
    private val listaniveles : ArrayList<NivelGlucosa> = ArrayList()

    private var nivelesSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.niveles_fragment_string)
        _view = inflater.inflate(R.layout.fragment_niveles, container, false)

        setUpProfileDB()
        setUpCurrentUser()

        setUpReciclerView()
        setUpNvBtn()
        subscribeToNiveles()

        return _view
    }

    fun setUpProfileDB(){
        nivelesDBRef =store.collection("Niveles_glucosa")
    }

    fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    fun setUpReciclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = NivelesGlucosaAdapter(listaniveles)
        _view.recyclerViewNiveles.setHasFixedSize(true)
        _view.recyclerViewNiveles.layoutManager = layoutManager
        _view.recyclerViewNiveles.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewNiveles.adapter =adapter
    }

    private fun setUpNvBtn(){
        _view.botonGuardarNivel.setOnClickListener(){
            val nivelText = _view.editTextNivelGlucosa.text.toString()
            if (nivelText.isNotEmpty()){
                val nivel = NivelGlucosa(currentUser.uid, nivelText, Date().toString())
                saveNivel(nivel)
                _view.editTextNivelGlucosa.setText("")
            }else{
                activity!!.toast("Vacio")
            }
        }
    }

    private fun saveNivel(nivel : NivelGlucosa){
        val newNivel = HashMap<String, Any>()
        newNivel["Userid"] = nivel.Userid
        newNivel["Nivel"] = nivel.Nivel
        newNivel["Fecha"] = nivel.Fecha
        nivelesDBRef.add(newNivel)
            .addOnCompleteListener{
                activity!!.toast("Añadido")
            }
            .addOnFailureListener{
                activity!!.toast("Error")
            }
    }

    private fun subscribeToNiveles(){
        nivelesSubscription = nivelesDBRef
            .whereEqualTo("Userid",currentUser.uid)
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        //activity!!.toast(exception.message.toString())
                        return
                    }

                    snapshot?.let {
                        listaniveles.clear()
                        val nivel = it.toObjects(NivelGlucosa::class.java)
                        listaniveles.addAll(nivel)
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewNiveles.smoothScrollToPosition(0)
                    }
                }
            })
    }

    override fun onDestroyView() {
        nivelesSubscription?.remove()
        super.onDestroyView()
    }

}
