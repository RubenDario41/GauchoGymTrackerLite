package com.example.gimnasio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val etNombreEjercicio = findViewById<EditText>(R.id.etNombreEjercicio)
        val etRepeticiones = findViewById<EditText>(R.id.etRepeticiones)
        val etSeries = findViewById<EditText>(R.id.etSeries)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)

        btnIniciar.setOnClickListener {
            val nombre = etNombreEjercicio.text.toString()
            val repeticiones = etRepeticiones.text.toString().toIntOrNull()
            val series = etSeries.text.toString().toIntOrNull()

            if (nombre.isNotBlank() && repeticiones != null && series != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("nombre", nombre)
                intent.putExtra("repeticiones", repeticiones)
                intent.putExtra("series", series)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
