package karun.com.googlemapdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {


	//     compile 'com.google.android.gms:play-services:9.2.0'


	LocationManager locationManager;
	String locationProvider;

	Button mBtnFind;
	GoogleMap mMap;
	EditText etPlace;

	private double lat = 0.0;
	private double lng = 0.0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initializeLocationManager();
		setContentView(R.layout.activity_main2);


		mBtnFind = (Button) findViewById(R.id.btn_show);
		etPlace = (EditText) findViewById(R.id.et_place);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		mBtnFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String location = etPlace.getText().toString();
				if (location == null || location.equals("")) {
					Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
					return;
				}

				String url = "https://maps.googleapis.com/maps/api/geocode/json?";

				try {
					location = URLEncoder.encode(location, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				String address = "address=" + location;
				String sensor = "sensor=false";

				url = url + address + "&" + sensor;
				DownloadTask downloadTask = new DownloadTask();
				downloadTask.execute(url);

			}
		});

	}


	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		this.locationManager.requestLocationUpdates(this.locationProvider, 400, 1, this);


	}

	@Override
	protected void onPause() {
		super.onPause();
		this.locationManager.removeUpdates(this);
	}

	private void initializeLocationManager() {
		this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();

		this.locationProvider = locationManager.getBestProvider(criteria, false);

		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Location location = locationManager.getLastKnownLocation(locationProvider);
		if (location != null) {

			onLocationChanged(location);
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		Log.d("TAGS:", "Current Lat Long : " + location.getLatitude() + "," + location.getLongitude());

		lat = location.getLatitude();
		lng = location.getLongitude();
	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		mMap.setMyLocationEnabled(true);

		lat=25.166865;
		lng=83.622684;
		LatLng sydney = new LatLng(lat, lng);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
		mMap.addMarker(new MarkerOptions()
				.title("")
				.snippet("")
				.position(sydney));

	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}


    private class DownloadTask extends AsyncTask<String, Integer, String>{

            String data = null;
            @Override
            protected String doInBackground(String... url) {
                    try{                    		
                            data = downloadUrl(url[0]);
                    }catch(Exception e){
                             Log.d("Background Task",e.toString());
                    }
                    return data;
            }

            @Override
            protected void onPostExecute(String result){
            		ParserTask parserTask = new ParserTask();
                    parserTask.execute(result);
            }

    }

	class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {
		
			List<HashMap<String, String>> places = null;			
			GeocodeJSONParser parser = new GeocodeJSONParser();
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	            places = parser.parse(jObject);
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return places;
		}
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){
			mMap.clear();
			
			for(int i=0;i<list.size();i++){

	            MarkerOptions markerOptions = new MarkerOptions();
	            HashMap<String, String> hmPlace = list.get(i);
				lat = Double.parseDouble(hmPlace.get("lat"));
				lng = Double.parseDouble(hmPlace.get("lng"));
	            String name = hmPlace.get("formatted_address");
	            LatLng latLng = new LatLng(lat, lng);
	            markerOptions.position(latLng);
	            markerOptions.title(name);
	            mMap.addMarker(markerOptions);
                if(i==0)
				   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }            
		}
	}

}
