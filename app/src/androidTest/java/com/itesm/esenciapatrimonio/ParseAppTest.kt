package com.itesm.esenciapatrimonio

import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.parse.Parse
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class ParseAppTest : AppCompatActivity()//: TestCase()
{
    //val oParse = ParseApp();

    fun ParseTest_GetRestoreSite(listRestoreSite:MutableList<SRestoreSite>):Unit
    {
        lateinit var oSite:SRestoreSite;

        assertNotNull(listRestoreSite)
        assertNotEquals(listRestoreSite?.size, 0)
    }

    @Before
    fun connectParse(){
            Parse.enableLocalDatastore(this)
            Parse.initialize(
                Parse.Configuration.Builder(this)
                    .applicationId(getString(R.string.back4app_app_id)) // if defined
                    .clientKey(getString(R.string.back4app_client_key))
                    .server(getString(R.string.back4app_server_url))
                    .enableLocalDataStore()
                    .build()
            )

            ParseApp.init()
    }

    @After
    fun closeParse(){

    }

    @Test
    @Throws(Exception::class)
    fun getAllRestoreSite(){
        ParseApp.getAllRestoreSite(){listRestoreSite ->
            lateinit var oSite:SRestoreSite;

            assertNotNull(listRestoreSite)
            assertNotEquals(listRestoreSite?.size, 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun addRestoreSite(){
        val site = SRestoreSite(site_name = "Test")
        ParseApp.addRestoreSite(site){listSite ->
            assertNotNull(listSite)
            assertNotEquals(listSite?.size, 0)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteRestoreSite(){

    }

    @Test
    @Throws(Exception::class)
    fun addCompare(){

    }

    @Test
    @Throws(Exception::class)
    fun deleteCompare(){

    }

    @Test
    @Throws(Exception::class)
    fun getCompareBySite(){

    }
}