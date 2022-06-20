package com.itesm.esenciapatrimonio.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.itesm.esenciapatrimonio.ParseApp
import com.itesm.esenciapatrimonio.databinding.FragmentAdminAddPhotosBinding
import com.itesm.esenciapatrimonio.transactions.TransactionData

/**
 * Fragmento que despiega la vista para añadir fotografías en el modo administrador
 * @author e-corp
 */

class AdminAddPhotosFragment: Fragment() {
    lateinit var ImageUri: Uri

    private var _binding: FragmentAdminAddPhotosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var triggerAntigua = 0
    var triggerActual = 0

    @RequiresApi(Build.VERSION_CODES.O)

    /**
     * Infla la view de acuerdo a lo que se tiene que renderizar con la lógica de lo que debería
     * de suceder dentro del fragmento una vez que se inicia dentro de la actividad.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminAddPhotosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var code_id = 0

        _binding!!.imagenAntigua.setOnClickListener{
            code_id = 1
            pickImageGallery(code_id)
        }

        _binding!!.imagenActual.setOnClickListener{
            code_id = 2
            pickImageGallery(code_id)
        }

        _binding!!.imageUpload.setOnClickListener{
            // Revisa si entra el texto
            //Log.d(TAG, "Holaaaaaaa "+ _binding!!.tipoFoto.text.toString())
            if (triggerActual == 1 && triggerAntigua == 1 && _binding!!.tipoFoto.text.toString() != "") {
                // The following code uploads the image to the database
                //Verificar que el dispositivo este conectado a internet o utilizando datos
                val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

                if (activeNetwork != null && activeNetwork.isConnected){

                    ParseApp.addComparePicture(TransactionData.restoredSite[0].site_name, _binding!!.tipoFoto.text.toString(), imageNow, imageOld){
                            Compare ->
                    }
                    Toast.makeText(context,"Subiendo...", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(context,"Error de conexión", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(context, "Te falta poner algún dato", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    /**
     * Sirve para seleccionar una imagen de la galería
     * @param code_id
     */
    private fun pickImageGallery(code_id: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        startActivityForResult(intent, code_id)
    }

    lateinit var imageNow:Bitmap
    lateinit var imageOld:Bitmap

    /**
     * Establece la imagen seleccionada en la view y en otra variable que
     * servirá para subir ambas imágenes al servidor dependiendo del requestCode
     * @param requestCode, data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK){
            _binding!!.imagenAntigua.setImageURI(data?.data)
            triggerAntigua = 1
            imageOld = _binding!!.imagenAntigua.drawable.toBitmap()
        }
        if (requestCode == 2 && resultCode == RESULT_OK){
            _binding!!.imagenActual.setImageURI(data?.data)
            triggerActual = 1

            imageNow = _binding!!.imagenActual.drawable.toBitmap()
        }
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
