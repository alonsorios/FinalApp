package com.alonsorios.finalapp.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.PhoneNumberUtils
import android.telephony.SmsManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.toast
import kotlinx.android.synthetic.main.fragment_nocturno.view.*

class NocturnoFragment : Fragment() {

    private lateinit var _view: View
    val numero_telefono= "5578120570"
    val REQUEST_PHONE_CALL = 1
    val REQUEST_SEND_SMS = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        activity?.setTitle(R.string.nocturno_fragment_string)
        _view = inflater.inflate(R.layout.fragment_nocturno, container, false)
        setUpLlamadaBtn()
        setUpSMSBtn()
        return _view
    }

    fun setUpSMSBtn() {
        _view.buttonSMS.setOnClickListener(){
            if (ActivityCompat.checkSelfPermission(_view.context,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.SEND_SMS),REQUEST_SEND_SMS) }
            }else{
                sendSMS()
            }
        }
    }
    fun setUpLlamadaBtn() {
        _view.buttonLlamada.setOnClickListener(){
            if (ActivityCompat.checkSelfPermission(_view.context, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                activity?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL) }
            }else {
                startCall()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==REQUEST_PHONE_CALL)startCall()
        if (requestCode==REQUEST_SEND_SMS)sendSMS()
    }

    private fun startCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:"+numero_telefono)
        startActivity(callIntent)
    }
    
    private fun sendSMS() {
        val Number = "5578120570"
        val text = "Hola prueba de SMS 2"
        SmsManager.getDefault().sendTextMessage(Number,null,text,null,null)
        activity!!.toast("Mensaje de texto enviado")
    }


}
