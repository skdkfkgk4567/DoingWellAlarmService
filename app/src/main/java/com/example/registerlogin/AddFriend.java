package com.example.registerlogin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddFriend extends AppCompatActivity
{
    private TextView FriendName;
    private Button AddBuTTON;
    public static String insertname;
    public static String userNum;
    public static String friendNum;
    private String Status_Code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);

        FriendName = findViewById(R.id.FriendName);
        AddBuTTON = findViewById(R.id.AddBuTTON);

        Intent intent = getIntent();

        String username = intent.getExtras().getString("userName");
        insertname = username;
        AddBuTTON.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddFriend.ADD task = new AddFriend.ADD();
                task.execute(insertname,FriendName.getText().toString());
            }
        });


    }

    private String mJsonString;
    private static String TAG = "AddFriend";
    private class ADD extends AsyncTask<String, Void, String>
    {


        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AddFriend.this,
                    "Please Wait", null, true, true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            String nameKeyword = params[0];
            String friendKeyword = params[1];
            userNum = params[0];
            friendNum = params[1];


            String serverURL = "http://ansimapk.dothome.co.kr/insert.php";
            String postParameters = "?userNum=" +  nameKeyword + "&friendName=" + friendKeyword;

            try {
                serverURL = serverURL+postParameters;
                Log.d(TAG,"serverURL = " + serverURL);
                System.out.println("serverURL : " + serverURL);
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK)
                {
                    inputStream = httpURLConnection.getInputStream();
                    Status_Code = "HTTP_OK";
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                    Status_Code = "HTTP_ERROR";
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

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            System.out.println(" Status_Code :  "+Status_Code);
            if(Status_Code.equals("HTTP_OK"))
            {
                Toast.makeText(getApplicationContext(), "추가완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), FriendList.class);
                intent.putExtra("userId",insertname);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), friendNum + "님을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddFriend.class);
                intent.putExtra("userName",insertname);
                startActivity(intent);

            }
        }
    }
}
