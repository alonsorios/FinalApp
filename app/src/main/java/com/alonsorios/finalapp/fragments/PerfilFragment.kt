package com.alonsorios.finalapp.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.adapters.ProfileAdapter
import com.alonsorios.finalapp.dialogues.ProfileDialog
import com.alonsorios.finalapp.models.NewProfileEvent
import com.alonsorios.finalapp.models.Profile
import com.alonsorios.finalapp.toast
import com.alonsorios.finalapp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_perfil.view.*
import java.util.*
import java.util.EventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class PerfilFragment : Fragment() {

    private lateinit var _view : View

    private lateinit var adapter : ProfileAdapter
    private val profileList : ArrayList<Profile> = ArrayList()

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private val store : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var profileDBRef: CollectionReference

    private var profileSubscription: ListenerRegistration? = null
    private lateinit var profileBusListener :  Disposable



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.perfil_fragment_string)
        _view = inflater.inflate(R.layout.fragment_perfil, container, false)

        setUpProfileDB()
        setUpCurrentUser()

        setUpReciclerView()
        setUpProfileButton()

        subscribeToProfiles()
        subscribeToNewProfiles()
        return _view
    }

    fun setUpProfileDB(){
        profileDBRef =store.collection("profile")
    }

    fun setUpCurrentUser(){
        currentUser = mAuth.currentUser!!
    }

    fun setUpReciclerView(){
        val layoutManager = LinearLayoutManager(context)
        adapter = ProfileAdapter(profileList)
        _view.recyclerViewProfile.setHasFixedSize(true)
        _view.recyclerViewProfile.layoutManager = layoutManager
        _view.recyclerViewProfile.itemAnimator = DefaultItemAnimator()
        _view.recyclerViewProfile.adapter =adapter
    }

    fun setUpProfileButton(){
        _view.editProfile.setOnClickListener{ProfileDialog().show(
                fragmentManager!!,"")}
    }

    private fun saveProfile(profile: Profile){
        val newProfile = HashMap<String, Any>()
        newProfile["Nombre"] =profile.Nombre
        newProfile["Apellido"] =profile.Apellido
        newProfile["Edad"] =profile.Edad
        newProfile["Altura"] =profile.Altura
        newProfile["peso"] =profile.peso
        newProfile["userID"] =profile.userID

        profileDBRef.add(newProfile)
            .addOnCompleteListener{
                activity!!.toast("Datos Actualizados")
            }
            .addOnFailureListener{
                activity!!.toast("Error, intenta de nuevo")
            }
    }

    private fun subscribeToProfiles(){
        //val query = profileDBRef.whereEqualTo("userID", currentUser.uid)

        profileSubscription = profileDBRef
            .whereEqualTo("userID", currentUser.uid)
            .orderBy("Nombre",Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener, com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
                    exception?.let {
                        //activity!!.toast(exception.message.toString())
                        return
                    }

                    snapshot?.let {
                        profileList.clear()
                        val profile = it.toObjects(Profile::class.java)
                        profileList.addAll(profile)
                        adapter.notifyDataSetChanged()
                        _view.recyclerViewProfile.smoothScrollToPosition(0)
                    }
                }
            })
    }

    private fun subscribeToNewProfiles(){
        profileBusListener = RxBus.listen(NewProfileEvent::class.java).subscribe({
            saveProfile(it.profile)
        })
    }

    override fun onDestroyView() {
        profileBusListener.dispose()
        profileSubscription?.remove()
        super.onDestroyView()
    }

}
