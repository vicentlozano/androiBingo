import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {
    protected var medidasPantalla: Int = 0
    protected var orientacion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = resources.configuration
        medidasPantalla= configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        orientacion = configuration.orientation
    }
}