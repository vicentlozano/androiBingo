package vilo.dev


import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import com.google.android.material.button.MaterialButton
import androidx.core.view.updateLayoutParams
import com.google.android.material.button.MaterialButtonToggleGroup

class Configuration : BaseActivity() {
    private var numeroCartones: Int = 0
    private var precio: Double = 0.0
    private var seleccion: Int = 0
    private var miliSeconds: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.configuration_dialog)
        if (savedInstanceState != null) {
            numeroCartones = savedInstanceState.getInt("numeroCartones")
            precio = savedInstanceState.getDouble("precio")
            seleccion = savedInstanceState.getInt("seleccion")
            miliSeconds = savedInstanceState.getLong("miliSeconds")
        }
        val contenido1: EditText = findViewById(R.id.contenido1)
        val contenido2: EditText = findViewById(R.id.contenido2)
        val toggleGroup: MaterialButtonToggleGroup = findViewById(R.id.toggleButtonGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> seleccion = 0
                    R.id.button2 -> seleccion = 1
                    R.id.button3 -> seleccion = 2
                    R.id.button4 -> seleccion = 3
                }
            }
        }
        val toggleGroup2 :MaterialButtonToggleGroup = findViewById(R.id.toggleButtonGroup2)
        toggleGroup2.addOnButtonCheckedListener{group, checkedId, isChecked ->
            if (isChecked){
                when(checkedId) {
                    R.id.button5 -> miliSeconds = 2000
                    R.id.button6 -> miliSeconds = 3000
                    R.id.button7 -> miliSeconds = 4000
                    R.id.button8 -> miliSeconds = 5000
                }
            }
        }


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
                    intent.putExtra("miliSeconds", miliSeconds)
                    startActivity(intent)
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Error: entrada no válida", Toast.LENGTH_SHORT).show()

                }
            } else {

                val intent = Intent(this, Bombo::class.java)
                intent.putExtra("numeroCartones", 0)
                intent.putExtra("precio", 0.0)
                intent.putExtra("seleccion", 0)
                intent.putExtra("miliSeconds", 3500)
                startActivity(intent)
            }
        }
        val guia1 : Guideline = findViewById(R.id.guideline_center1)
        val guia2 : Guideline = findViewById(R.id.guideline_center2)
        val tituloCartones:TextView = findViewById(R.id.titulo1)

        if(orientacion ==2 && medidasPantalla<3) {
            tituloCartones.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.pantallas_media)
            }
            continuarB.updateLayoutParams<MarginLayoutParams> {
                bottomMargin = resources.getDimensionPixelSize(R.dimen.boton_principio)
            }
            guia1.setGuidelinePercent(0.0f)
            guia2.setGuidelinePercent(0.5f)
            tituloCartones.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.pantallas_media)
            }
            val prem:TextView = findViewById(R.id.premios)
            prem.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.grid_top_margin)
            }
            val vel:TextView = findViewById(R.id.velocidad)
            vel.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.grid_top_margin)
            }
            val constraintLayout: ConstraintLayout = findViewById(R.id.constraint_confi)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(R.id.titulo2, ConstraintSet.START, R.id.guideline_center2, ConstraintSet.START, 100)
            constraintSet.connect(R.id.titulo2, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 100)
            constraintSet.connect(R.id.titulo2, ConstraintSet.TOP, R.id.titulo1, ConstraintSet.TOP, 0)
            constraintSet.connect(R.id.contenido2, ConstraintSet.START, R.id.guideline_center2, ConstraintSet.START, 100)
            constraintSet.connect(R.id.contenido2, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 100)
            constraintSet.setMargin(R.id.titulo1,ConstraintSet.START,100)
            constraintSet.setMargin(R.id.titulo1,ConstraintSet.END,100)
            constraintSet.setMargin(R.id.contenido1,ConstraintSet.START,100)
            constraintSet.setMargin(R.id.contenido1,ConstraintSet.END,100)
            constraintSet.applyTo(constraintLayout)



        }
        if(orientacion==1 && medidasPantalla>2){
            tituloCartones.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.dim_config)
            }
        }
        if(medidasPantalla<2){
            tituloCartones.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.pantallas_media)
            }
        }
        if(orientacion==1 && medidasPantalla<3){

            guia1.setGuidelinePercent(0.1f)
            guia2.setGuidelinePercent(0.9f)
            tituloCartones.updateLayoutParams<MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.dim_config2)
            }
            val porcen : MaterialButton = findViewById(R.id.button1)
            porcen.setPadding(2, 5, 2, 5)
            val porcen1 : MaterialButton = findViewById(R.id.button2)
            porcen1.setPadding(2, 5, 2, 5)
            val porcen2 : MaterialButton = findViewById(R.id.button3)
            porcen2.setPadding(2, 5, 2, 5)
            val porcen3 : MaterialButton = findViewById(R.id.button4)
            porcen3.setPadding(2, 5, 2, 5)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("numeroCartones", numeroCartones)
        outState.putDouble("precio", precio)
        outState.putInt("seleccion", seleccion)
        outState.putLong("miliSecond",miliSeconds)
    }

}