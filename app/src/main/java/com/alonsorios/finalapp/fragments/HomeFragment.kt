package com.alonsorios.finalapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.utils.CircleTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    private lateinit var _view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.home_fragment_string)
        _view = inflater.inflate(R.layout.fragment_home, container, false)

        setUpCurrentUser()
        setUpCurrentUserInfoUI()

        return _view
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }

    private fun setUpCurrentUserInfoUI() {
        _view.textViewInfoEmail.text = currentUser.email
        _view.textViewInfoName.text = currentUser.displayName?.let { currentUser.displayName } ?: run { getString(R.string.info_no_name) }



        Picasso.get().load(R.drawable.ic_no_image_profile).resize(300, 300)
                .centerCrop().transform(CircleTransform()).into(_view.imageViewInfoAvatar)


    }

}
