package sauceda.rayos.practicaautenticacionsaucedar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val email: EditText = findViewById(R.id.etEmail)
        val password: EditText = findViewById(R.id.etPassword)
        val error: TextView = findViewById(R.id.tvError)

        val button = findViewById<Button>(R.id.btnLogin)
        val register = findViewById<Button>(R.id.btnGoRegister)
        error.visibility = TextView.INVISIBLE

        register.setOnClickListener(){
            val intent = Intent(this, SignInActivity::class.java)

            startActivity(intent)
        }

        button.setOnClickListener({
            if(email.text.isEmpty() || password.text.isEmpty()){
                showError("Por favor, rellene todos los campos",true)
            }else{
                login(email.text.toString(),password.text.toString())
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToMain(user: FirebaseUser){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
    fun showError(text:String = "",visible:Boolean){
        val error: TextView = findViewById(R.id.tvError)
        error.text = text
        error.visibility = if(visible) TextView.VISIBLE else TextView.INVISIBLE
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            goToMain(currentUser)
        }
    }

    fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    goToMain(user!!)
                } else {
                    showError("Error al iniciar sesión",true)
                }
            }
    }

}