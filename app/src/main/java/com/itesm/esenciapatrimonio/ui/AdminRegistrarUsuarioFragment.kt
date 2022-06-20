package com.itesm.esenciapatrimonio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.databinding.FragmentRegisterUserBinding

/**
 * Fragmento que que despliega la vista para registrar a un
 * usuario teniendo acceso a la parte de administración.
 *
 * Falta enviar los datos a la base de datos para que pueda
 * ser totalmente funcional, lo único que tiene son verifi-
 * caciones en los campos de texto.
 *
 * @author e-corp
 */
class AdminRegistrarUsuarioFragment: Fragment() {

    private var _binding: FragmentRegisterUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    /**
     * Infla la view de acuerdo a lo que se tiene que renderizar con la lógica de lo que debería
     * de suceder dentro del fragmento una vez que se inicia dentro de la actividad.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.botonLogin.setOnClickListener{
            val username:String = binding.Usuario.text.toString()
            val password:String = binding.contrasena.text.toString()
            val confirm:String = binding.contrasenaConfirm.text.toString()

            registerUser(username,password, confirm)
        }

        return root
    }


    /**
     * Sirve para tomar los datos de registro y verificarlos de manera segura para luego
     * ser enviados a la base de datos sin algún problema.
     */
    private fun registerUser(username: String, password: String, confirm: String) {

        if (username == "" || username == " "){
            Toast.makeText(activity,"No pusiste un usuario", Toast.LENGTH_SHORT).show()
        }
        else if (password == "" || password == " "){
            Toast.makeText(activity,"No pusiste una contraseña", Toast.LENGTH_SHORT).show()
        }
        else if (confirm == "" || confirm == " "){
            Toast.makeText(activity,"No pusiste una confirmación de contraseña", Toast.LENGTH_SHORT).show()
        }
        else{
            if (password == confirm){
                // TODO: Parse verify username and password
                Toast.makeText(activity,"Registro exitoso", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity,"Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * Es la llamada para limpiar lo que hay en el fragmento antes de que sea destruído
     * puede ser llamado automáticamente por el sistema cuando ya esto no está en uso
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}