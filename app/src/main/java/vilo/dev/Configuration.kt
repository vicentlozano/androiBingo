package vilo.dev

import BaseActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.button.MaterialButton
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.widget.ImageView
import androidx.core.view.updateLayoutParams

class Configuration : BaseActivity() {
    private var numeroCartones: Int = 0
    private var precio: Double = 0.0
    private var seleccion: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.configuration_dialog)
        if (savedInstanceState != null) {
            numeroCartones = savedInstanceState.getInt("numeroCartones")
            precio = savedInstanceState.getDouble("precio")
            seleccion = savedInstanceState.getInt("seleccion")
        }
        val contenido1: EditText = findViewById(R.id.contenido1)
        val contenido2: EditText = findViewById(R.id.contenido2)
        val spinner: Spinner = findViewById(R.id.spinner)
        val opciones = resources.getStringArray(R.array.porcentajes)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                 seleccion = position
                // Ahora puedes usar la variable 'seleccion' para lo que necesites
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                 seleccion = 0
            }
        }
        val imageView = findViewById<ImageView>(R.id.dinero)
        Glide.with(this)
            .load(R.drawable.imagen_dinero)
            .apply(RequestOptions().circleCrop())
            .into(imageView)
        val continuarB: MaterialButton = findViewById(R.id.continuar)
        continuarB.setOnClickListener {
            val contenido1Text = contenido1.text.toString()
            if (contenido1Text.isNotBlank()) {
                try {
                    numeroCartones = Integer.parseInt(contenido1Text)
                    precio = contenido2.text.toString().toDouble()
                    val intent = Intent(this, Bombo::class.java)
                    intent.putExtra("numeroCartones", numeroCartones)
                    intent.putExtra("precio", precio)
                    intent.putExtra("seleccion", seleccion)
                    startActivity(intent)
                } catch (e: NumberFormatException) {
                    // Manejar el caso cuando el texto no puede ser convertido a un número
                }
            } else {
                // Manejar el caso cuando el EditText está vacío
                val intent = Intent(this, Bombo::class.java)
                intent.putExtra("numeroCartones", 0)
                intent.putExtra("precio", 0.0)
                intent.putExtra("seleccion", 0)
                startActivity(intent)
            }
        }
        if(orientacion==2 && medidasPantalla<3) {
            val marginBottom = resources.getDimensionPixelSize(R.dimen.margin_bottom)
            continuarB.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = marginBottom
            }
            val top = resources.getDimensionPixelSize(R.dimen.top_margin)
            imageView.updateLayoutParams<ViewGroup.MarginLayoutParams> {topMargin = top }

        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("numeroCartones", numeroCartones)
        outState.putDouble("precio", precio)
        outState.putInt("seleccion", seleccion)
    }

}