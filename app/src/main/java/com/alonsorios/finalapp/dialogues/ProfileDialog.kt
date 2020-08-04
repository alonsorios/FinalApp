package com.alonsorios.finalapp.dialogues

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.models.NewProfileEvent
import com.alonsorios.finalapp.models.Profile
import com.alonsorios.finalapp.toast
import com.alonsorios.finalapp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.dialog_profile.view.*

class ProfileDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_profile,null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(view)
            .setPositiveButton(getString(R.string.dialog_ok)){_,_->
                val textName = view.editTextName.text.toString()
                val textApellido = view.editTextApellidos.text.toString()
                val textEdad = view.editTextEdad.text.toString()
                val textAltura = view.editTextAltura.text.toString()
                val textPeso = view.editTextPeso.text.toString()
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val profile =Profile(textName, textApellido, textEdad, textAltura, textPeso,userID)
                RxBus.publish(NewProfileEvent(profile))
            }
            .setNegativeButton(R.string.dialog_cancel){_,_->
                activity!!.toast("Cancelado")
            }
            .create()
    }

}