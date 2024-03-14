package vilo.dev

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton

class MiDialogoPersonalizadoLinia: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialogo_personalizado, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setGravity(Gravity.TOP) // Esto hará que el diálogo se muestre en la parte superior de la pantalla
            val textView = view.findViewById<TextView>(R.id.mensaje)
            textView.text = getString(R.string.cantar_linia) // Utiliza el mensaje aquí
            val textView2 = view.findViewById<TextView>(R.id.tittle)
            textView2.text = getString(R.string.linia_correcta)
            val botonAceptar = view.findViewById<MaterialButton>(R.id.aceptar)
            botonAceptar.text = getString(R.string.si)
            botonAceptar.setOnClickListener {
                // Aquí puedes manejar el clic en el botón "Aceptar"
                dialog.dismiss()
            }

            val botonCancelar = view.findViewById<MaterialButton>(R.id.cancelar)
            botonCancelar.text = getString(R.string.no)
            botonCancelar.setOnClickListener {
                // Aquí puedes manejar el clic en el botón "Cancelar"
                dialog.dismiss()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}