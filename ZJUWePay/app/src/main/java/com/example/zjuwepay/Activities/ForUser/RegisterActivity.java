package com.example.zjuwepay.Activities.ForUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_REGISTER = "/api/user/register";

    //components
    private EditText etRegName, etRegPwd1, etRegPwd2, etRegPhone, etPwdEmail, etDescription;
    private ImageView btnRegConfirm, btnBackToMenu, iv_img;
    private LinearLayout btnSetFace;
    private Gson myPack = new Gson();

    //setting from other activity
    private int face_user_chosen;

    //async control
    private boolean msgKey = false;

    private void handleRegister(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_REGISTER)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }

                String mainInfo = response.body().string();

                Map responseInfo;
                JsonObject jsonObject = new JsonParser().parse(mainInfo).getAsJsonObject();
                responseInfo = myPack.fromJson(jsonObject, Map.class);

                PublicData.setFeedback((String)responseInfo.get("ErrorInfo"));
                msgKey = true;
                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });

//        OkHttp.postAsync(URL_ROOT + USER_REGISTER, map, new OkHttp.DataCallBack() {
//            @Override
//            public void requestFailure(Request request, IOException e) {
//                Log.d(TAG, "onFailure: " + e.getMessage());
//            }
//            @Override
//            public void requestSuccess(String result) {
//                Log.i("上传成功", result);
//            }
//        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        //From others activity
        final Intent intent = getIntent();
        face_user_chosen = intent.getIntExtra("face_id", 0);

        //bind edit text
        etPwdEmail      = findViewById(R.id.et_regEmail);
        etRegName       = findViewById(R.id.et_regName);
        etRegPhone      = findViewById(R.id.et_regPhone);
        etRegPwd1       = findViewById(R.id.et_regPwd_1);
        etRegPwd2       = findViewById(R.id.et_regPwd_2);
        etDescription   = findViewById(R.id.et_regDescription);

        //bind image view
        iv_img          = findViewById(R.id.temp_face);

        //refresh icon
        iv_img.setImageResource(faces[face_user_chosen]);

        //bind button
        btnBackToMenu   = findViewById(R.id.btn_backToMenu);
        btnRegConfirm   = findViewById(R.id.btn_regConfirm);
        btnSetFace      = findViewById(R.id.btn_setFace);

        //bind listener
        btnBackToMenu.setOnClickListener(this);
        btnRegConfirm.setOnClickListener(this);
        btnSetFace.setOnClickListener(this);

        //refresh edit view info
        etRegName.setText(PublicData.getRegNameTemp());
        etRegPwd1.setText(PublicData.getRegPwd1Tmp());
        etRegPwd2.setText(PublicData.getRegPwd2Temp());
        etRegPhone.setText(PublicData.getRegPhoneTemp());
        etPwdEmail.setText(PublicData.getRegEmailTemp());
        etDescription.setText(PublicData.getRegDescription());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.btn_regConfirm:
                String regName          = etRegName.getText().toString();
                String regPwd1          = etRegPwd1.getText().toString();
                String regPwd2          = etRegPwd2.getText().toString();
                String regPhone         = etRegPhone.getText().toString();
                String regEmail         = etPwdEmail.getText().toString();
                String regDescription   = etDescription.getText().toString();

                //regex
                String phoneCheck = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
                Pattern phone = Pattern.compile(phoneCheck);
                String emailCheck = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern email = Pattern.compile(emailCheck);

                if(face_user_chosen == 0) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请选择头像", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(regName.equals("")) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(regPwd1.equals("")) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(regPwd2.equals("")) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请输入第二次密码", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(!regPwd1.equals(regPwd2)) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(regPhone.length() != 11 || !phone.matcher(regPhone).matches()) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请输入正确格式的手机号", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                } else if(!email.matcher(regEmail).matches()) {
                    Toast errorMsg = Toast.makeText(RegisterActivity.this, "请输入正确格式的邮箱", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errorMsg.show();
                    break;
                }

                Map regInfo = new HashMap();
                regInfo.put("regName", regName);
                regInfo.put("regPwd", regPwd1);
                regInfo.put("regPhone", regPhone);
                regInfo.put("regEmail", regEmail);
                regInfo.put("regDescription", regDescription);
                regInfo.put("regFaceId", face_user_chosen);
                handleRegister(regInfo);

                PublicData.setRegNameTemp(null);
                PublicData.setRegPwd1Tmp(null);
                PublicData.setRegPwd2Temp(null);
                PublicData.setRegPhoneTemp(null);
                PublicData.setRegEmailTemp(null);
                PublicData.setRegDescriptionTemp(null);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                if(thisMsg == "") {
                    thisMsg = "注册成功";
                }

                Toast errMsg = Toast.makeText(RegisterActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 10);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(thisMsg.equals("注册成功")) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }

                break;
            case R.id.btn_setFace:
                //save to cache
                PublicData.setRegNameTemp(etRegName.getText().toString());
                PublicData.setRegPwd1Tmp(etRegPwd1.getText().toString());
                PublicData.setRegPwd2Temp(etRegPwd2.getText().toString());
                PublicData.setRegPhoneTemp(etRegPhone.getText().toString());
                PublicData.setRegEmailTemp(etPwdEmail.getText().toString());
                PublicData.setRegDescriptionTemp(etDescription.getText().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, FaceSelectActivity.class);
                        intent.putExtra("super", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 0);
                break;
        }
    }
}
