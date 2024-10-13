package com.example.applogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var app: UserApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = application as UserApp // Asegúrate de que esto esté correcto

        val btnLogin: Button = findViewById(R.id.buttonLogin)
        val editUsername: EditText = findViewById(R.id.editTextUsername)
        val editPassword: EditText = findViewById(R.id.editTextPassword)

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Validar que la contraseña tenga más de 8 caracteres
                if (password.length <= 8) {
                    Toast.makeText(this, "La contraseña debe tener más de 8 caracteres", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Salir del listener si la contraseña no es válida
                }

                lifecycleScope.launch {
                    val user = app.room.userDao().getUser(username, password)

                    if (user == null) {
                        // El usuario no existe, se puede registrar
                        val newUser = User(id = 0, name = username, password = password)
                        app.room.userDao().insert(newUser)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "Usuario registrado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // El usuario ya existe
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
