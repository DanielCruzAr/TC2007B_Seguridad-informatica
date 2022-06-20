package com.itesm.esenciapatrimonio.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.R
import com.itesm.esenciapatrimonio.transactions.TransactionData
import com.itesm.esenciapatrimonio.databinding.FragmentSitioRestauradoBinding

/**
 * @author e-corp
 *
 * Fragmento donde muestra la información de un sitio restaurado
 * y al picar el botón te manda a la galería de fotos del sitio restaurado.
 */

class RestoredSiteFragment: Fragment() {
    private var _binding: FragmentSitioRestauradoBinding? = null
    private val binding get() = _binding!!

    /**
     * Método onCreate que infla la vista en la interfaz establecida por el fragmento
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSitioRestauradoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val site = TransactionData.restoredSite[0];

        // Muestra el titulo del sitio restaurado
        _binding!!.Titulo.text = site.site_name

        // Muestra la descripción del sitio restaurado
        _binding!!.descripcion.text = site.information
        _binding!!.placeID.setTextIsSelectable(true)

        // Muestra la dirección del sitio restaurado
        _binding!!.Direccion.text = "Dirección: "+site.address
        _binding!!.Direccion.setTextIsSelectable(true)

        // Muestra las coordenadas del sitio restaurado
        _binding!!.Coordenadas.text = "Coordenadas:\n\tX: "+site.coordinate_x.toString()+"\n\tY: "+site.coordinate_y.toString()
        _binding!!.Coordenadas.setTextIsSelectable(true)

        // Muestra el Id del sitio restaurado
        _binding!!.placeID.text = "Id del lugar: "+site.objectId
        _binding!!.placeID.setTextIsSelectable(false)
        _binding!!.placeID.isVisible = false

        // Muestra las fechas en años del sitio restaurado
        _binding!!.dates.text = "Fechas\n\tEst year: "+site.est_year.toString()+"\n\tAño de restauración: "+site.restore_year
        _binding!!.dates.setTextIsSelectable(true)
        _binding!!.dates.isVisible = false

        // Botón que al seleccionarlo nos muestra más detalles del sitio restaurado
        _binding!!.verMasDetalles.setOnClickListener {
            _binding!!.verMasDetalles.isVisible = false
            _binding!!.placeID.isVisible = false
            _binding!!.dates.isVisible = true
        }

        // Botón que al seleccionarlo nos muestra más de la historia del sitio restaurado
        _binding!!.verHistoria.setOnClickListener{
            val params: ViewGroup.LayoutParams = _binding!!.descripcion.getLayoutParams() as ViewGroup.LayoutParams
            params.height = (ViewGroup.LayoutParams.WRAP_CONTENT)
            _binding!!.descripcion.setLayoutParams(params)
            _binding!!.verHistoria.isVisible = false
        }

        // Botón que al seleccionarlo nos redirecciona a la vista de Gallery Fragment
        // para ver las imagenes del sitio restaurado.
        _binding!!.verGaleria.setOnClickListener{
            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val fragment = GalleryFragment()
            fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        // Verificar que el dispositivo este conectado a internet o utilizando datos
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected) {
        }else {
            Toast.makeText(context,"Error de conexión, verifique que su dispositivo esté conectado a internet", Toast.LENGTH_SHORT).show()
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
