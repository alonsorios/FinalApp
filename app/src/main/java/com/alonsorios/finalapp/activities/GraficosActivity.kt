package com.alonsorios.finalapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alonsorios.finalapp.R
import com.alonsorios.finalapp.models.Insulina
import com.github.mikephil.charting.charts.LineChart

class GraficosActivity : AppCompatActivity() {
//https://medium.com/@yilmazvolkan/kotlinlinecharts-c2a730226ff1

    val arreglo = ArrayList<Insulina>()
    val xAxisLabel  = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficos)
    }
}
