package vilo.dev

import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.updateLayoutParams
import com.google.android.ump.*

class MainActivity :  BaseActivity() {
    private lateinit var consentInformation: ConsentInformation
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.menu)
        // Create a ConsentRequestParameters object.
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
                    // Consent gathering failed.
                    Log.w(
                        TAG, String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                }
            },
            { requestConsentError ->
                // Consent gathering failed.
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
            val marginBottom = resources.getDimensionPixelSize(R.dimen.margin_bottom)
            botonInicio.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = marginBottom
            }
        }
    }
}