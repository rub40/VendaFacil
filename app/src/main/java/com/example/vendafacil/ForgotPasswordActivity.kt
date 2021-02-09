package com.example.vendafacil

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.vendafacil.databinding.ActivityForgotPasswordBinding
import com.example.vendafacil.models.User
import com.google.firebase.auth.FirebaseAuth


class ForgotPasswordActivity : AppCompatActivity() {

    private var user: User = User()
    private lateinit var auth: FirebaseAuth

    private var statusEmail : Boolean = false;

    private val binding: ActivityForgotPasswordBinding by lazy {
        DataBindingUtil.setContentView<ActivityForgotPasswordBinding>(
            this,
            R.layout.activity_forgot_password
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configSupportBar();
        AddBind()
        ActiveValidateField()
        ActiveButtonsClick()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun configSupportBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Voltar"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent2)));
    }

    private fun AddBind() {
        setContentView(R.layout.activity_forgot_password)
        binding.user = user
    }

    private fun ActiveValidateField() {
        val etEmail = findViewById<EditText>(R.id.et_email_forgotpassword);
        etEmail.setOnFocusChangeListener { p0, p1 -> ValidateEmail(user.email, etEmail) }

    }

    private fun ValidateEmail(email: String, etEmail: EditText?) {
        statusEmail = false
        if(email.isNotEmpty()){
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                etEmail?.error = "o campo possui valores incorretos"
            else
                statusEmail = true
        }
    }

    private fun Send(){
        auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(user.email)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Forgot Password", "sendPasswordResetEmail:success")

                    Toast.makeText(
                        baseContext, "Enviado com sucesso.",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.finish()

                }else{
                    Toast.makeText(
                        baseContext, "Falha ao enviar email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun ActiveButtonsClick() {
        val btnSendEmail = findViewById<Button>(R.id.btn_send_forgotpassword)
        btnSendEmail.setOnClickListener {
            btnSendEmail.requestFocusFromTouch()
            if (VerifyEmail())
                Send()
        }
    }

    private fun VerifyEmail(): Boolean {
        return (statusEmail)
    }
}
