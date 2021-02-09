package com.example.vendafacil

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.vendafacil.databinding.ActivityLoginBinding
import com.example.vendafacil.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity() {

    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=\\S+$).{6,}$")
    private var user: User = User()
    private lateinit var auth: FirebaseAuth

    private var statusEmail: Boolean = false
    private var statusPassword: Boolean = false

    private val binding: ActivityLoginBinding by lazy {
       DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        AddBind()
        ActiveValidateField()
        ActivateButtonClick()
    }

    private fun ActiveValidateField() {
        val etEmail = findViewById<EditText>(R.id.et_email_login);
        etEmail.setOnFocusChangeListener { p0, p1 -> ValidateEmail(user.email, etEmail) }

        val etPassword = findViewById<EditText>(R.id.et_password_login);
        etPassword.setOnFocusChangeListener { p0, p1 -> ValidatePassword(user.password, etPassword) }
    }

    private fun AddBind() {
        setContentView(R.layout.activity_login)
        binding.user = user;
    }

    private fun ActivateButtonClick() {

        val tvCreateAccount = findViewById<TextView>(R.id.createAccount)
        tvCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        val tvForgotPassword = findViewById<TextView>(R.id.forgotPassword)
        tvForgotPassword.setOnClickListener(){
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val btnLogin = findViewById<Button>(R.id.button_login);
        btnLogin.setOnClickListener{
            btnLogin.requestFocusFromTouch()
            if(VerifyUser())
                SignIn();
        }
    }

    private fun VerifyUser(): Boolean {
        ValidateEmail(user.email, findViewById(R.id.et_email_login))
        ValidatePassword(user.password, findViewById(R.id.et_password_login))
        return (statusEmail) && (statusPassword)
    }

    private fun SignIn() {

        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithEmail:success")
                    val user = auth.currentUser
                    UpdateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Falha de autenticação",
                        Toast.LENGTH_SHORT).show()
                    UpdateUI(null)
                }
            }
    }

    private fun UpdateUI(firebaseUser: FirebaseUser?){
        if(firebaseUser != null){

            auth.updateCurrentUser(firebaseUser)

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        }
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
}
