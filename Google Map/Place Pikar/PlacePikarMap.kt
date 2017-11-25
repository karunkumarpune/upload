package karun.com.googlemapdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.activity_main.*


class PlacePikarMap : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


   /* <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.INTERNET" />

    <meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyDdrtaTSjzTliPN3b8ZlywzMO_IJkr54Z8"/>

    compile 'com.google.android.gms:play-services:9.2.0'
*/

    private var mGoogleApiClient: GoogleApiClient? = null
    private val PLACE_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)


        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()


        val builder = PlacePicker.IntentBuilder()
        try {
            startActivityForResult(builder.build(this@PlacePikarMap), PLACE_PICKER_REQUEST)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }


    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient!!.connect()
    }

    override fun onStop() {
        mGoogleApiClient!!.disconnect()
        super.onStop()
    }


    override fun onConnectionFailed(p0: ConnectionResult) {

        Toast.makeText(applicationContext, p0.errorMessage.toString() + "", Toast.LENGTH_LONG).show();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                val stBuilder = StringBuilder()
                val placename = String.format("%s", place.name)
                val latitude = place.latLng.latitude.toString()
                val longitude = place.latLng.longitude.toString()
                val address = String.format("%s", place.address)
                val avatar = String.format("%s", place.websiteUri)
                val viewport = String.format("%s", place.viewport)
                stBuilder.append("Name: ")
                stBuilder.append(placename)
                stBuilder.append("\n")
                stBuilder.append("Latitude: ")
                stBuilder.append(latitude)
                stBuilder.append("\n")
                stBuilder.append("Logitude: ")
                stBuilder.append(longitude)
                stBuilder.append("\n")
                stBuilder.append("Address: ")
                stBuilder.append(address)
                stBuilder.append("\n Avatar: ")
                stBuilder.append(avatar)
                stBuilder.append("\n viewport: ")
                stBuilder.append(viewport)

               // textView.text = stBuilder.toString()

                Log.d("Location : ",stBuilder.toString())
            }
        }
    }
}





