package com.example.registerlogin.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.registerlogin.AddFriend;
import com.example.registerlogin.AlarmReceiver;
import com.example.registerlogin.Database;
import com.example.registerlogin.MessageActivity;
import com.example.registerlogin.R;
import com.example.registerlogin.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class SettingFragment extends Fragment implements View.OnClickListener
{
    TimePicker picker;
    Context mContext;
    int PERMISSION_ALL = 1;

    @BindView(R.id.rb_1)
    RadioButton rb_1;
    @BindView(R.id.rb_2)
    RadioButton rb_2;
    String[] PERMISSIONS = {
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.RECEIVE_WAP_PUSH,
            android.Manifest.permission.RECEIVE_MMS
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    void diaryNotification(Calendar calendar,int index)
    {
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean dailyNotify = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DAILY_NOTIFICATION, true);
        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = getActivity().getPackageManager();
        //ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(getContext(), AlarmReceiver.class);
        alarmIntent.putExtra("type",index);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, alarmIntent, index);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (Build.VERSION.SDK_INT >= 23) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            // 퍼미션이 승인 거부되면
            else {
                //finish();
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savesInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Button bt_alarm_register = view.findViewById(R.id.bt_alarm_register);
        bt_alarm_register.setOnClickListener(this);
        Button bt_message = view.findViewById(R.id.bt_message);
        bt_message.setOnClickListener(this);
        Button bt_delete = view.findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(this);

        mContext  = view.getContext();
        ButterKnife.bind(this, view);
        picker=view.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        if(!hasPermissions(getContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }/*
        picker=getActivity().findViewById(R.id.timePicker);
        picker.setIs24HourView(true);*/

        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("daily alarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        Date nextDate = nextNotifyTime.getTime();

        // 이전 설정값으로 TimePicker 초기화
        Date currentTime = nextNotifyTime.getTime();
        SimpleDateFormat HourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat MinuteFormat = new SimpleDateFormat("mm", Locale.getDefault());
        int pre_hour = Integer.parseInt(HourFormat.format(currentTime));
        int pre_minute = Integer.parseInt(MinuteFormat.format(currentTime));
        if (Build.VERSION.SDK_INT >= 23 ){
            picker.setHour(pre_hour);
            picker.setMinute(pre_minute);
        }
        else{
            picker.setCurrentHour(pre_hour);
            picker.setCurrentMinute(pre_minute);
        }

        return view;
    }

    @Override
    public void onClick(View view)
    {
        System.out.println(view.getId());
        switch (view.getId())
        {
            case R.id.bt_alarm_register:
                int hour, hour_24, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour_24 = picker.getHour();
                    minute = picker.getMinute();
                }
                else{
                    hour_24 = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if(hour_24 > 12) {
                    am_pm = "PM";
                    hour = hour_24 - 12;
                }
                else
                {
                    hour = hour_24;
                    am_pm="AM";
                }

                // 현재 지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }
                Date currentDateTime = calendar.getTime();
                //  Preference에 설정한 값 저장

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
                editor.apply();

                if(rb_1.isChecked()){
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getContext(),date_text + "으로 알람1이 설정되었습니다!", Toast.LENGTH_SHORT).show();
                    diaryNotification(calendar,0);
                    //Database.getInstance(mContext).setMessage(0,input_msg.getText().toString());
                }else if(rb_2.isChecked()){
                    String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                    Toast.makeText(getContext(),date_text + "으로 알람2가 설정되었습니다!", Toast.LENGTH_SHORT).show();
                    diaryNotification(calendar,1);
                    //Database.getInstance(mContext).setMessage(1,input_msg.getText().toString());
                }
                break;
            case R.id.bt_message:
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_delete:
                if(rb_1.isChecked()){
                    Database.getInstance(mContext).setMessage(0,"0");
                    Toast.makeText(mContext,"알람1이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }else if(rb_2.isChecked()){
                    Database.getInstance(mContext).setMessage(1,"0");
                    Toast.makeText(mContext,"알람2가 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb_1:
                break;
            case R.id.rb_2:
                break;
        }
    }
}
