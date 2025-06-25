package edu.umb.cs443

import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val weatherQuery = "https://api.openweathermap.org/data/3.0/onecall?"
    private val geoQuery= "https://api.openweathermap.org/geo/1.0/direct?q="
    private val apikey = "&appid=43537ee5cb4348e43f41344354cb2ef8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mFragment.getMapAsync(this)

        findViewById<Button>(R.id.button).setOnClickListener{
            getWeatherInfo()
        }
        val edittext = findViewById<View>(R.id.editText) as EditText
        edittext.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> // If the event is a key-down event on the "enter" button
            if (event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                // Perform action on key press
                try {
                    getWeatherInfo()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val defaultLocation = LatLng(42.314083, -71.037929)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
        }
    }

    fun processWeatherJson(result: String?) {
        if (result == null) return
        try {
            val jsonObj = JSONObject(result)
            val current = jsonObj.getJSONObject("current")
            val tempCelsius = current.getDouble("temp") - 273.15f

            // get weather icon
            val iconCode = current.getJSONArray("weather").getJSONObject(0).getString("icon")

            // update UI
            runOnUiThread {
                findViewById<TextView>(R.id.textView).text = getString(R.string.temperature_format, tempCelsius)
                setWeatherIcon("https://openweathermap.org/img/wn/$iconCode@2x.png")
            }
        } catch (e: Exception) {
            Log.e(DEBUG_TAG, "Error on weather JSON parsing: $e")
        }
    }

    fun setWeatherIcon(url: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                URL(url).openStream().use { inputStream ->
                    val bitMap = BitmapFactory.decodeStream(inputStream)
                    withContext(Dispatchers.Main) {
                        findViewById<ImageView>(R.id.imageView).setImageBitmap(bitMap)
                    }
                }
            } catch (e: Exception) {
                Log.e(DEBUG_TAG, "Weather Image can't download: $e")
            }
        }
    }

    fun getWeatherInfo(){
        val myedittext = findViewById<View>(R.id.editText) as EditText
        var cityname = myedittext.text.toString() ?: return
        myedittext.clearFocus()
        val connMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCap = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        if (networkCap != null && (networkCap.hasTransport(TRANSPORT_WIFI)||networkCap.hasTransport(
                TRANSPORT_CELLULAR))) {
            var query = geoQuery+cityname+"&limit=1"+apikey
            Log.d(DEBUG_TAG, "The query URL is: $query")
            // Log.i("MyTag", query);
            GlobalScope.launch {
                var jStr = downloadUrl(query)
                Log.d(DEBUG_TAG, "The response is: $jStr")
                val ll: LatLng = processJStr(jStr)!!
                ll.let { latLng ->
                    var weatherUrl = "$weatherQuery$apikey&lat=${latLng.latitude}&lon=${latLng.longitude}"
                    val weatherResponse = downloadUrl(weatherUrl)
                    processWeatherJson(weatherResponse)
                }
                Log.d(DEBUG_TAG, "Lat, Log: ${ll.latitude}, ${ll.longitude}")
            }
        } else {
            Toast.makeText(
                applicationContext,
                "No network connection available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*process the JSON string from GEO query*/
    fun processJStr(result: String?): LatLng? {
        if (result == null) {
            Log.i(DEBUG_TAG, "weather info is null")
            return null
        }
        val jo = JSONArray(result).get(0) as JSONObject
        val lat = jo.getDouble("lat")
        val lng = jo.getDouble("lon")

        return LatLng(lat,lng)
    }

    /*download an URL object*/
    fun downloadUrl(myurl: String): String {
        var `is`: InputStream? = null
        var result = String()
        Log.d(DEBUG_TAG, "The query URL is: $myurl")

        try {
            val url = URL(myurl)
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 10000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            conn.doInput = true
            // Starts the query
            conn.connect()
            val response = conn.responseCode
            Log.d(DEBUG_TAG, "The response is: $response")
            `is` = conn.inputStream
            val bis = BufferedInputStream(`is`)
            var read = 0
            val bufSize = 512
            val buffer = ByteArray(bufSize)
            while (true) {
                read = bis.read(buffer)
                if (read == -1) {
                    break
                }
                result += String(buffer)
            }
        } catch (e: Exception) {
            println(e)
        } finally {
            if (`is` != null) {
                `is`.close()
                Log.d(DEBUG_TAG, "is is closed")
            }
        }
        return result
    }

    companion object {
        const val DEBUG_TAG = "edu.umb.cs443.MYMSG"
    }
}