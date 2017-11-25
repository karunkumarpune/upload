package karun.com.googlemapdemo

import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

class Find_Distance : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //pusauli to mohania
        val s = getDistance(25.097458, 83.721476, 25.167526, 83.620795)
        Log.d("TAGS", " $s KM/S")
        Toast.makeText(applicationContext, "Total Distance: $s KM/S", Toast.LENGTH_SHORT).show()
    }


    private fun getDistance(latA: Double, lngA: Double, latB: Double, lngB: Double): String {
        val startPoint = Location("locationA")
        startPoint.latitude = latB
        startPoint.longitude = lngB

        val endPoint = Location("locationB")
        endPoint.latitude = latA
        endPoint.longitude = lngA

        val distance = startPoint.distanceTo(endPoint)

        Log.d("TAGS", " $distance M/S")
        val meters = distance / 1000
        val kilometer = meters.toString()
        val kilometer1 = kilometer.split("\\.".toRegex(), 2).toTypedArray()
        return kilometer1[0]
    }
}