package vilo.dev


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.material.button.MaterialButton

import android.widget.TextView as textView

class Bombo : AppCompatActivity() {
    private var numeroCartones: Int = 0
    private var precio: Double = 0.0
    private var seleccion: Int = 0
    private var arrayBombo = IntArray(90)
    private var arrayTextViews =
        Array<String?>(90) { null } // Array para guardar el estado de los TextViews
    private var bolaSaliente: String? = null // Variable para guardar el estado del TextView bola_saliente
    private var isPlaying = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bombo)
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        val botonNuevaBola = findViewById<MaterialButton>(R.id.botonNuevaBola)
        val botonPlayPause = findViewById<MaterialButton>(R.id.play)
        val botonAutomaticoManual = findViewById<MaterialButton>(R.id.manual_automatico)

        if (savedInstanceState != null) {
            numeroCartones = savedInstanceState.getInt("numeroCartones")
            precio = savedInstanceState.getDouble("precio")
            seleccion = savedInstanceState.getInt("seleccion")
            arrayBombo = savedInstanceState.getIntArray("arrayBombo") ?: IntArray(90)
            arrayTextViews = savedInstanceState.getStringArray("arrayTextViews") ?: Array<String?>(90) { null }
            bolaSaliente = savedInstanceState.getString("bolaSaliente")
            numeroSalida.visibility = savedInstanceState.getInt("bolaSalienteVisibility")
            botonNuevaBola.visibility = savedInstanceState.getInt("botonNuevaBolaVisibility")
            botonPlayPause.visibility = savedInstanceState.getInt("botonPlayPauseVisibility")
            botonAutomaticoManual.text = savedInstanceState.getString("modoActual") ?: getString(R.string.automatico)




        }

        numeroCartones = intent.getIntExtra("numeroCartones", 0)
        precio = intent.getDoubleExtra("precio", 0.0)
        seleccion = intent.getIntExtra("seleccion", 0)
        enableEdgeToEdge()
        premios(numeroCartones, precio, seleccion)
        noventaContenedores()
        for (i in 1..90) {
            val textView = findViewById<textView>(i)
            textView.text = arrayTextViews[i - 1] ?: ""
        }

        botonNuevaBola.setOnClickListener {
            if(numeroSalida.visibility == View.INVISIBLE){
                numeroSalida.visibility = View.VISIBLE
            } // Aquí se cambia la visibilidad a visible

            if (arrayBombo.contains(0)) {
                nunmeroAleatorio()
            }
            else{
                botonNuevaBola.isEnabled = false
                botonNuevaBola.text = getString(R.string.bombo_lleno)
            }


        }

        botonPlayPause.setOnClickListener {
            if(numeroSalida.visibility == View.INVISIBLE){
                numeroSalida.visibility = View.VISIBLE
            } // Aquí se cambia la visibilidad a visible}
            if (isPlaying && arrayBombo.contains(0)) {
                botonPlayPause.background =
                    AppCompatResources.getDrawable(this, R.drawable.playerpause)
                handler.postDelayed(runnable, 4000) // Inicia el cambio automático de la bola
                isPlaying = false

            } else {
                botonPlayPause.background =
                    AppCompatResources.getDrawable(this, R.drawable.playerplay)
                handler.removeCallbacks(runnable) // Detiene el cambio automático de la bola
                isPlaying = true
            }
        }
        numeroSalida.text = bolaSaliente ?: ""
        val botonNuevaPartida = findViewById<MaterialButton>(R.id.boton_nueva_partida)
        botonNuevaPartida.setOnClickListener {
            val intent = Intent(this, Configuration::class.java)
            startActivity(intent)
        }

        botonAutomaticoManual.setOnClickListener {
            if ( botonAutomaticoManual.text == getString(R.string.automatico) && botonNuevaBola.text != getString(R.string.bombo_lleno)) {
                botonAutomaticoManual.text = getString(R.string.manual)
                botonNuevaBola.visibility = View.GONE
                botonPlayPause.visibility = View.VISIBLE
                isPlaying = true
                botonPlayPause.background = AppCompatResources.getDrawable(this, R.drawable.playerplay)

            } else {
                botonAutomaticoManual.text = getString(R.string.automatico)
                handler.removeCallbacks(runnable) // Detiene el cambio automático de la bola
                botonNuevaBola.visibility = View.VISIBLE
                botonPlayPause.background = AppCompatResources.getDrawable(this, R.drawable.playerplay)
                botonPlayPause.visibility = View.GONE
            }
        }


    }


    private fun noventaContenedores() {
        for (i in 1..90) {
            val gridLayout = findViewById<GridLayout>(R.id.bomboGrid)
            val textView = textView(this)
            val layoutParams = GridLayout.LayoutParams()
            textView.layoutParams = GridLayout.LayoutParams()
            textView.text = ""
            textView.gravity = Gravity.CENTER
            textView.id = i
            textView.textSize = 20f
            layoutParams.width = 0
            layoutParams.setMargins(4, 4, 4, 4)
            layoutParams.height = 0
            layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            textView.layoutParams = layoutParams
            gridLayout.addView(textView)

        }
    }


    private fun nunmeroAleatorio() {
        var numero: Int = (1..90).random()
        while (arrayBombo.contains(numero)) {
            numero = (1..90).random()
        }
        arrayBombo[numero - 1] = numero
        val textView = findViewById<textView>(numero)
        textView.text = numero.toString()
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        numeroSalida.text = numero.toString()


    }


    private fun premios(cartones: Int, precio: Double, porcentaje: Int) {
        val botonLinea = findViewById<TextView>(R.id.Línia)
        val botonBingo = findViewById<TextView>(R.id.bingo)
        val premioTotal = cartones * precio
        var premioBingo = 0.0
        var premioLinia = 0.0
        when (porcentaje) {
            0 -> {
                premioLinia = premioTotal * 0.1
                premioBingo = premioTotal * 0.9
            }

            1 -> {
                premioLinia = premioTotal * 0.2
                premioBingo = premioTotal * 0.8
            }

            2 -> {
                premioLinia = premioTotal * 0.3
                premioBingo = premioTotal * 0.7
            }

            3 -> {
                premioLinia = premioTotal * 0.4
                premioBingo = premioTotal * 0.6
            }
        }

        botonLinea.text = getString(R.string.linia_con_premio, premioLinia)
        botonBingo.text = getString(R.string.bingo_con_premio, premioBingo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("numeroCartones", numeroCartones)
        outState.putDouble("precio", precio)
        outState.putInt("seleccion", seleccion)
        outState.putIntArray("arrayBombo", arrayBombo)
        for (i in 1..90) {
            val textView = findViewById<textView>(i)
            arrayTextViews[i - 1] = textView.text.toString()
        }
        outState.putStringArray("arrayTextViews", arrayTextViews)
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        outState.putString("bolaSaliente", numeroSalida.text.toString())
        outState.putInt("bolaSalienteVisibility", numeroSalida.visibility)
        val botonNuevaBola = findViewById<MaterialButton>(R.id.botonNuevaBola)
        outState.putInt("botonNuevaBolaVisibility", botonNuevaBola.visibility)
        val botonAutomaticoManual = findViewById<MaterialButton>(R.id.manual_automatico)
        outState.putString("modoActual",botonAutomaticoManual.text.toString())
        val botonPlayPause = findViewById<MaterialButton>(R.id.play)
        outState.putInt("botonPlayPauseVisibility", botonPlayPause.visibility)

    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Detiene el Runnable cuando la actividad se destruye
    }


    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if(arrayBombo.contains(0)) {
                nunmeroAleatorio()
                handler.postDelayed(this, 4000) // Ejecuta el Runnable cada 4 segundos
            }
            else{
                handler.removeCallbacks(this)
                val botonPlayPause = findViewById<MaterialButton>(R.id.play)
                botonPlayPause.visibility=View.GONE
                val botonBombo = findViewById<MaterialButton>(R.id.botonNuevaBola)
                botonBombo.isEnabled = false
                botonBombo.text = getString(R.string.bombo_lleno)
                botonBombo.visibility=View.VISIBLE


            }

        }


    }
}
