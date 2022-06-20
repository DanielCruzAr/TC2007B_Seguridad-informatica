package com.itesm.esenciapatrimonio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itesm.esenciapatrimonio.transactions.TransactionData
import com.squareup.picasso.Picasso

/**
 * Clase que crea el RecyclerView para mostrar la galería
 * @author e-corp
 * @param sortedRestoredSite
 */
class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.ViewHolder>(){

    private var listener: GalleryAdapterClickEvents? = null

    fun setListener(listener: GalleryAdapterClickEvents){
        this.listener = listener
    }

    interface GalleryAdapterClickEvents {
        fun onClick(url: String)
    }

    /**
     * Crea un objeto común a todas las instancias de la clase y contiene los arrays que se necesitan para
     * crear los objetos del recyclerview a partir de las imágenes antiguas y nuevas
     */
    companion object {
        private lateinit var categorias: Array<String>// = arrayOf("Card 1")

        private lateinit var imagenes_antiguas: Array<String> /*= arrayOf(
        "https://images.unsplash.com/photo-1480074568708-e7b720bb3f09?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2074&q=80"
    )
    */

        private lateinit var imagenes_actuales: Array<String>/* = arrayOf(
        "https://images.unsplash.com/photo-1564013799919-ab600027ffc6?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2070&q=80"
    )*/

        /**
         * Inicualiza los datos de acuerdo con lo obtenido a través de parse
         */
        fun initData()
        {
            val listCategorias:MutableList<String> = mutableListOf()
            val listImagenes_antiguas:MutableList<String> = mutableListOf()
            val listImagenes_actuales:MutableList<String> = mutableListOf()

            for(comp in TransactionData.listCompare){
                listCategorias.add(comp.description)
                listImagenes_actuales.add(comp.newPic_id)
                listImagenes_antiguas.add(comp.oldPic_id)
            }

            categorias = listCategorias.toTypedArray()
            imagenes_actuales = listImagenes_actuales.toTypedArray()
            imagenes_antiguas = listImagenes_antiguas.toTypedArray()
        }
    }

    private var listCompare :MutableList<SComparePicture> ?= null

    /**
     * Describe el item view y los metadatos que contienen las variables en el RecyclerView.
     */
    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        var imagen_antigua : ImageView
        var imagen_actual : ImageView
        var categoria : TextView


        init{
            imagen_antigua = itemView.findViewById(R.id.imagenAntigua)
            imagen_actual = itemView.findViewById(R.id.imagenActual)
            categoria = itemView.findViewById(R.id.categoriaCarta)
        }

    }

    /**
     * Devuelve una vista de un elemento. Usamos inflate() para crear una vista a partir del layout XML
     * @return ViewGroup
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_gallery_block, parent, false)

        listCompare = null
        ParseApp.getAllComparePictureBySite(TransactionData.restoredSite[0].site_name){listComp ->
            listCompare = listComp
        }

        return ViewHolder(v)
    }

    /**
     * Personaliza un elemento de tipo ViewHolder. Desde ese ViewHolder el sistema se encarga
     * de crear la vista que se pondrá en cada elemento del RecyclerView y al final con el
     * getItemCount se identificará el número de elementos que se van a crear en el Recycler.
     * @param holder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoria.text = categorias[position]

        Picasso.get().load(imagenes_antiguas[position]).into(holder.imagen_antigua)
        Picasso.get().load(imagenes_actuales[position]).into(holder.imagen_actual)

        /*
        Picasso.get().load(ParseApp.listImageTest[0]).into(holder.imagen_antigua)
        Picasso.get().load(ParseApp.listImageTest[1]).into(holder.imagen_actual)
         */

        /*Wait until the listComp is fetched by Parse*/
        /*
        while(listCompare == null){;}
        if(listCompare != null) {
            if (listCompare?.size != 0) {
                Picasso.get().load(listCompare!![position % listCompare!!.size].oldPic_id)
                    .into(holder.imagen_antigua)
                Picasso.get().load(listCompare!![position % listCompare!!.size].newPic_id)
                    .into(holder.imagen_actual)
            }
        }
*/
        holder.imagen_antigua.setOnClickListener{
            listener?.onClick(imagenes_antiguas[position])
        }

        holder.imagen_actual.setOnClickListener{
            listener?.onClick(imagenes_actuales[position])
        }
    }

    /**
     * Regresa el numero de elementos para crear cada item del RecyclerView
     * @return int
     */
    override fun getItemCount(): Int {
        return categorias.size
    }

}
