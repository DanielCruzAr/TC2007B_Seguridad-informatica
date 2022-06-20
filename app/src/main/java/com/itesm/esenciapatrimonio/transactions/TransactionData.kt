package com.itesm.esenciapatrimonio.transactions

import com.itesm.esenciapatrimonio.SComparePicture
import com.itesm.esenciapatrimonio.SRestoreSite

/**
 * @author e-corp
 * Objeto que guarda los sitios restaurados en una lista
 * para poder ser utilizados despues de las transacciones y no tener
 * que estar haciendo peticiones a la base de datos
 */

object TransactionData {

    lateinit var restoredSite: MutableList<SRestoreSite>
    lateinit var listCompare:MutableList<SComparePicture>

}