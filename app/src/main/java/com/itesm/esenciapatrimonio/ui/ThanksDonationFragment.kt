package com.itesm.esenciapatrimonio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.databinding.FragmentDonacionGraciasBinding

/**
 * @author e-corp
 *
 * Fragmento donde se le da un agradecimiento al usuario por donar.
 */

class ThanksDonationFragment: Fragment() {

    private var _binding: FragmentDonacionGraciasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Método onCreate que infla la vista en la interfaz establecida por el fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDonacionGraciasBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.continuarBoton.setOnClickListener{

        }

        return root
    }

    interface IOnBackPressed {
        fun onBackPressed(): Boolean
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