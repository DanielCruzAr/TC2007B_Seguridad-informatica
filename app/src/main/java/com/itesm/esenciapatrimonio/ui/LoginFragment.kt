package com.itesm.esenciapatrimonio.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.R
import com.itesm.esenciapatrimonio.databinding.FragmentLoginBinding

/**
 * @author e-corp
 *
 * Fragmento donde el administrador podra hacer login en la aplicación
 * para contar con los beneficios de admin.
 */

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // Array del usuario y la contraseña.
    private lateinit var usuario: EditText
    private lateinit var contrasena: EditText

    companion object {
        var isLogin: Boolean = false
    }

    var failedLoginAttempt = 0

    external fun getPassword(): String
    external fun getUsername(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        System.loadLibrary("keys")
    }

    /**
     * Método onCreate que infla la vista en la interfaz establecida por el fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    /**
     * Infla la view de acuerdo a lo que se tiene que renderizar con la lógica de lo que debería
     * de suceder dentro del fragmento una vez que se inicia dentro de la actividad.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Esta parte funciona sólo para comprobar el estado de inicio de sesión.
        if (!isLogin){
            binding.botonLogin.setOnClickListener {
                val cm = it.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

                if (activeNetwork != null && activeNetwork.isConnected){

                    val user: String = binding.Usuario.text.toString()
                    val password: String = binding.contrasena.text.toString()

                    login(user, password)

                }
                else{
                    Toast.makeText(it.context,"Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            // Si el login fue exitoso redirecciona a la vista de AdminMainFragment.
            Toast.makeText(activity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val fragment = AdminMainFragment()

            fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
            fragmentTransaction?.commit()
        }
    }

    /**
     * Recibe un dos strings y comprueba los datos de manera segura, si el login es exitoso
     * te redirige a la vista de administración
     */
    fun login(username: String, password: String) {

        if (username == "" || username == " ") {
            Toast.makeText(activity, "No pusiste un usuario", Toast.LENGTH_SHORT).show()
        } else if (password == "" || password == " ") {
            Toast.makeText(activity, "No pusiste una contraseña", Toast.LENGTH_SHORT).show()
        } else {

            if (username.trim().equals(
                    getUsername(),
                    false
                ) && password.trim().equals(
                    getPassword(),
                    false
                )
            ) {
                // Variable global para no iniciar sesión de nuevo mientras la aplicación permanece abierta.
                isLogin = true

                //Ir al espacio de administración.
                Toast.makeText(activity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val fragmentManager = fragmentManager
                val fragmentTransaction = fragmentManager?.beginTransaction()

                val fragment = AdminMainFragment()

                fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
                fragmentTransaction?.addToBackStack(null)
                fragmentTransaction?.commit()
            } else {
                failedLoginAttempt++
                checkFailedLoginAttemptCase()
                Toast.makeText(activity, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    /**
     * Revisa las veces en las que te equivocaste, si te equivocas cinco veces, aparecerá un
     * contador de 5 minutos, sirve para evitar ataques de fuerza bruta.
     */
    private fun checkFailedLoginAttemptCase() {
        if (failedLoginAttempt >= 5) {
            failedLoginAttempt = 0

            binding.botonLogin.visibility = View.GONE
            binding.tvTimer.visibility = View.VISIBLE

            val timer = object : CountDownTimer(1000L * 60 * 5, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    var diff = millisUntilFinished
                    val secondsInMilli: Long = 1000
                    val minutesInMilli = secondsInMilli * 60

                    val elapsedMinutes = diff / minutesInMilli
                    diff %= minutesInMilli

                    val elapsedSeconds = diff / secondsInMilli

                    binding.tvTimer.text = "$elapsedMinutes:$elapsedSeconds"

                }

                override fun onFinish() {
                    binding.botonLogin.visibility = View.VISIBLE
                    binding.tvTimer.visibility = View.GONE
                }
            }
            timer.start()

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