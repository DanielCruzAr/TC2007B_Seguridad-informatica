package com.itesm.esenciapatrimonio

import android.os.Build
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.itesm.esenciapatrimonio.databinding.ActivityMainBinding
import com.parse.Parse

/**
 * @author e-corp
 * En el main se establece el menu de navegación
 * en el cual se agrupan los fragmentos principales
 */

class MainActivity : AppCompatActivity() {

    //variable para la configuración del menu de navegacion
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var context: Context = this

    /**
     * Android onCreate
     * Se establece la interfaz principal (mapa)
     * y se inicializa Parse
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Parse.enableLocalDatastore(this)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id)) // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                //.enableLocalDataStore()
                .build()
        )

        //Test Parse
        /*
        val ParseTestWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<ParseTestWorker>().build()
        WorkManager
            .getInstance(this)
            .enqueue(ParseTestWorkRequest)
         */
        ParseApp.init()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Solicitar permisos
        val permissions: Permissions = Permissions()
        permissions.requestPermissions(context)

        //Iniciar el menu lateral
        setSupportActionBar(binding.appBarMain.topAppBar)
        initializeNavbar()
    }

    /**
     * Drawer Layout
     **/
    private fun initializeNavbar(){
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navigationView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.view_map, R.id.view_advanced_search, R.id.view_about_us, R.id.view_login, R.id.view_donate
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Despliega el menu en la vista
     * @param menu que contiene todos los elementos del menu
     * @return true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true
    }

    /**
     * Controlador de la navegación
     * Establece en donde se van a inflar las views de los fragmentos
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}