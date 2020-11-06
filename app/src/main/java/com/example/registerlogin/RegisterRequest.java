package com.example.registerlogin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    // 서버 url 설정 (PHP 파일 연동)
    final static private String URL = "http://ansimapk.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword , String userName, int userNum, int userAge, Response.Listener<String> listener) {
        super(Method.POST,URL,listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword",userPassword);
        map.put("userName",userName);
        map.put("userNum",userNum + "");
        map.put("userAge",userAge + "");
        //map.put("userPhone",userPhone);
        for(Map.Entry<String, String>  entry : map.entrySet())
        {
            System.out.println("[key] : " + entry.getKey() + " [Value] : " + entry.getValue());
        }

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        System.out.println(map);
        return map;
    }
}
