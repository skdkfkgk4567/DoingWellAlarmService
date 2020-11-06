package com.example.registerlogin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HospitalMap extends AppCompatActivity implements AutoPermissionsListener, GoogleMap.OnMarkerClickListener
{
    private static final String TAG = "HospitalMap";
    private GoogleMap map;
    public static String tts, tts2;
    SupportMapFragment mapFragment;
    public static String mapx;
    public static String mapy;
    GoogleMap.OnMarkerClickListener markerClickListener;
    LocationManager manager;
    private ArrayList<PersonData> mArrayList;
    private UsersAdapter mAdapter;
    private String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalmap);
        Intent intent = getIntent();
        final String userId = intent.getExtras().getString("userId");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.hospitalmap);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
/*
        ImageButton imageButton = (ImageButton) findViewById(R.id.btn_call);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:119"));
                //Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:12345"));
                startActivity(intent);
            }
        });

        Button mapbutton = findViewById(R.id.btn_map);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalMap.this, MapActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        Button friendlist = findViewById(R.id.btn_friends);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalMap.this, FriendList.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });


        Button hotbutton = findViewById(R.id.btn_hot);
        hotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalMap.this, HospitalMap.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
*/
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                startLocationService();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                map.setMyLocationEnabled(true);
                map.setOnMarkerClickListener(markerClickListener);

            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AutoPermissions.Companion.loadAllPermissions(this, 100);
        GetData task = new GetData();
        task.execute( "http://ansimapk.dothome.co.kr/test2.php", "");
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            int chk1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int chk2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            Location location = null;
            if (chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else {
                return;
            }

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String msg = "최근 위치 ->  Latitue : " + latitude + "\nLongitude : " + longitude;
                showCurrentLocation(latitude, longitude);
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);



        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerID = marker.getId();
        LatLng location = marker.getPosition();
        Toast.makeText(HospitalMap.this, "마커 클릭 Marker ID : "+markerID+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
        return false;
    }

    class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.d("Map", message);
            mapx = String.valueOf(longitude);
            mapy = String.valueOf(latitude);
            try {
                showResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            showCurrentLocation(latitude, longitude);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this, "permissions granted : " + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(HospitalMap.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){

            }
            else {
                mJsonString = result;
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult()
    {
        String TAG_JSON="webnautes";
        String TAG_NAME = "Hname";
        String TAG_ADD = "Hadd";
        String TAG_HX ="Hx";
        String TAG_HY = "Hy";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject item = jsonArray.getJSONObject(i);

                String Hname = item.getString(TAG_NAME);
                String Hadd = item.getString(TAG_ADD);
                String Hx = item.getString(TAG_HX);
                String Hy = item.getString(TAG_HY);


                double Xpos = Double.parseDouble(Hx);
                double Ypos = Double.parseDouble(Hy);
                int height = 100;
                int width = 100;
                BitmapDrawable bitmapDrawable= (BitmapDrawable) getResources().getDrawable(R.drawable.hospital_marker_red);
                Bitmap b = bitmapDrawable.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b,width,height,false);

                PersonData personalData = new PersonData();

                personalData.setMember_id(Hname);
                personalData.setMember_pass(Hadd);
                personalData.setMember_name(Hx);
                personalData.setMember_num(Hy);

                MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(Ypos, Xpos));
                options.title(Hname);
                options.snippet(Hadd);
                options.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                map.addMarker(options);
            }



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }



    public static void main(String[] args)
    {
        new HospitalMap();
    }

}
