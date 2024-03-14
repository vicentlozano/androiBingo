package vilo.dev

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity :  AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.menu)


                val botonInicio = findViewById<Button>(R.id.botonInicio)
                botonInicio.setOnClickListener {
                    val intent = Intent(this, Configuration::class.java)
                    startActivity(intent)
                }
            }







}