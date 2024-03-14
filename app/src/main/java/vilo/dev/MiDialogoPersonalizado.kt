package vilo.dev

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MiDialogoPersonalizado : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mensage = arguments?.getString("mensage")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialogo_personalizado, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setGravity(Gravity.TOP) // Esto hará que el diálogo se muestre en la parte superior de la pantalla
            val textView = view.findViewById<TextView>(R.id.mensaje)
            textView.text = mensage // Utiliza el mensaje aquí
            val botonAceptar = view.findViewById<MaterialButton>(R.id.aceptar)
            botonAceptar.setOnClickListener {
                // Aquí puedes manejar el clic en el botón "Aceptar"
                val intent = Intent(context, Configuration::class.java)
                context?.startActivity(intent)
                dialog.dismiss()
            }

            val botonCancelar = view.findViewById<MaterialButton>(R.id.cancelar)
            botonCancelar.setOnClickListener {
                // Aquí puedes manejar el clic en el botón "Cancelar"
                dialog.dismiss()
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}