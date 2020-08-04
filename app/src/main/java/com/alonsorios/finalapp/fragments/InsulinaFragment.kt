package com.alonsorios.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.adapters.InsulinaAdapter
import com.alonsorios.finalapp.models.Insulina
import com.alonsorios.finalapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_insulina.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList

class InsulinaFragment : Fragment() {

    private lateinit var _view : View

    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var insulinaDBRef: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private lateinit var adapter : InsulinaAdapter
    private val listainsulina : ArrayList<Insulina> = ArrayList()

    private var insulinaSubscription: ListenerRegistration? = null


    override fun onCreateView(        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.insulina_fragment_string)
        _view = inflater.inflate(R.layout.fragment_insulina, container, false)

        setUpInsulinaDB()
        setUpCurrentUser()

        setUpReciclerView()
        setUpInsulinaBtn()
        subscribeToInsulina()

        return _view
    }

    fun setUpInsulinaDB(){
        insulinaDBRef =store.collection("Registro_insulina")
    }

    fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    fun setUpReciclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = InsulinaAdapter(listainsulina)
        _view.recyclerViewInsulina.setHasFixedSize(true)
        _view.recyclerViewInsulina.layoutManager = layoutManager
        _view.recyclerViewInsulina.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewInsulina.adapter =adapter
    }

    fun setUpInsulinaBtn(){
        _view.botonGuardarNivelInsulina.setOnClickListener(){
            val insulinaText = _view.editTextNivelInsulina.text.toString()
            if (insulinaText.isNotEmpty()){
                val nivel = Insulina(currentUser.uid, insulinaText, Date().toString())
                saveInsulina(nivel)
                _view.editTextNivelInsulina.setText("")
            }else{
                activity!!.toast("Vacio")
            }
        }
    }

    private fun saveInsulina(insulina : Insulina){
        val newInsulina = HashMap<String, Any>()
        newInsulina["Userid"] = insulina.Userid
        newInsulina["Registro"] = insulina.Registro
        newInsulina["Fecha"] = insulina.Fecha
        insulinaDBRef.add(newInsulina)
            .addOnCompleteListener{
                activity!!.toast("Añadido")
            }
            .addOnFailureListener{
                activity!!.toast("Error")
            }
    }

    fun subscribeToInsulina(){
        insulinaSubscription = insulinaDBRef
            .whereEqualTo("Userid",currentUser.uid)
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        //activity!!.toast(exception.message.toString())
                        return
                    }

                    snapshot?.let {
                        listainsulina.clear()
                        val insulina = it.toObjects(Insulina::class.java)
                        listainsulina.addAll(insulina)
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewInsulina.smoothScrollToPosition(0)
                    }
                }
            })
    }

    override fun onDestroyView() {
        insulinaSubscription?.remove()
        super.onDestroyView()
    }

}
