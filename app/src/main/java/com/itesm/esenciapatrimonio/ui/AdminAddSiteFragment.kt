package com.itesm.esenciapatrimonio.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.ParseApp
import com.itesm.esenciapatrimonio.R
import com.itesm.esenciapatrimonio.SRestoreSite
import com.itesm.esenciapatrimonio.databinding.FragmentAdminAddSiteBinding

/**
 * Fragmento que despiega la vista para añadir un sitio a la base de datos
 * dentro de este fragmento está toda la lógica para realizar el proceso.
 * @author e-corp
 */

class AdminAddSiteFragment: Fragment() {
    private var _binding: FragmentAdminAddSiteBinding? = null

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

        _binding = FragmentAdminAddSiteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding!!.botonAgregar.setOnClickListener{
            if(_binding!!.nombreSitio.text.toString() == ""){
                Toast.makeText(context, "Te falta poner el nombre del sitio", Toast.LENGTH_SHORT).show()
            }
            else if(_binding!!.description.text.toString() == ""){
                Toast.makeText(context, "Te falta poner la descripción del sitio", Toast.LENGTH_SHORT).show()
            }
            else if(_binding!!.direccionSitio.text.toString() == ""){
                Toast.makeText(context, "Te falta poner la direccion del sitio", Toast.LENGTH_SHORT).show()
            }
            else if(_binding!!.coordenadasXY.text.toString() == ""){
                Toast.makeText(context, "Te faltan las coordenadas del sitio", Toast.LENGTH_SHORT).show()
            }
            else if(_binding!!.anioConstruccion.text.toString() == ""){
                Toast.makeText(context, "Te falta poner el año de construcción", Toast.LENGTH_SHORT).show()
            }
            else if(_binding!!.anioRestauracion.text.toString() == ""){
                Toast.makeText(context, "Te falta poner el año de restauración", Toast.LENGTH_SHORT).show()
            }
            else{
                val str = _binding!!.coordenadasXY.text.toString()
                val list: List<String> = str.split(",").toList()
                if(list.size < 2 ){
                    Toast.makeText(context, "Falta una coordenada o hay algo mal", Toast.LENGTH_SHORT).show()
                }
                else if (list[0].toDouble() < -90.0 || list[0].toDouble() > 90.0) {
                    Toast.makeText(context, "Coordenada x fuera de rango", Toast.LENGTH_SHORT)
                        .show()
                } else if (list[1].toDouble() < -180.0 || list[1].toDouble() > 180.0) {
                    Toast.makeText(context, "Coordenada y fuera de rango", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    val oNewSite = SRestoreSite(site_name = _binding!!.nombreSitio.text.toString(),
                        information = _binding!!.description.text.toString(),
                        est_year = _binding!!.anioConstruccion.text.toString().toInt(),
                        restore_year = _binding!!.anioRestauracion.text.toString().toInt(),
                        address = _binding!!.direccionSitio.text.toString(),
                        coordinate_x = list[0].toDouble(),
                        coordinate_y = list[1].toDouble())

                    //val oParse = ParseApp()
                    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                    if (activeNetwork != null && activeNetwork.isConnected) {
                        //Si el dispositivo esta conectado carga los datos de parse en el mapa
                        ParseApp.addRestoreSite(oNewSite, {listSite-> })
                        Toast.makeText(activity,"Sitio agregado exitosamente",Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(context,"Error de conexión, Sitio no agregado", Toast.LENGTH_SHORT).show()
                    }

                        // For debug:
                        //Toast.makeText(context, "Salida de coordenadas "+list[0]+" "+list[1], Toast.LENGTH_SHORT).show()
                    //Verificar que el dispositivo este conectado a internet o utilizando datos
                        val fragmentManager = fragmentManager
                        val fragmentTransaction = fragmentManager?.beginTransaction()

                        val fragment = AdminMainFragment()

                        fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
                        fragmentTransaction?.commit()
                    }
                }
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