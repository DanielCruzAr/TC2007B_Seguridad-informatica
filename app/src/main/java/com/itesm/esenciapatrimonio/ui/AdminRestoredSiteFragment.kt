package com.itesm.esenciapatrimonio.ui

import android.app.AlertDialog
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
import com.itesm.esenciapatrimonio.ParseApp
import com.itesm.esenciapatrimonio.R
import com.itesm.esenciapatrimonio.databinding.FragmentAdminSitioRestauradoBinding
import com.itesm.esenciapatrimonio.transactions.TransactionData

/**
 * Fragmento que que despliega la vista para ver el sitio restaurado en modo administrador,
 * permite eliminar el sitio restaurado completamente, ver la información general y accesar
 * a la vista de galería de imágenes de administrador desde aquí.
 *
 * Estaba planeado poder editar la información como el título y descripción pero no alcanzamos
 * el código ya está hecho, sólo falta conectarlo con parse y la base de datos.
 * @author e-corp
 */
class AdminRestoredSiteFragment: Fragment() {

    private var _binding: FragmentAdminSitioRestauradoBinding? = null
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
        _binding = FragmentAdminSitioRestauradoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val site = TransactionData.restoredSite[0];

        //Log.d("Sitio restaurado", "${site.site_name}")

        _binding!!.Titulo.setText(site.site_name)

        _binding!!.descripcion.setText(site.information)

        _binding!!.Direccion.text = "Dirección: " + site.address
        _binding!!.Direccion.setTextIsSelectable(true)

        _binding!!.Coordenadas.text =
            "Coordenadas:\n\tX: " + site.coordinate_x.toString() + "\n\tY: " + site.coordinate_y.toString()
        _binding!!.Coordenadas.setTextIsSelectable(true)

        _binding!!.placeID.text = "Id del lugar: " + site.objectId
        _binding!!.placeID.setTextIsSelectable(false)
        _binding!!.placeID.isVisible = false

        _binding!!.dates.text =
            "Fechas\n\tEst year: " + site.est_year.toString() + "\n\tAño de restauración: " + site.restore_year
        _binding!!.dates.setTextIsSelectable(true)
        _binding!!.dates.isVisible = false

        _binding!!.verGaleria.setOnClickListener {

            val fragmentManager = fragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()

            val fragment = AdminGalleryFragment()

            fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()

        }

        _binding!!.guardarNameAndDesc.setOnClickListener{
            //TODO: Here goes the code to save the Name and Description of the place if edited
        }

        _binding!!.eliminarSitio.setOnClickListener{
            val builder = AlertDialog.Builder(it.context)
            builder.setMessage("¿Estás seguro de eliminar el sitio?")
                .setCancelable(false)
                .setPositiveButton("SÍ") { dialog, id ->
                    // Aquí va el código para eliminar el sitio
                    ParseApp.deleteRestoreSite(TransactionData.restoredSite[0].site_name){siteName ->

                    }

                    Toast.makeText(context, "Sitio eliminado", Toast.LENGTH_SHORT).show()

                    Thread.sleep(1_500)
                    val fragmentManager = fragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    val fragment = AdminAZResultsFragment()

                    fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
                    fragmentTransaction?.commit()
                }
                .setNegativeButton("NO, NO ELIMINAR") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        //Verificar que el dispositivo este conectado a internet o utilizando datos
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected) {
        }
        else{
            Toast.makeText(context,"Error de conexión", Toast.LENGTH_SHORT).show()
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