package com.example.registerlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        final String userId = intent.getExtras().getString("userId");
        System.out.println(userId);
/*
        ImageButton imageButton =  findViewById(R.id.btn_call);
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
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        Button hotbutton = findViewById(R.id.btn_hot);
        hotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HospitalMap.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        Button friendlist = findViewById(R.id.btn_friends);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FriendList.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        Button btn_set = findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HospitalMap.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
*/

    }
}


