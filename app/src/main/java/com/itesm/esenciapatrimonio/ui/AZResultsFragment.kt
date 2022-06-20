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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itesm.esenciapatrimonio.*
import com.itesm.esenciapatrimonio.databinding.FragmentAzResultsBinding

/**
 * @author e-corp
 *
 * Fragmento que ordena los sitios restaurados de la base de datos alfabéticamente
 * y los muestra como lista de manera vertical
 */

class AZResultsFragment: Fragment() {
    private var _binding: FragmentAzResultsBinding? = null
    private val binding get() = _binding!!

    // Array de sitio restaurado desde la base de datos.
    lateinit var sortedRestoredSite: List<SRestoreSite>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAzResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Verificar que el dispositivo este conectado a internet o utilizando datos.
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected) {

            // Si el dispositivo esta conectado carga los datos de parse en el buscador.
            ParseApp.getAllRestoreSite(this::getRestoredSite)
        }else {
            Toast.makeText(context,"Error de conexión, verifique que su dispositivo esté conectado a internet", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    /**
     * Funcion para obetener los datos de parse
     * @param listRestoredSite Lista de objetos tipo SRestoredSite
     */
    fun getRestoredSite(listRestoredSite:MutableList<SRestoreSite>):Unit{
        // Ordenar los datos de restoredSite por nombre
        sortedRestoredSite = listRestoredSite.sortedBy { it.site_name }
        //variable para controlar el elemento de recycler view
        val recycler = view?.findViewById<RecyclerView>(R.id.recycler_azResult)
        // Mostrar en la view cada elemento de la lista como boton
        var layoutManager: RecyclerView.LayoutManager? = null
        var adapter: RecyclerView.Adapter<AZResultAdapter.ViewHolder>? = null
        layoutManager = LinearLayoutManager(activity)
        recycler?.layoutManager = layoutManager
        //Adaptador del Recycler View
        adapter = AZResultAdapter(sortedRestoredSite, this)
        recycler?.adapter = adapter
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