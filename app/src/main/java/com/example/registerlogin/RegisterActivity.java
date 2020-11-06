package com.example.registerlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_age, et_num, et_phone;
    private Button btn_register, test;
    private String et_num_str;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext  = this;
        //id 값 찾아주기
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_num = findViewById(R.id.et_num);
        test = findViewById(R.id.test);
        et_num_str = String.valueOf(Get_Usernum.usernum+1);
        et_num.setText(et_num_str);
        et_phone = findViewById(R.id.et_phone);





        //회원가입 버튼 클릭시 수행
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit text 에 현재 입력되어있는 값을 가져온다.
                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();
                String userName = et_name.getText().toString();
                int userNum = Integer.parseInt(et_num.getText().toString());

                int userAge = Integer.parseInt(et_age.getText().toString());
                //String userPhone = et_phone.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Database.getInstance(mContext).setPhoneNumber(et_phone.getText().toString());
                                Toast.makeText(getApplicationContext(), "회원 가입이 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, Update_Usernum.class);
                                startActivity(intent);
                            } else { //회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원 가입이 실패했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                };
                //서버로 volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userNum, userAge, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this );
                queue.add(registerRequest);

            }
        });
        test.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userNum = et_num.getText().toString();
                System.out.println(userNum);
            }
        });

    }
}