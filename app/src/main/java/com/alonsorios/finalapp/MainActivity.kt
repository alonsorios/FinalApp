package com.alonsorios.finalapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.alonsorios.finalapp.activities.LoginActivity
import com.alonsorios.finalapp.adapters.PagerAdapter
import com.alonsorios.finalapp.app.MyApp.Companion.prefs
import com.alonsorios.finalapp.app.preferences
import com.alonsorios.finalapp.fragments.*
import com.alonsorios.finalapp.others.ToolbarActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarToLoad(toolbarView as Toolbar)

        setNavDrawer()
        setUserHeaderInformation()

        if (savedInstanceState == null) {
            fragmentTransaction(HomeFragment())
            nav_view.menu.getItem(0).isChecked = true
        }

    }

    private fun setNavDrawer(){
        val toggle = ActionBarDrawerToggle(this, drawerLayout, _toolbar, R.string.open_drawer, R.string.close_drawer)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun fragmentTransaction(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun loadFragmentById(id: Int) {
        when(id){
            R.id.nav_home->fragmentTransaction(HomeFragment())
            R.id.nav_night_mode->fragmentTransaction(NocturnoFragment())
            R.id.nav_alerta->fragmentTransaction(AlertarFragment())
            R.id.nav_levels->fragmentTransaction(NivelesFragment())
            R.id.nav_insulina->fragmentTransaction(InsulinaFragment())
            R.id.nav_medicamento->fragmentTransaction(Medicamento())
            R.id.nav_alimentos->fragmentTransaction(AlimenticiaFragment())
            R.id.nav_recordatorios->fragmentTransaction(RecordatoriosFragment())
            R.id.nav_profile->fragmentTransaction(PerfilFragment())
        }
    }

    private fun setUserHeaderInformation() {
        //val name = nav_view.getHeaderView(0).findViewById<TextView>(R.id.textViewName)
        val email = nav_view.getHeaderView(0).findViewById<TextView>(R.id.textViewEmail)
        //val foto = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageViewAvatar)
        val user = mAuth.currentUser
        var usermail = ""
        //var username = ""
        //var photoURL =""
        user?.let {
            usermail = user.email.toString()
            //username?.let{ user.displayName.toString()}
        /*    user.photoUrl?.let {
                Picasso.get().load(user.photoUrl).resize(100, 100).into(foto)
            }?: kotlin.run {
                Picasso.get().load(R.drawable.ic_no_image).resize(100, 100).into(foto)
            }*/
        }
        email?.let { email.text = usermail }
        //name?.let { name.text = username }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        loadFragmentById(item.itemId)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.general_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                preferences.numeroMensaje=""
                preferences.nombreMensaje=""
                preferences.nombreLlamada=""
                preferences.numeroLlamada=""
                FirebaseAuth.getInstance().signOut()
                goToActivity<LoginActivity> {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
