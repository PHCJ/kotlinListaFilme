package com.example.aluno.firebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener

    private lateinit var etUsuario:EditText
    private lateinit var etSenha:EditText
    private lateinit var bnCriar:Button
    private lateinit var bnAcessar:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        etUsuario = findViewById(R.id.et_login_usuario)
        etSenha = findViewById(R.id.et_login_senha)
        bnCriar = findViewById(R.id.bn_login_criar)
        bnAcessar = findViewById(R.id.bn_login_acessar)



        bnCriar.setOnClickListener{ view ->
            createUser(view, etUsuario.text.toString(),
                    etSenha.text.toString())
        }

        bnAcessar.setOnClickListener { view ->
            signIn(view, etUsuario.text.toString(),
                    etSenha.text.toString())
        }

        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null){
                var intent = Intent (this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
    override fun onStart(){
        super.onStart()
        mAuth.addAuthStateListener (mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null){
            mAuth.removeAuthStateListener { mAuthListener }
        }
    }

    fun signIn(view: View, email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if (!it.isSuccessful){
                        showMessage(view,
                                "Erro: $ {it.exception?.message}")
                    }
                }
    }

    fun createUser(view: View, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (!it.isSuccessful){
                        showMessage(view,
                                "Erro:${it.exception?.message}")
                    }
                }
    }
    fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show()
    }
}
