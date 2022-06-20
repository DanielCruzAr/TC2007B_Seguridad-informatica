package com.itesm.esenciapatrimonio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itesm.esenciapatrimonio.transactions.GoToRestoredSite
import com.itesm.esenciapatrimonio.transactions.TransactionData
import com.itesm.esenciapatrimonio.ui.AdminAZResultsFragment
import com.itesm.esenciapatrimonio.ui.AdminRestoredSiteFragment

/**
 * Clase que crea el RecyclerView para los resultados de A a la Z en modo administrador
 * @author e-corp
 * @param sortedRestoredSite
 */
class AdminAZResultAdapter(sortedRestoredSite: List<SRestoreSite>,  fragment: AdminAZResultsFragment) : RecyclerView.Adapter<AdminAZResultAdapter.ViewHolder>() {

    private val azFragment = fragment
    private val listRestoredSite = sortedRestoredSite

    /**
     * Describe el item view y los metadatos que contienen las variables en el RecyclerView.
     */
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titulo : TextView
        var descripcion : TextView
        var boton : Button

        init {
            titulo = itemView.findViewById(R.id.tituloLugar)
            descripcion = itemView.findViewById(R.id.descripcionLugar)
            boton = itemView.findViewById(R.id.button_consult)
        }
    }

    /**
     * Devuelve una vista de un elemento. Usamos inflate() para crear una vista a partir del layout XML
     * @return ViewGroup
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_az_results, parent, false)
        return ViewHolder(v)
    }

    /**
     * Personaliza un elemento de tipo ViewHolder. Desde ese ViewHolder el sistema se encarga
     * de crear la vista que se pondrá en cada elemento del RecyclerView y al final con el
     * getItemCount se identificará el número de elementos que se van a crear en el Recycler.
     * @param holder
     */
    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.titulo.text = listRestoredSite[i].site_name
        holder.descripcion.text = listRestoredSite[i].information
        holder.boton.setOnClickListener{
            //llevar a la view del sitio restaurado correspondiente
            //Sobreescribimos el objeto TransactionData con el sitio restaurado seleccionado
            TransactionData.restoredSite = mutableListOf(listRestoredSite[i])
            //Hacemos la transaccion de fragmentos con la clase GoToRestoredSite
            GoToRestoredSite(azFragment, AdminRestoredSiteFragment()).makeTransaction()
        }
    }

    /**
     * Regresa el numero de elementos para crear cada item del RecyclerView
     * @return int
     */
    override fun getItemCount(): Int {
        return listRestoredSite.size
    }
}