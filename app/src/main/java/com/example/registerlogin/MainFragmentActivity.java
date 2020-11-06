package com.example.registerlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.registerlogin.Fragment.FriendListFragment;
import com.example.registerlogin.Fragment.HospitalFragment;
import com.example.registerlogin.Fragment.LocationFragment;
import com.example.registerlogin.Fragment.MainFragment;
import com.example.registerlogin.Fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragmentActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LocationFragment locationFragment = new LocationFragment();
    private FriendListFragment friendListFragment = new FriendListFragment();
    private HospitalFragment hospitalFragment = new HospitalFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private MainFragment mainFragment = new MainFragment();
    public static String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        Intent intent = getIntent();
        final String userId = intent.getExtras().getString("userId");
        username = userId;
        System.out.println("userName : "+username);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, mainFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements  BottomNavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            String userName = username;
            bundle.putString("userName",userName);
            locationFragment.setArguments(bundle);
            friendListFragment.setArguments(bundle);
            hospitalFragment.setArguments(bundle);
            settingFragment.setArguments(bundle);


            switch (item.getItemId())
            {
                case R.id.location:

                    Intent intent = new Intent(MainFragmentActivity.this, NewLocation.class);
                    intent.putExtra("userName",userName);
                    System.out.println("userName : "+userName);
                    startActivity(intent);
                    //transaction.replace(R.id.frameLayout, locationFragment).commitAllowingStateLoss();
                    break;
                case R.id.friendlist:
                    transaction.replace(R.id.frameLayout, friendListFragment).commitAllowingStateLoss();
                    break;
                case R.id.hospital:
                    Intent intent3 = new Intent(MainFragmentActivity.this, NewHospital.class);
                    intent3.putExtra("userName",userName);
                    System.out.println("userName : "+userName);
                    startActivity(intent3);
                    //transaction.replace(R.id.frameLayout, hospitalFragment).commitAllowingStateLoss();
                    break;
                case R.id.setting:
                    transaction.replace(R.id.frameLayout, settingFragment).commitAllowingStateLoss();
                    break;
                case R.id.call:
                    Intent intent2 = new Intent();
                    intent2.setAction(Intent.ACTION_DIAL);
                    intent2.setData(Uri.parse("tel:119"));
                    //Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:12345"));
                    startActivity(intent2);
                    break;
            }
            return true;
        }
    }
}
