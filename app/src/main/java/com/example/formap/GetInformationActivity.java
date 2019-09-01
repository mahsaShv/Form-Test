package com.example.formap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

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
import java.util.concurrent.CountDownLatch;

public class GetInformationActivity  extends AppCompatActivity {

    TextView nameT;
    TextView genderT;
    TextView phoneNumT;
    TextView homeNumT;
    TextView addressT;
    TextView locT;

    String name;
    String familyName;
    String homeNum;
    String phoneNum;
    String gender;
    String address;
    String lat;
    String longi;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_getinfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameT = (TextView) findViewById(R.id.nameView);
        genderT = (TextView) findViewById(R.id.genderView);
        homeNumT = (TextView) findViewById(R.id.homeNumView);
        phoneNumT = (TextView) findViewById(R.id.phoneNumView);
        addressT = (TextView) findViewById(R.id.addressView);
        locT = (TextView) findViewById(R.id.locView);

        AsyncT2 asyncT2 = new AsyncT2(name , familyName, homeNum,phoneNum,address,gender,lat,longi,countDownLatch);
        asyncT2.execute();
        try {
            countDownLatch.wait();
            nameT.setText(name+" "+familyName);
            genderT.setText(gender);
            homeNumT.setText(homeNum);
            phoneNumT.setText(phoneNum);
            addressT.setText(address);
            locT.setText("Location : "+lat+ " , "+ longi);





        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}


class AsyncT2 extends AsyncTask<Void, Void, Void> {

    String name;
    String familyName;
    String homeNum;
    String phoneNum;
    String address;
    String gender;
    HttpURLConnection httpURLConnection;
    String lat;
    String longi;
    CountDownLatch countDownLatch;

    AsyncT2(String name, String familyName, String homeNum, String phoneNum, String address, String gender , String lat, String longi,CountDownLatch countDownLatch
    ) {
        this.name = name;
        this.familyName = familyName;
        this.homeNum = homeNum;
        this.phoneNum = phoneNum;
        this.address = address;
        this.gender = gender;
        this.lat = lat;
        this.longi = longi;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://faran3.com");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.


//            String userCredentials = "989822222222:12345678";
//            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(),Base64.DEFAULT));

            final String basicAuth = "Basic " + Base64.encodeToString("989822222222:12345678".getBytes(), Base64.NO_WRAP);


//            httpURLConnection.setRequestProperty ("Authorization", basicAuth);

            httpURLConnection.setRequestProperty("Authorization", basicAuth);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */ );

            httpURLConnection.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            String jsonString = sb.toString();


            JSONObject person = new JSONObject(jsonString);

            name = person.getString("name");
            familyName = person.getString("familyName");
            homeNum = person.getString("homeNum");
            phoneNum = person.getString("phoneNum");

            address = person.getString("address");
            gender = person.getString("gender");

            lat = person.getString("latitude");

            longi = person.getString("longitude");
            countDownLatch.countDown();












        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
}
