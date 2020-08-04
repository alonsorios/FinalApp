package com.alonsorios.finalapp.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.app.preferences
import com.alonsorios.finalapp.toast
import kotlinx.android.synthetic.main.fragment_alertar.*
import kotlinx.android.synthetic.main.fragment_alertar.view.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlertarFragment : Fragment() {

    private lateinit var _view : View
    var CODE = 0
    val REQUEST_READ_CONTACTS = 3
    val PICK_CONTACT_REQUEST = 4


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.alertar_fragment_string)
        _view = inflater.inflate(R.layout.fragment_alertar, container, false)

        setUpLlamadaBtn()
        setUpMensajeBtn()
        //getvaluesSharedPreferences()

        return _view
    }

    private fun getvaluesSharedPreferences() {
        if (preferences.nombreLlamada.isNotEmpty() && preferences.numeroLlamada.isNotEmpty()){
            val nombre: String =preferences.nombreLlamada
            val numero: String =preferences.nombreLlamada
            textViewNombreLlamada.text =nombre
            textViewNumero.text = numero
        }
        if (preferences.nombreMensaje.isNotEmpty() && preferences.numeroMensaje.isNotEmpty()){
            val nombre2: String =preferences.nombreMensaje
            val numero2: String =preferences.numeroMensaje
            textViewNombreMensaje.text = nombre2
            textViewNumero2.text = numero2
        }
    }

    private fun setUpMensajeBtn() {
        _view.imageViewEditar2.setOnClickListener(){
            if (ActivityCompat.checkSelfPermission(_view.context,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                CODE=1
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_READ_CONTACTS) }
            }else{
                CODE=1
                seleccionaContacto()
            }
        }
    }

    private fun setUpLlamadaBtn() {
        _view.imageViewEditar.setOnClickListener(){
            if (ActivityCompat.checkSelfPermission(_view.context,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                CODE=2
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.READ_CONTACTS),REQUEST_READ_CONTACTS) }
            }else{
                CODE=2
                seleccionaContacto()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==REQUEST_READ_CONTACTS)seleccionaContacto()
    }

    private fun seleccionaContacto() {
        val i = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(i,PICK_CONTACT_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_CONTACT_REQUEST){
            if(resultCode == RESULT_OK) {
                val contactUri: Uri = data!!.data
                renderContact(contactUri)
            }
        }
    }

    private fun renderContact(contactUri: Uri) {
        if (CODE==1){
            textViewNombreMensaje.text = getName(contactUri)
            textViewNumero2.text = getPhone(contactUri)
            //Aqui guardamos las Shared Preferences
            preferences.nombreMensaje = getName(contactUri) as String
            preferences.numeroMensaje = getPhone(contactUri) as String
        }else if (CODE==2){
            textViewNombreLlamada.text = getName(contactUri)
            textViewNumero.text = getPhone(contactUri)
            //Aqui guardamos las Shared Preferences
            preferences.nombreLlamada = getName(contactUri) as String
            preferences.numeroLlamada = getPhone(contactUri) as String
        }
    }

    private fun getPhone(contactUri: Uri): String? {
        var id: String? = null
        var phone: String? = null

        val contactCursor: Cursor = activity!!.getContentResolver().query(
            contactUri, arrayOf(ContactsContract.Contacts._ID),
            null, null, null
        )

        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(0)
        }
        contactCursor.close()

        val selectionArgs = Phone.CONTACT_ID + " = ? AND " +
                Phone.TYPE + "= " +
                Phone.TYPE_MOBILE

        val phoneCursor: Cursor = activity!!.getContentResolver().query(
            Phone.CONTENT_URI, arrayOf(Phone.NUMBER),
            selectionArgs
            , arrayOf(id),
            null
        )
        if (phoneCursor.moveToFirst()) {
            phone = phoneCursor.getString(0)
        }
        phoneCursor.close()

        return phone
    }

    private fun getName(contactUri: Uri): CharSequence? {
        var name: String? = null
        val contentResolver: ContentResolver = activity!!.getContentResolver()
        val c: Cursor = contentResolver.query(
            contactUri, arrayOf(ContactsContract.Contacts.DISPLAY_NAME), null, null, null
        )
         if (c.moveToFirst()) {
            name = c.getString(0)
        }
        c.close()

        return name
    }

}
