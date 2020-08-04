package com.alonsorios.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.adapters.MedicamentosAdapter
import com.alonsorios.finalapp.models.Medicina
import com.alonsorios.finalapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_medicamento.*
import kotlinx.android.synthetic.main.fragment_medicamento.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.HashMap

class Medicamento : Fragment()  {

    lateinit var _view: View

    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var medicamentoDBRef: CollectionReference

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private lateinit var adapter : MedicamentosAdapter
    private val listamedicina : ArrayList<Medicina> = ArrayList()

    private var medicamentoSubscription: ListenerRegistration? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.medicamento_fragment_string)
        _view = inflater.inflate(R.layout.fragment_medicamento, container, false)

        setUpInsulinaDB()
        setUpCurrentUser()

        setUpReciclerView()
        setUpMedicamentoBtn()
        subscribeToMedicina()

        return _view
    }

    fun setUpInsulinaDB(){
        medicamentoDBRef =store.collection("Registro_medicamento")
    }

    fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    private fun setUpReciclerView() {
        val layoutManager = LinearLayoutManager(context)
        adapter = MedicamentosAdapter(listamedicina)
        _view.recyclerViewMedicamento.setHasFixedSize(true)
        _view.recyclerViewMedicamento.layoutManager = layoutManager
        _view.recyclerViewMedicamento.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewMedicamento.adapter =adapter
    }

    fun setUpMedicamentoBtn(){
        _view.botonGuardarMedicacion.setOnClickListener(){
            val medicinaText = _view.editTextMedicamento.text.toString()
            val dosisIngerida = _view.editTextDosis.text.toString()
            if (medicinaText.isNotEmpty()){
                val nivel = Medicina(currentUser.uid, medicinaText,  dosisIngerida,Date().toString())
                saveMedicina(nivel)
                _view.editTextMedicamento.setText("")
                _view.editTextDosis.setText("")
            }else{
                activity!!.toast("Vacio")
            }
        }
    }

    private fun saveMedicina(medicina : Medicina){
        val newMedicina = HashMap<String, Any>()
        newMedicina["Userid"] = medicina.Userid
        newMedicina["Nombre_medicamento"] = medicina.Nombre_medicamento
        //newMedicina["Tipo_medicamento"] = medicina.Tipo_medicamento
        newMedicina["Dosis"] = medicina.Dosis
        newMedicina["Fecha"] = medicina.Fecha
        medicamentoDBRef.add(newMedicina)
            .addOnCompleteListener{
                activity!!.toast("Añadido")
            }
            .addOnFailureListener{
                activity!!.toast("Error")
            }
    }

    private fun subscribeToMedicina() {
        medicamentoSubscription =medicamentoDBRef
            .whereEqualTo("Userid",currentUser.uid)
            .addSnapshotListener(object :EventListener,com.google.firebase.firestore.EventListener<QuerySnapshot>{
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        return
                    }
                    snapshot?.let {
                        listamedicina.clear()
                        val medicamento = it.toObjects(Medicina::class.java)
                        listamedicina.addAll(medicamento)
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewMedicamento.smoothScrollToPosition(0)
                    }
                }
            })
    }



    override fun onDestroyView() {
        medicamentoSubscription?.remove()
        super.onDestroyView()
    }



}
