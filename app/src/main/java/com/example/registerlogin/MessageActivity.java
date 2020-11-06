package com.example.registerlogin;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends AppCompatActivity {

    Context mContext;
    @BindView(R.id.rb_1)
    RadioButton rb_1;
    @BindView(R.id.rb_2) RadioButton rb_2;
    @BindView(R.id.input_msg)
    EditText input_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick(R.id.bt_alarm_register) void RegisterAlarm(){
        if(rb_1.isChecked()){
            Toast.makeText(getApplicationContext(),"알람1이 설정되었습니다!", Toast.LENGTH_SHORT).show();
            Database.getInstance(mContext).setMessage(0,input_msg.getText().toString());
        }else if(rb_2.isChecked()){
            Toast.makeText(getApplicationContext(),"알람2가 설정되었습니다!", Toast.LENGTH_SHORT).show();
            Database.getInstance(mContext).setMessage(1,input_msg.getText().toString());
        }
    }

}