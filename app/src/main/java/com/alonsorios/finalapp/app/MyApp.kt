package com.alonsorios.finalapp.app

import android.app.Application
import com.alonsorios.finalapp.others.MySharedPreferences

val preferences: MySharedPreferences by lazy { MyApp.prefs!!}

class MyApp : Application(){
    companion object{
        var prefs :MySharedPreferences?=null
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
    }
}