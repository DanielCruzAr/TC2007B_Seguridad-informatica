package com.itesm.esenciapatrimonio

import android.R.attr
import android.app.Application
import android.graphics.Picture
import android.util.Log
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import com.parse.*
import java.io.File
import java.time.Instant
import android.R.attr.bitmap

import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter


typealias CallbackGetRestoreSite = (MutableList<SRestoreSite>)->Unit
typealias CallbackGetPicture = (MutableList<SPicture>)->Unit
typealias CallbackGetCompare = (MutableList<SComparePicture>)->Unit
typealias CallbackCheckExist = (Boolean)->Unit
typealias CallbackImage = (MutableList<String>)->Unit
typealias CallbackDeleteSite = (String)->Unit

public enum class EPicType(var type:Int)
{
    undefined(0)
}

public data class SRestoreSite(var objectId:String = ""
                                , var site_name:String = "not defined name"
                                , var information:String = "not defined information"
                                , var est_year:Int = 0
                                , var restore_year:Int = 0
                                , var address:String = "not defeined address"
                                , var coordinate_x:Double = 0.0
                                , var coordinate_y:Double = 0.0
                                )

public data class SPicture(var objectId:String = ""
                            , var url:String = ""
                            , var image_name:String = ""
                            )

public data class SComparePicture(var objectId:String = ""
                                    , var oldPic_id:String = ""
                                    , var newPic_id:String = ""
                                    , var site_id:String = ""
                                    , var description:String = ""
                                    )
/**
 * This is the object of the ParseApp,
 * It will handle the parse library,
 * communicate withe the Back4App cloud platform,
 * in order to save/get/delete the object
 * in the table of Picture, RestoreSite and ComparePicture
 *
 * @author Wenguang Hu
 * */
public object ParseApp /*: Application()*/ {
    /*
    override fun onCreate()
    {
        super.onCreate()


    }
*/
    lateinit var returnRestoreSite:MutableList<SRestoreSite>;
    lateinit var returnPicture:MutableList<SPicture>;
    lateinit var returnComparePicture:MutableList<SComparePicture>;
    lateinit var pCallbackSite:CallbackGetRestoreSite;
    lateinit var pCallbackPicture:CallbackGetPicture;
    lateinit var pCallbackCompare:CallbackGetCompare;

    var listImageTest:MutableList<String> = mutableListOf()

    var bIsUpdatedSite:Boolean = false;
    var bIsUpdatedPicture:Boolean = false;
    var bIsUpdatedCompare:Boolean = false;

    //those one are in use
    private lateinit var mapSiteObjectId: MutableMap<String, String>
    private lateinit var listPicture: MutableList<SPicture>
    private lateinit var listCompare: MutableList<SComparePicture>
    private lateinit var listRestoreSite: MutableList<SRestoreSite>

/*
    fun dummyInit()
    {

    }
 */

    /**
     * it init the Parse library, and get all the all the network data to the
     * @param
     * */
    fun init()
    {
        bIsUpdatedPicture = false
        bIsUpdatedCompare = false
        bIsUpdatedSite = false

        //Do the get All for all the Picture, Compare and Restore site table
        getAllRestoreSite {listSite ->
            mapSiteObjectId = mutableMapOf()
            listRestoreSite = mutableListOf()

            for(oSite in listSite){
                mapSiteObjectId[oSite.site_name] = oSite.objectId
                listRestoreSite.add(oSite)
            }

            bIsUpdatedSite  = true
        }

        getAllPicture{listPic ->

        }

        getAllComparePicture{listCom ->

        }
    }

    /**
     * get the Restore Site by the ObjectId
     * @param objectId the objectId in the Parse Database
     * @param pCallback the callback will be called when the parse succeed, and return the list of the site to it
     * */
    fun getRestoreSite(objectId: String, pCallback: CallbackGetRestoreSite):Unit
    {
        returnRestoreSite = mutableListOf(SRestoreSite(objectId = objectId))

        var query = ParseQuery.getQuery<ParseObject>("RestoreSite")

        if(pCallback != null) {
            pCallbackSite = pCallback;

            query.getInBackground(objectId) { `object`, e ->
                if (e == null) {
                    // object will be your game score
                    returnRestoreSite[0].site_name = `object`.getString("site_name").toString()
                    returnRestoreSite[0].information = `object`.getString("information").toString()
                    returnRestoreSite[0].est_year = `object`.getInt("est_year")
                    returnRestoreSite[0].restore_year = `object`.getInt("restore_year")
                    returnRestoreSite[0].address = `object`.getString("address").toString()
                    returnRestoreSite[0].coordinate_x = `object`.getDouble("coordinate_x")
                    returnRestoreSite[0].coordinate_y = `object`.getDouble("coodinate_y")

                    this.pCallbackSite(returnRestoreSite)

                } else {
                    // something went wrong
                }
            }
        }

        return;
    }

    private fun getSiteListFromParse(objectList: List<ParseObject>?)
    {
        lateinit var obj:ParseObject
        if (objectList != null) {
            this.returnRestoreSite = mutableListOf()
            for(obj in objectList){
                this.returnRestoreSite.add(SRestoreSite(
                    obj.objectId//obj.getString("objectId").toString()
                    , obj.getString("site_name").toString()
                    , obj.getString("information").toString()
                    , obj.getInt("est_year")
                    , obj.getInt("restore_year")
                    , obj.getString("address").toString()
                    , obj.getDouble("coordinate_x")
                    , obj.getDouble("coodinate_y")
                ))
            }

            this.pCallbackSite(this.returnRestoreSite);
        }
        else
        {
            Log.d("Parse", "Error: objectList null")
        }
    }

    /**
     * Create a new ComparePicture object
     * @param siteName the name of the site
     * @param siteDescrip the description of the compare picture
     * @param imageNew the bitmap of the image of site's new picture
     * @param imageOld the bitmap of the image of site's old picture
     * @param pCallback the callback will be called when pare save is done, and return the Compare Structure of the data
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addComparePicture(siteName:String, siteDescrip:String, imageNew:Bitmap, imageOld:Bitmap, pCallback:(SComparePicture)->Unit)
    {
        val timeStamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        val objImageNew = ParseObject("Picture")
        val objImageOld = ParseObject("Picture")
        val streamImageNew = ByteArrayOutputStream()
        val streamImageOld = ByteArrayOutputStream()

        imageNew.compress(Bitmap.CompressFormat.PNG, 100, streamImageNew)
        imageOld.compress(Bitmap.CompressFormat.PNG, 100, streamImageOld)

        val byteArrayImageNew = streamImageNew.toByteArray()
        val byteArrayImageOld = streamImageOld.toByteArray()

        val oParseFileImageNew:ParseFile =  ParseFile("New.png",byteArrayImageNew)
        val oParseFileImageOld:ParseFile =  ParseFile("Old.png",byteArrayImageOld)

        objImageNew.put("file", oParseFileImageNew)
        objImageOld.put("file", oParseFileImageOld)

        objImageNew.put("image_name", "new_" + siteName + "_" + timeStamp)
        objImageOld.put("image_name", "old_" + siteName + "_" + timeStamp)

        //Save Image New
        oParseFileImageNew.saveInBackground(
            SaveCallback { e ->
                if(e == null) {
                    objImageNew.saveInBackground { e ->

                        if (e == null) {
                            //Save Image Old
                            oParseFileImageOld.saveInBackground(
                                SaveCallback { e ->
                                    if(e == null) {
                                        objImageOld.saveInBackground { e ->

                                            if (e == null) {
                                                //Get Site Obj
                                                val siteQuery = ParseQuery<ParseObject>("RestoreSite")
                                                siteQuery.whereEqualTo("site_name", siteName)
                                                siteQuery.getFirstInBackground { `object`, e ->
                                                    if (e == null) {
                                                        if(`object` != null)
                                                        {
                                                            //save Compare Object
                                                            val objCompare = ParseObject("ComparePicture")

                                                            objCompare.put("oldPic_id", objImageOld)
                                                            objCompare.put("newPic_id", objImageNew)
                                                            objCompare.put("site_id", `object`)
                                                            objCompare.put("description", siteDescrip)

                                                            objCompare.saveInBackground { e ->
                                                                if(e == null){
                                                                    if(pCallback != null)
                                                                    {
                                                                        init()
                                                                        pCallback(SComparePicture(objCompare.objectId, oParseFileImageOld.url, oParseFileImageNew.url, siteName, siteDescrip))
                                                                    }
                                                                }
                                                                else{
                                                                    Log.d("Parse", "Error: " + e.message)
                                                                }
                                                            }
                                                        }
                                                        else
                                                        {
                                                            Log.d("Parse", "Can not find the site")
                                                        }
                                                    } else {
                                                        Log.d("Parse", "Error: " + e.message)
                                                    }
                                                }
                                            } else {
                                                //We have an error.We are showing error message here.
                                                Log.d("Parse", "Error: " + e.message)
                                            }
                                        }
                                    }
                                    else{
                                        Log.d("Parse", "Save File Error " + e.message)
                                    }
                                })

                        } else {
                            //We have an error.We are showing error message here.
                            Log.d("Parse", "Error: " + e.message)
                        }
                    }
                }
                else{
                    Log.d("Parse", "Save File Error " + e.message)
                }
            })
    }

    /**
     * add the single picture to the Picture table
     * @param oFile the bitmap of the file need to be added
     * @param fileName the file name of the picture
     * @param pCallback the callback will be called when the save image finished, return the parse object of the picture
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addPicture(oFile:Bitmap, fileName:String = "undefined name ${Instant.now().toString()}", pCallback:(ParseObject)->Unit){
        val newPictureObject = ParseObject("Picture")

//        val isFile = oFile.isFile
//        val isExist = oFile.exists()
        val stream = ByteArrayOutputStream()

        oFile.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val byteArray = stream.toByteArray()

        val oParseFile:ParseFile =  ParseFile(byteArray)
        newPictureObject.put("file", oParseFile)
        newPictureObject.put("image_name", fileName)

        oParseFile.saveInBackground(
            SaveCallback { e ->
                if(e == null) {
                    newPictureObject.saveInBackground { e ->

                        if (e == null) {
                            //We saved the object and fetching data again
                            if (pCallback != null) {
                                pCallback(newPictureObject)
                            }
                        } else {
                            //We have an error.We are showing error message here.
                            Log.d("Parse", "Error: " + e.message)
                        }
                    }
                }
                else{
                    Log.d("Parse", "Save File Error " + e.message)
                }
            })
    }

    /**
     * Add the restore site to the RestoreSite table
     * @param oSite the structure contain the new site information
     * @param pCallback the callback will be called when the save operation is finished
     * */
    fun addRestoreSite(oSite:SRestoreSite, pCallback: CallbackGetRestoreSite):Unit{
        val newSiteObject = ParseObject("RestoreSite")

        newSiteObject.put("site_name", oSite.site_name)
        newSiteObject.put("information", oSite.information)
        newSiteObject.put("est_year", oSite.est_year)
        newSiteObject.put("restore_year", oSite.restore_year)
        newSiteObject.put("address", oSite.address)
        newSiteObject.put("coordinate_x", oSite.coordinate_x)
        newSiteObject.put("coodinate_y", oSite.coordinate_y)

        newSiteObject.saveInBackground { e ->

            if (e == null) {
                //We saved the object and fetching data again
                if(pCallback != null)
                {
                    pCallback(mutableListOf(oSite))
                }
            } else {
                //We have an error.We are showing error message here.
                Log.d("Parse", "Error: " + e.message)
            }
        }
    }

    /**
     * Delete the specific site in the RestoreSite table
     * @param siteName the name of the site which we need to delete
     * @param pCallback the callback will be called when the delete operation is done, it will get the siteName as parameter
     * */
    fun deleteRestoreSite(siteName:String, pCallback:CallbackDeleteSite){
        var query = ParseQuery.getQuery<ParseObject>("RestoreSite")
        query.whereEqualTo("site_name", siteName);

        query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
            if (e == null) {
                Log.d("Parse", "Delete " + objectList?.size + " Site")

                if(objectList != null) {
                    for ((index, obj) in objectList.withIndex()) {
                        obj.deleteInBackground{ e ->
                            if(e == null){
                                if(index == objectList.size - 1){
                                    pCallback(siteName)
                                }
                            }
                            else{
                                Log.d("Parse", "Delete Error:" + e.message)
                            }
                        }
                    }
                }

            } else {
                Log.d("Parse", "Error: " + e.message)
            }
        }
    }

    /**
     * Get all the list of the site in RestoreSite table
     * @param pCallback the callback will be called when the operation is done, return a list of SRestoreSite as parameter
     * */
    fun getAllRestoreSite(pCallback: CallbackGetRestoreSite):Unit
    {
        this.returnRestoreSite = mutableListOf()

        var query = ParseQuery.getQuery<ParseObject>("RestoreSite")
        query.orderByAscending("site_name");
        /*
        if(this.bIsUpdatedSite)
        {
            query.fromLocalDatastore();
        }*/

        if(pCallback != null) {
            pCallbackSite = pCallback;

            query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                if (e == null) {
                    Log.d("Parse", "Retrieved " + objectList?.size + " Site")
                    this.bIsUpdatedSite = true;
                    this.getSiteListFromParse(objectList);
                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }

    /*
    fun getAllRestoreSiteByName(siteName:String, pCallback: CallbackGetRestoreSite):Unit
    {
        this.returnRestoreSite = mutableListOf(SRestoreSite())

        var query = ParseQuery.getQuery<ParseObject>("RestoreSite")
        query.whereEqualTo("site_name", siteName);
        if(this.bIsUpdatedSite)
        {
            query.fromLocalDatastore();
        }

        if(pCallback != null) {
            pCallbackSite = pCallback;

            query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                //progressDialog!!.hide()
                if (e == null) {
                    Log.d("Parse", "Retrieved " + objectList?.size + " Site")
                    lateinit var obj:ParseObject
                    if (objectList != null) {
                        this.getSiteListFromParse(objectList);
                    }
                    else
                    {
                        Log.d("Parse", "Error: objectList null")
                    }
                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }


    fun isSiteNameExist(siteName:String, pCallback:CallbackCheckExist)
    {
        var query = ParseQuery.getQuery<ParseObject>("RestoreSite")
        query.whereEqualTo("site_name", siteName);
        if(this.bIsUpdatedSite)
        {
            query.fromLocalDatastore();
        }

        if(pCallback != null) {

            query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                //progressDialog!!.hide()
                if (e == null) {
                    Log.d("Parse", "Retrieved " + objectList?.size + " Site")

                    if (objectList != null) {
                        var bExist = false;
                        if(objectList.isNotEmpty())
                        {
                            bExist = true;
                        }

                        pCallback(bExist);
                    }
                    else
                    {
                        Log.d("Parse", "Error: objectList null")
                    }
                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }
    */

    /**
     * Get all the object in the ComparePicture table
     * @param pCallback when the get operation is done, the call back will be called, pass the list of the SComparePicture as parameter
     * */
    fun getAllComparePicture(pCallback:(MutableList<SComparePicture>)->Unit)
    {
        var query = ParseQuery.getQuery<ParseObject>("ComparePicture")
        /*
        if(this.bIsUpdatedSite)
        {
            query.fromLocalDatastore();
        }*/

        if(pCallback != null) {

            query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                if (e == null) {
                    Log.d("Parse", "Compare " + objectList?.size + " Site")

                    if(objectList != null) {
                        listCompare = mutableListOf()

                        for (obj in objectList) {
                            listCompare.add(SComparePicture(obj.objectId
                                , obj.getParseObject("oldPic_id")?.fetchIfNeeded<ParseObject>()?.getParseFile("file")?.url.toString()
                                , obj.getParseObject("newPic_id")?.fetchIfNeeded<ParseObject>()?.getParseFile("file")?.url.toString()
                                , obj.getParseObject("site_id")?.fetchIfNeeded<ParseObject>()?.getString("site_name").toString()
                                , obj.getString("description").toString()
                                )
                            )
                        }

                        this.bIsUpdatedCompare = true;

                        if(pCallback != null){
                            pCallback(listCompare)
                        }
                    }

                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }

    /**
     * get all the Compare Picture of the same site
     * @param siteName the name of the site
     * @param pCallback the callback will be called after the operation is done, pass the list of SComparePicture as parameter
     * */
    fun getAllComparePictureBySite(siteName:String, pCallback:(MutableList<SComparePicture>)->Unit)
    {
        var queryCompare = ParseQuery.getQuery<ParseObject>("ComparePicture")
        var querySite = ParseQuery.getQuery<ParseObject>("RestoreSite")

        querySite.whereEqualTo("site_name", siteName);
        queryCompare.whereMatchesQuery("site_id", querySite)
        /*
        if(this.bIsUpdatedSite)
        {
            query.fromLocalDatastore();
        }*/

        if(pCallback != null) {

            queryCompare.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                if (e == null) {
                    Log.d("Parse", "Compare " + objectList?.size + " Site")

                    if(objectList != null) {
                        listCompare = mutableListOf()

                        for (obj in objectList) {
                            listCompare.add(SComparePicture(obj.objectId
                                , obj.getParseObject("oldPic_id")?.fetchIfNeeded<ParseObject>()?.getParseFile("file")?.url.toString()
                                , obj.getParseObject("newPic_id")?.fetchIfNeeded<ParseObject>()?.getParseFile("file")?.url.toString()
                                , obj.getParseObject("site_id")?.fetchIfNeeded<ParseObject>()?.getString("site_name").toString()
                                , obj.getString("description").toString()
                            )
                            )
                        }

                        this.bIsUpdatedCompare = true;

                        if(pCallback != null){
                            pCallback(listCompare)
                        }
                    }

                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }

    /**
     * get all the picture in Picture table
     * @param pCallback the callback will be called after operation is done, pass the list of SPicture as parameter
     * */
    fun getAllPicture(pCallback:(MutableList<SPicture>)->Unit)
    {
        //this.returnRestoreSite = mutableListOf(SRestoreSite())
        this.listPicture = mutableListOf()

        var query = ParseQuery.getQuery<ParseObject>("Picture")
 //       if(this.bIsUpdatedSite)
//        {
//            query.fromLocalDatastore();
//        }

        if(pCallback != null) {
            //pCallbackSite = pCallback;

            query.findInBackground { objectList: List<ParseObject>?, e: ParseException? ->
                if (e == null) {
                    Log.d("Parse", "Retrieved Image" + objectList?.size + " Site")

                    var listImage:MutableList<String> = mutableListOf()

                    if (objectList != null) {
                        for(obj in objectList) {
                            //obj.getParseFile("file").toString()
                            //val oFile = obj.getParseFile("file")
                            //oFile?.getUrl()
                            //val bitmap = BitmapFactory.decodeStream(obj.getParseFile("file").toString().byteInputStream())
                            val imageUrl = obj.getParseFile("file")?.url
                            if(imageUrl != null) {
                                //listImage.add(imageUrl)
                                listPicture.add(SPicture(obj.objectId, imageUrl, obj.getString("image_name").toString()))
                            }
                        }

                        listImageTest = listImage
                        pCallback(listPicture)

                        bIsUpdatedPicture = true
                    }
                    //this.bIsUpdatedSite = true;
                    //this.getSiteListFromParse(objectList);
                } else {
                    Log.d("Parse", "Error: " + e.message)
                }
            }
        }

        return;
    }

    /**
     * Delete the Compare Picture object by the ObjectId
     * @param objectId the object id of the ComparePicture object
     * */
    fun deleteComparePicture(objectId: String)
    {
        var query = ParseQuery.getQuery<ParseObject>("ComparePicture")

        query.getInBackground(objectId) { `object`, e ->
            if (e == null) {
                // object will be your game score
                val objImageOld = `object`.getParseObject("oldPic_id")
                val objImageNew = `object`.getParseObject("newPic_id")
                //val fileImageOld = objImageOld?.fetchIfNeeded<ParseObject>()?.getParseFile("file")
                //val fileImageNew = objImageNew?.fetchIfNeeded<ParseObject>()?.getParseFile("file")

                `object`.delete()
                objImageOld?.delete()
                objImageNew?.delete()
            } else {
                // something went wrong
            }
        }
    }

    /*
    fun googleLogin(user:String, tokenString:String)
    {
        val authData:Map<String, String> = mapOf("access_token" to tokenString, "id" to user)

        ParseUser.logInWithInBackground("google", authData)
    }
    */

}