package vilo.dev

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.button.MaterialButton
import androidx.core.view.updateLayoutParams
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import android.widget.TextView as textView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

class Bombo : BaseActivity() {
    private var numeroCartones: Int = 0
    private var precio: Double = 0.0
    private var seleccion: Int = 0
    private var miliSeconds: Long = 0
    private var arrayBombo = IntArray(90)
    private var arrayTextViews =
        Array<String?>(90) { null }
    private var bolaSaliente: String? = null
    private var isPlaying = true
    private var mInterstitialAd: InterstitialAd? = null
    private  val tag = "Bombo"
    private lateinit var consentInformation: ConsentInformation
    private var isConsentObtained = AtomicBoolean(false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bombo)
        MobileAds.initialize(this) {}
        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val params = ConsentRequestParameters.Builder().build()
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {

                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.OBTAINED) {
                    isConsentObtained.set(true)
                    loadAds()
                }
            },
            { requestConsentError ->

                Log.w(TAG, String.format("%s: %s", requestConsentError.errorCode, requestConsentError.message))
            }
        )

        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        val botonNuevaBola = findViewById<MaterialButton>(R.id.botonNuevaBola)
        val botonPlayPause = findViewById<MaterialButton>(R.id.play)
        val botonAutomaticoManual = findViewById<MaterialButton>(R.id.manual_automatico)

        if (savedInstanceState != null) {
            numeroCartones = savedInstanceState.getInt("numeroCartones")
            precio = savedInstanceState.getDouble("precio")
            seleccion = savedInstanceState.getInt("seleccion")
            miliSeconds = savedInstanceState.getLong("miliSeconds")
            arrayBombo = savedInstanceState.getIntArray("arrayBombo") ?: IntArray(90)
            arrayTextViews = savedInstanceState.getStringArray("arrayTextViews") ?: Array(90) { null }
            bolaSaliente = savedInstanceState.getString("bolaSaliente")
            numeroSalida.visibility = savedInstanceState.getInt("bolaSalienteVisibility")
            botonNuevaBola.visibility = savedInstanceState.getInt("botonNuevaBolaVisibility")
            botonPlayPause.visibility = savedInstanceState.getInt("botonPlayPauseVisibility")
            botonAutomaticoManual.text = savedInstanceState.getString("modoActual") ?: getString(R.string.automatico)
        }

        numeroCartones = intent.getIntExtra("numeroCartones", 0)
        precio = intent.getDoubleExtra("precio", 0.0)
        seleccion = intent.getIntExtra("seleccion", 0)
        miliSeconds = intent.getLongExtra("miliSeconds", 3500)
        enableEdgeToEdge()
        premios(numeroCartones, precio, seleccion)
        noventaContenedores()
        updateTextViewsBackground(arrayTextViews,bolaSaliente)


        botonNuevaBola.setOnClickListener {


            if (arrayBombo.contains(0)) {
                nunmeroAleatorio()
            }
            else{
                botonNuevaBola.isEnabled = false
                botonNuevaBola.text = getString(R.string.bombo_lleno)
            }


        }

        botonPlayPause.setOnClickListener {
            if (isPlaying && arrayBombo.contains(0)) {
                botonPlayPause.background =
                    AppCompatResources.getDrawable(this, R.drawable.playerpause)
                handler.postDelayed(runnable, miliSeconds)
                isPlaying = false

            } else {
                botonPlayPause.background =
                    AppCompatResources.getDrawable(this, R.drawable.playerplay)
                handler.removeCallbacks(runnable)
                isPlaying = true
            }
        }
        numeroSalida.text = bolaSaliente ?: ""
        val botonNuevaPartida = findViewById<MaterialButton>(R.id.boton_nueva_partida)
        botonNuevaPartida.setOnClickListener {

            if (isConsentObtained.get() && mInterstitialAd != null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {

                        Log.d(tag, "Ad dismissed fullscreen content.")
                        mInterstitialAd = null
                        val intent = Intent(this@Bombo, Configuration::class.java)
                        startActivity(intent)
                    }

                }
                mInterstitialAd?.show(this@Bombo)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet or the user has not given consent.")
                val intent = Intent(this, Configuration::class.java)
                startActivity(intent)
            }
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
                handler.removeCallbacks(runnable)
                botonNuevaBola.visibility = View.VISIBLE
                botonPlayPause.background = AppCompatResources.getDrawable(this, R.drawable.playerplay)
                botonPlayPause.visibility = View.GONE
            }
        }
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {

                Log.d(tag, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {

                Log.d(tag, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }



            override fun onAdImpression() {

                Log.d(tag, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {

                Log.d(tag, "Ad showed fullscreen content.")
            }
        }

        if(orientacion==2 && medidasPantalla<3) {
            val margenSup = resources.getDimensionPixelSize(R.dimen.top_margin)
            numeroSalida.updateLayoutParams<ViewGroup.MarginLayoutParams>{
                topMargin = margenSup
                width = resources.getDimensionPixelSize(R.dimen.dimBola)
                height = resources.getDimensionPixelSize(R.dimen.dimBola)

            }
            numeroSalida.textSize = 50f
            val lineaGuiaHorizontal = findViewById<Guideline>(R.id.guia_Horizontal)
            lineaGuiaHorizontal.setGuidelinePercent(0.45f)
            val gridLayout = findViewById<GridLayout>(R.id.bomboGrid)
            gridLayout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = resources.getDimensionPixelSize(R.dimen.grid_top_margin)
                bottomMargin = margenSup
            }
            for(i in 1..90){
                val textView = findViewById<TextView>(i)
                textView.textSize = 15f
            }
            val bomboLayout1 = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.bomboLayout)
            bomboLayout1.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = resources.getDimensionPixelSize(R.dimen.pantallas_media)
                rightMargin =  resources.getDimensionPixelSize(R.dimen.pantallas_media)
            }

        }
        if(orientacion == 1){
            val lineaGuiaHorizontal = findViewById<Guideline>(R.id.guia_Horizontal)
            lineaGuiaHorizontal.setGuidelinePercent(0.37f)
            numeroSalida.updateLayoutParams<ViewGroup.MarginLayoutParams>{
                topMargin = resources.getDimensionPixelSize(R.dimen.top_vertical_bola)
                if(medidasPantalla>3){
                    width = resources.getDimensionPixelSize(R.dimen.dim_bola_grande)
                    height = resources.getDimensionPixelSize(R.dimen.dim_bola_grande)
                    numeroSalida.textSize = 120f
                }
            }

        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                if (isConsentObtained.get() && mInterstitialAd != null) {
                    mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {

                            Log.d(tag, "Ad dismissed fullscreen content.")
                            mInterstitialAd = null
                            if (isEnabled) {
                                isEnabled = false
                                val intent = Intent(this@Bombo, Configuration::class.java)
                                startActivity(intent)
                            }
                        }

                    }
                    mInterstitialAd?.show(this@Bombo)
                } else {
                    Log.d(tag, "The interstitial ad wasn't ready yet or the user has not given consent.")
                    if (isEnabled) {
                        isEnabled = false
                        val intent = Intent(this@Bombo, Configuration::class.java)
                        startActivity(intent)
                    }
                }
            }
        })

    }

    private fun updateTextViewsBackground(arrayTextViews: Array<String?>, bolaSaliente: String?) {
        for (i in 1..90) {
            val textView = findViewById<textView>(i)
            textView.text = arrayTextViews[i - 1] ?: ""
            if (textView.text.isNotEmpty()) {
                textView.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.rounded_blue, theme)
            }
        }
        if (bolaSaliente?.isNotEmpty() == true) {
            val id = bolaSaliente.toInt()
            val textView = findViewById<textView>(id)
            if (textView != null) {

                textView.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.rounded_purple, theme)

            }

        }
    }

    private fun noventaContenedores() {
        for (i in 1..90) {
            val gridLayout = findViewById<GridLayout>(R.id.bomboGrid)
            val textView = textView(this)
            val layoutParams = GridLayout.LayoutParams()
            val botonNuevaBola = findViewById<MaterialButton>(R.id.botonNuevaBola)
            textView.gravity = Gravity.CENTER
            textView.id = i
            textView.textSize = 20f
            layoutParams.width = 0
            layoutParams.setMargins(4, 4, 4, 4)
            layoutParams.height = 0
            layoutParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            textView.layoutParams = layoutParams
            textView.setTextColor(Color.WHITE)
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            if (botonNuevaBola.currentTextColor == Color.WHITE) {
                textView.background = AppCompatResources.getDrawable(this, R.drawable.rounded_grey)
            } else {
                textView.background = AppCompatResources.getDrawable(this, R.drawable.rounded_carbon)
            }
            gridLayout.addView(textView)

        }
    }


    private fun nunmeroAleatorio() {
        var numero: Int = (1..90).random()
        while (arrayBombo.contains(numero)) {
            numero = (1..90).random()
        }
        arrayBombo[numero - 1] = numero
        arrayTextViews[numero - 1] = numero.toString() // Actualizar arrayTextViews
        val textView = findViewById<textView>(numero)
        textView.text = numero.toString()
        textView.background = AppCompatResources.getDrawable(this, R.drawable.rounded_blue)
        val numeroSalida = findViewById<textView>(R.id.bola_saliente)
        numeroSalida.text = numero.toString()
        updateTextViewsBackground(arrayTextViews, numeroSalida.text.toString())

    }
    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(tag, adError.toString() )
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(tag, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
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
        outState.putLong("miliSeconds",miliSeconds)
        for (i in 1..90) {
            val textView = findViewById<TextView>(i)
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
        handler.removeCallbacks(runnable)
    }


    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if(arrayBombo.contains(0)) {
                nunmeroAleatorio()
                handler.postDelayed(this, miliSeconds)
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
