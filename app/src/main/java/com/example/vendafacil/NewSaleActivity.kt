package com.example.vendafacil

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.vendafacil.databinding.ActivityNewSaleBinding
import com.example.vendafacil.models.Sale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_new_sale.*
import java.text.SimpleDateFormat
import java.util.*

class NewSaleActivity : AppCompatActivity() {

    private var sale: Sale = Sale();
    private var db = FirebaseFirestore.getInstance()

    private val binding: ActivityNewSaleBinding by lazy {
        DataBindingUtil.setContentView<ActivityNewSaleBinding>(this, R.layout.activity_new_sale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AddBind()

        setSupportActionBar(toolbar_new_sale);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = "Voltar"

        ConfigurarBotoes()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun AddBind() {
        setContentView(R.layout.activity_new_sale)
        binding.sale = sale
    }

    private fun ConfigurarBotoes() {

        var btn = findViewById<Button>(R.id.btn_add_new_sale)
        btn.setOnClickListener(){
            btn.requestFocusFromTouch()
            if(VerificarCampos()){
                AdicionarSaleFireBase()
            }
        }

        et_sale_add_obs?.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        btn_add_new_sale.setOnClickListener(){
            if(VerificarCampos()){
                AdicionarSaleFireBase()
            }
        }

        toolbar_new_sale.setOnClickListener(){

        }

    }

    private fun AdicionarSaleFireBase() {
        var auth = FirebaseAuth.getInstance()

        val data = hashMapOf(
            "UserId" to auth.currentUser?.uid,
            "data_criacao" to getCurrentDateTime().toString("dd/MM/yyyy"),
            "nome" to sale.name,
            "observacao" to sale.obs,
            "faturado" to false,
            "valor" to sale.valueSale
            //"valor" to findViewById<EditText>(R.id.et_sale_add_value).text.toString().toDouble()
        )

        db.collection("Sale").add(data)
        finish()
    }

    private fun VerificarCampos(): Boolean {
        if(sale.name.isEmpty())
            return false
        if(sale.valueSale <= 0.0)
            return false

       return true
    }

    private fun AddSale(): Boolean {
        return true
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
