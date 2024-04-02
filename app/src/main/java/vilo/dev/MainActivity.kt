package vilo.dev

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.google.android.ump.*

class MainActivity :  BaseActivity() {
    private lateinit var consentInformation: ConsentInformation
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.menu)

        val params = ConsentRequestParameters
            .Builder()
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainActivity
                ) { loadAndShowError ->

                    Log.w(
                        TAG, String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )


                }
            },
            { requestConsentError ->

                Log.w(TAG, String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message))
            }
        )

        val botonInicio = findViewById<Button>(R.id.botonInicio)
        botonInicio.setOnClickListener {
            val intent = Intent(this, Configuration::class.java)
            startActivity(intent)
        }
        if(orientacion==2 && medidasPantalla<3) {
            val marginBottom = resources.getDimensionPixelSize(R.dimen.boton_principio1)
            botonInicio.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = marginBottom
            }
        }
        if(orientacion ==1 && medidasPantalla<3){
            val miImagen = findViewById<ImageView>(R.id.imagen_fondo)
            miImagen.setImageResource(R.drawable.imagen_bingo169)
        }

    }
}