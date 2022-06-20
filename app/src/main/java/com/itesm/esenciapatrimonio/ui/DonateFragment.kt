package com.itesm.esenciapatrimonio.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.R
import com.itesm.esenciapatrimonio.databinding.FragmentDonacionBinding

/**
 * @author e-corp
 *
 * Fragmento donde el usuario puede poner la cantidad que desea donar
 * y al picarle al botón de donar lo redirecciona a la página de PayPal.
 */

class DonateFragment: Fragment() {
    private var _binding: FragmentDonacionBinding? = null
    private val binding get() = _binding!!

    /**
     * Método onCreate que infla la vista en la interfaz establecida por el fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDonacionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Cantidad inicial
        var money = 5
        // Currency
        var curr = "USD"
        // La forma de Hu
        var texto = _binding!!.cantidad
        // URL
        var url = "https://paypal.me/FoxWare/"+money+curr

        texto.text = "$"+money+" "+curr

        // Botón para reducir la cantidad de la donación.
        _binding!!.botonMenos.setOnClickListener {
            if (money == 5){
                // Hacer nada
            }
            else if(money >=10){
                money -= 5
                texto.text = "$"+money+" "+curr
                url = "https://paypal.me/FoxWare/"+money+curr
            }
        }

        // Botón para aumentar la cantidad de la donación
        _binding!!.botonMas.setOnClickListener {
            money += 5
            texto.text = "$"+money+" "+curr
            url = "https://paypal.me/FoxWare/"+money+curr
        }

        // Botón que al seleccionarlo nos redirecciona a la vista de ThanksDonationFragment
        // para agradecer por la donación.
        _binding!!.botonDonar.setOnClickListener{

            //val openURL = Intent(android.content.Intent.ACTION_VIEW)
            //openURL.data = Uri.parse(url)
            //startActivity(openURL)
            Toast.makeText(context,"¡Próximamente!", Toast.LENGTH_SHORT).show()

            // Ir a la vista de thanks
            //val fragmentManager = fragmentManager
            //val fragmentTransaction = fragmentManager?.beginTransaction()

            //Thread.sleep(2_000)
            //val fragment = ThanksDonationFragment()

            //fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
            //fragmentTransaction?.commit()
        }

        return root
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