package com.itesm.esenciapatrimonio.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itesm.esenciapatrimonio.*
import com.itesm.esenciapatrimonio.databinding.FragmentGaleriaBinding
import com.itesm.esenciapatrimonio.transactions.TransactionData

/**
 * @author e-corp
 * Fragmento donde el usuario puede ver la galeria de imagenes del sitio restaurado.
 */
class GalleryFragment : Fragment() {

    private var _binding: FragmentGaleriaBinding? = null

    // Esta propiedad sólo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    // Array de sitio restaurado desde la base de datos.
    lateinit var site: SRestoreSite
    lateinit var listCompare:MutableList<SComparePicture>

    /**
     * Método onCreate que infla la vista en la interfaz establecida por el fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        site = TransactionData.restoredSite[0]

        _binding = FragmentGaleriaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var recycla = _binding!!.recyclerGaleria

        var layoutManager: RecyclerView.LayoutManager? = null

        layoutManager = LinearLayoutManager(activity)
        //layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycla.layoutManager = layoutManager

        // Compara las imagenes de la base de datos
        ParseApp.getAllComparePictureBySite(site.site_name){listComp->
            listCompare = listComp

            TransactionData.listCompare = listCompare

            GalleryAdapter.initData()
            val adapter = GalleryAdapter()
            recycla.adapter = adapter

            // Función que utiliza el fragmento de FullScreenImageFragment
            // para mostrar la imagen del sitio restaurado.
            adapter.setListener(object : GalleryAdapter.GalleryAdapterClickEvents {
                override fun onClick(url: String) {

                    val fragmentManager = fragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    val fragment = FullScreenImageFragment(url)
                    fragmentTransaction?.replace(R.id.nav_host_fragment_content_main, fragment)
                    fragmentTransaction?.addToBackStack(null)
                    fragmentTransaction?.commit()
                }
            })
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
