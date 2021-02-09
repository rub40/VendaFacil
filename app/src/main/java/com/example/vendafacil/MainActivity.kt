package com.example.vendafacil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.vendafacil.fragments.ListSaleFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*supportActionBar?.hide();*/
        setSupportActionBar(toolbar_main_window);
        setContentView(R.layout.activity_main)

        var fragment = ListSaleFragment()
        supportFragmentManager.beginTransaction().add(R.id.mainContainer, fragment).commit()

        val actionBar = supportActionBar;
        actionBar?.title = "Barra de navegação";

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar_main_window,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ){

        }

        drawerToggle.isDrawerIndicatorEnabled = true;
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_exit ->{
                    Sair();
                    true
                }

                R.id.nav_help ->{
                    AbrirHelp()
                    true
                }

                R.id.nav_sale ->{
                    AbrirListaVendas()
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    private fun AbrirListaVendas(){
        val intent = Intent(this, RelatorioActivity::class.java)
        startActivity(intent)

    }


    private fun AbrirHelp() {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    private fun Sair() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
