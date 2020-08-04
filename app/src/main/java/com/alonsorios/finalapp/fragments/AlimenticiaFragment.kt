package com.alonsorios.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.adapters.AlimentosAdapter
import com.alonsorios.finalapp.models.Alimentos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.fragment_alimenticia.view.*
import java.util.EventListener
import kotlin.collections.ArrayList

class AlimenticiaFragment : Fragment() {

    private lateinit var _view : View

    private lateinit var adapter : AlimentosAdapter
    private val listaalimentos : ArrayList<Alimentos> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var alimentosDBRef: CollectionReference

    private var alimentosSubscription: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.alimentos_fragment_string)
        _view = inflater.inflate(R.layout.fragment_alimenticia, container, false)

        setUpProfileDB()
        setUpCurrentUser()

        setUpReciclerView()
        subscribeToAlimentos()

        return _view
    }

    fun setUpProfileDB(){
        alimentosDBRef =store.collection("Alimentos")
    }

    fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    fun setUpReciclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = AlimentosAdapter(listaalimentos)
        _view.recyclerViewAlimentos.setHasFixedSize(true)
        _view.recyclerViewAlimentos.layoutManager = layoutManager
        _view.recyclerViewAlimentos.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewAlimentos.adapter =adapter
    }

    private fun subscribeToAlimentos(){
        //val query = profileDBRef.whereEqualTo("userID", currentUser.uid)

        alimentosSubscription = alimentosDBRef
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        //activity!!.toast(exception.message.toString())
                        return
                    }

                    snapshot?.let {
                        listaalimentos.clear()
                        val alimento = it.toObjects(Alimentos::class.java)
                        listaalimentos.addAll(alimento)
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewAlimentos.smoothScrollToPosition(0)
                    }
                }
            })
    }

    override fun onDestroyView() {
        alimentosSubscription?.remove()
        super.onDestroyView()
    }
}
