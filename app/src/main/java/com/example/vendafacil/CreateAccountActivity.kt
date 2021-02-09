package com.example.vendafacil

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.vendafacil.databinding.ActivityCreateAccountBinding
import com.example.vendafacil.models.User
import com.google.firebase.auth.*
import java.util.regex.Pattern

class CreateAccountActivity : AppCompatActivity() {

    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=\\S+$).{6,}$")
    private var user: User = User()
    private lateinit var auth: FirebaseAuth

    private var statusEmail : Boolean = false;
    private var statusPassword : Boolean = false;
    private var statusConfirmPassword: Boolean = false;

    private val binding: ActivityCreateAccountBinding by lazy {
        DataBindingUtil.setContentView<ActivityCreateAccountBinding>(
            this,
            R.layout.activity_create_account
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConfigSupportBar()
        AddBind()
        ActiveValidateFields()
        ActiveButtonsClick()
    }

    private fun ActiveButtonsClick() {
        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            btnRegister.requestFocusFromTouch()
            if (VerifyUser())
                SignUp()
        }
    }

    private fun ActiveValidateFields() {
        val etEmail = findViewById<EditText>(R.id.et_email_register);
        etEmail.setOnFocusChangeListener { p0, p1 -> ValidateEmail(user.email, etEmail) }

        val etPassword = findViewById<EditText>(R.id.et_password_register);
        etPassword.setOnFocusChangeListener { p0, p1 -> ValidatePassword(user.password, etPassword) }

        val etConfirmPassword = findViewById<EditText>(R.id.et_confirmpassword_register);
        etConfirmPassword.setOnFocusChangeListener { p0, p1 -> ValidateConfirmPassword(findViewById<EditText>(R.id.et_confirmpassword_register).text.toString(), user.password, etConfirmPassword) }
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

    private fun ValidatePassword(password: String, etPassword: EditText?){
        statusPassword = false
        if(password.isNotEmpty()){
            if(!PASSWORD_PATTERN.matcher(password).matches())
                etPassword?.error = "O campo possui valores incorretos"
            else
                statusPassword = true
        }
    }

    private fun ValidateConfirmPassword(confirmPassword: String, password: String, etConfirmPassword: EditText?){
        statusConfirmPassword = false
        if(confirmPassword.isNotEmpty()){
            if(confirmPassword != password)
                etConfirmPassword?.error = "O campo possui valores incorretos"
            else
                statusConfirmPassword = true
        }
    }

    private fun AddBind() {
        setContentView(R.layout.activity_create_account)
        binding.user = user
    }

    private fun VerifyUser(): Boolean {
        return (statusEmail) && (statusPassword) && (statusConfirmPassword)
    }

    private fun ConfigSupportBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Voltar"
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.colorAccent2
                )
            )
        )
    }

    private fun SignUp() {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Create Account", "createUserWithEmail:success")

                    SendEmailSignUp(auth.currentUser)
                    Toast.makeText(
                        baseContext, "Confirme o e-mail de verificação.",
                        Toast.LENGTH_SHORT
                    ).show()
                    this.finish()

                }else{
                    var erro : String? = null;

                    if (task.exception is FirebaseAuthUserCollisionException){
                        erro = "E-mail já cadastrado"
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException){
                        erro = "E-mail inválido"
                    } else if(task.exception is FirebaseAuthWeakPasswordException){
                        erro = "Senha inválida"
                    }else{
                        erro = "Erro desconhecido"
                    }

                    Log.w("Create Accout", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, erro,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun SendEmailSignUp(currentUser: FirebaseUser?) {
        currentUser?.sendEmailVerification()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
