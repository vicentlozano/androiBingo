package vilo.dev

import android.os.Bundle
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.updateLayoutParams


class MainActivity :  BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        setContentView(R.layout.menu)

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








