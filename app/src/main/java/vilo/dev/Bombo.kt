package vilo.dev

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Looper
import android.view.Gravity
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.GridLayout
import com.google.android.material.button.MaterialButton
import android.widget.TextView as textView

class Bombo : AppCompatActivity() {
    private var numeroCartones: Int = 0
    private var precio: Double = 0.0
    private var seleccion: Int = 0
    private var arrayBombo = IntArray(90)
    private var arrayTextViews = Array<String?>(90) { null } // Array para guardar el estado de los TextViews
    private var bolaSaliente: String? = null // Variable para guardar el estado del TextView bola_saliente



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            numeroCartones = savedInstanceState.getInt("numeroCartones")
            precio = savedInstanceState.getDouble("precio")
            seleccion = savedInstanceState.getInt("seleccion")
            arrayBombo = savedInstanceState.getIntArray("arrayBombo") ?: IntArray(90)
            arrayTextViews = savedInstanceState.getStringArray("arrayTextViews") ?: Array<String?>(90) { null }
            bolaSaliente = savedInstanceState.getString("bolaSaliente")



        }
        setContentView(R.layout.bombo)
         numeroCartones = intent.getIntExtra("numeroCartones", 0)
         precio = intent.getDoubleExtra("precio", 0.0)
         seleccion = intent.getIntExtra("seleccion", 0)
        enableEdgeToEdge()
        premios(numeroCartones,precio,seleccion)
        noventaContenedores()
        for (i in 1..90) {
            val textView = findViewById<textView>(i)
            textView.text = arrayTextViews[i - 1] ?: ""
        }
        val botonBombo = findViewById<MaterialButton>(R.id.botonNuevaBola)
        botonBombo.setOnClickListener {
            nunmeroAleatorio()
        }
        val botonBingo = findViewById<MaterialButton>(R.id.bingo)
        botonBingo.setOnClickListener {
            val count = arrayBombo.count { it !==0}
            if (count >= 15){
            showAlertBingo(getString(R.string.es_bingo))}
        }
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        numeroSalida.text = bolaSaliente ?: ""
        //animaciones botonoes bingo y linia
        val botonLinea: MaterialButton = findViewById(R.id.Línia)
        botonLinea.setOnClickListener {
            val count = arrayBombo.count { it != 0 }
            if (count >= 5) {
            showAlertLinia()}
        }
        val handlerLinea = android.os.Handler(Looper.getMainLooper())
        val runnableLinea = object : Runnable {
            override fun run() {
                // Crear y ejecutar la animación para el botón de línea
                val animacionLinea = ObjectAnimator.ofPropertyValuesHolder(
                    botonLinea,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f)
                ).apply {
                    duration = 500 // duración en milisegundos
                    repeatCount = 1
                    repeatMode = ObjectAnimator.REVERSE
                }
                animacionLinea.start()

                // Repetir este Runnable cada 8 segundos
                handlerLinea.postDelayed(this, 8000)
            }
        }

        val handlerBingo = android.os.Handler(Looper.getMainLooper())
        val runnableBingo = object : Runnable {
            override fun run() {
                // Crear y ejecutar la animación para el botón de bingo
                val animacionBingo = ObjectAnimator.ofPropertyValuesHolder(
                    botonBingo,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f)
                ).apply {
                    duration = 500 // duración en milisegundos
                    repeatCount = 1
                    repeatMode = ObjectAnimator.REVERSE
                }
                animacionBingo.start()

                // Repetir este Runnable cada 8 segundos
                handlerBingo.postDelayed(this, 8000)
            }
        }

        // Iniciar los Runnables
        handlerLinea.post(runnableLinea)
        handlerBingo.post(runnableBingo)
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
            layoutParams.setMargins(4,4,4,4)
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
        arrayBombo[numero-1] = numero
        val textView = findViewById<textView>(numero)
        textView.text = numero.toString()
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        numeroSalida.text = numero.toString()
        if(!arrayBombo.contains(0)){
          showAlertBingo(getString(R.string.bombo_completo))

        }


    }
    private fun showAlertBingo(mensage: String){
        val dialogo = MiDialogoPersonalizado()

            val args = Bundle()
            args.putString("mensage", mensage)
            dialogo.arguments = args
            dialogo.show(supportFragmentManager, "MiDialogoPersonalizado")
        }
    private fun showAlertLinia(){
        val dialogo = MiDialogoPersonalizadoLinia()
        dialogo.show(supportFragmentManager,"MiDialogoPersonalizadoLinia")
    }

    private fun premios(cartones: Int, precio: Double,porcentaje: Int){
        val botonLinea = findViewById<Button>(R.id.Línia)
        val botonBingo = findViewById<Button>(R.id.bingo)
        val premioTotal = cartones * precio
        var premioBingo = 0.0
        var premioLinia  = 0.0
        when(porcentaje) {
            0-> {premioLinia = premioTotal * 0.1
                premioBingo = premioTotal * 0.9}
            1-> {premioLinia = premioTotal * 0.2
                premioBingo = premioTotal * 0.8}
            2-> {premioLinia = premioTotal * 0.3
                premioBingo = premioTotal * 0.7}
            3-> {premioLinia = premioTotal * 0.4
                premioBingo = premioTotal * 0.6}
        }

        botonLinea.text = "${getString(R.string.linia)}\n${String.format("%.1f", premioLinia)}"
        botonBingo.text = "${getString(R.string.bingo)}\n${String.format("%.1f", premioBingo)}"

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

    }

    }
