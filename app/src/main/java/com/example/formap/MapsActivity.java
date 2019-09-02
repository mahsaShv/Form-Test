package com.example.formap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



    EditText nameT;
    EditText familyNameT;
    EditText phoneNumT;
    EditText homeNumT;
    EditText addressT;
    Button btnLogin;
    Switch switchee;
    ImageView locationV;

    String name;
    String familyName;
    String homeNum;
    String phoneNum;
    String gender;
    String address;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nameT = (EditText) findViewById(R.id.name);
        familyNameT = (EditText) findViewById(R.id.familyName);
        homeNumT = (EditText) findViewById(R.id.homeNum);
        phoneNumT = (EditText) findViewById(R.id.phoneNum);
        addressT = (EditText) findViewById(R.id.address);
        locationV = (ImageView) findViewById(R.id.location);


        btnLogin = (Button) findViewById(R.id.btnLogin);

        switchee = (Switch) findViewById(R.id.switchee);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = nameT.getText().toString();
                familyName = familyNameT.getText().toString();
                homeNum = homeNumT.getText().toString();
                phoneNum = phoneNumT.getText().toString();
                address = addressT.getText().toString();

                if (isConnectedToNetwork(MapsActivity.this)) {


                    if (name.length() > 0 && familyName.length() > 0 && homeNum.length() > 0 && phoneNum.length() > 0 && address.length() > 0) {
                        if (phoneNum.length() == 11 && homeNum.length() == 11) {


                            if (switchee.isChecked())
                                gender = "Female";
                            else
                                gender = "Male";


                            AsyncT asyncT = new AsyncT(name, familyName, homeNum, phoneNum, address, gender,latitude,longitude);
                            asyncT.execute();

                            Intent intent = new Intent(MapsActivity.this, GetInformationActivity.class);
                            startActivity(intent);


                        } else
                            Toast.makeText(MapsActivity.this, "Invalid phone number format", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(MapsActivity.this, "Fill in all required fields.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MapsActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });






    }

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


         mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.72, 51.38))
                .title("Marker")
                .draggable(true)
                .snippet("Hello")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                Toast.makeText(MapsActivity.this, "Dragging Start",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
//                // TODO Auto-generated method stub
//                Toast.makeText(
//                        MapsActivity.this,
//                        "Lat " + mMap.getMyLocation().getLatitude() + " "
//                                + "Long " + mMap.getMyLocation().getLongitude(),
//                        Toast.LENGTH_LONG).show();
//                System.out.println("yalla b2a "
//                        + mMap.getMyLocation().getLatitude());

                LatLng position = marker.getPosition(); //
                Toast.makeText(
                        MapsActivity.this,
                        "Lat " + position.latitude + " "
                                + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();

                latitude = position.latitude;
                longitude = position.longitude;
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(MainActivity.this, "Dragging",
                // Toast.LENGTH_SHORT).show();
                System.out.println("Draagging");
            }
        });

    }
}


class AsyncT extends AsyncTask<Void, Void, Void> {

    String name;
    String familyName;
    String homeNum;
    String phoneNum;
    String address;
    String gender;
    HttpURLConnection httpURLConnection;
    double lat;
    double longi;

    AsyncT(String name, String familyName, String homeNum, String phoneNum, String address, String gender , double lat, double longi
    ) {
        this.name = name;
        this.familyName = familyName;
        this.homeNum = homeNum;
        this.phoneNum = phoneNum;
        this.address = address;
        this.gender = gender;
        this.lat = lat;
        this.longi = longi;

    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://faran3.com");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`


//            String userCredentials = "09822222222:sana12345678";
//            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(),Base64.DEFAULT));

            final String basicAuth = "Basic " + Base64.encodeToString("09822222222:sana12345678".getBytes(), Base64.NO_WRAP);


//            httpURLConnection.setRequestProperty ("Authorization", basicAuth);

            httpURLConnection.setRequestProperty("Authorization", basicAuth);

            httpURLConnection.connect();


            JSONObject person = new JSONObject();
            try {
                person.put("region", 1);
                person.put("name", name);
                person.put("familyName", familyName);
                person.put("homeNum", homeNum);
                person.put("phoneNum", phoneNum);
                person.put("address", address);
                person.put("latitude", Double.toString(lat));
                person.put("longitude", Double.toString(longi));


            } catch (JSONException e) {
                e.printStackTrace();
            }


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(person.toString());
            wr.flush();
            wr.close();

            System.out.println(person.isNull("name"));


            InputStream response = httpURLConnection.getErrorStream();
            ;
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            StringBuilder sb = new StringBuilder();
            String line = null;

            int statusCode = httpURLConnection.getResponseCode();


            try {
                System.out.println("11111");
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");

                }
                System.out.println(sb.toString() + "nnnnnnnnnn " + statusCode);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
