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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.MainActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_LOGIN = "/api/user/login";

    //components
    private ImageView btnLoginConfirm, btnBackToMenu, ivUserFace;
    private TextView btnGoRegister;
    private EditText etLoginId, etLoginPwd;
    private boolean msgKey = false;

    //json helper
    private Gson myPack = new Gson();

    private void handleLogin(Map<Object, Object> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_LOGIN)
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

                if((boolean)responseInfo.get("IsSuccess")) {
                    PublicData.setLogState(true);
                    PublicData.setCurrentUserName((String)responseInfo.get("curName"));
                    PublicData.setUserFace(Integer.parseInt((String)responseInfo.get("curFaceId")));
                    PublicData.setCurrentUserId(Integer.parseInt((String)responseInfo.get("curId")));
                    PublicData.setCurrentUserDescription((String)responseInfo.get("curDescription"));
                    msgKey = true;

                } else {
                    PublicData.setLogState(false);

                    msgKey = true;
                }

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        //bind edit view
        etLoginId       = findViewById(R.id.et_loginId);
        etLoginPwd      = findViewById(R.id.et_loginPwd);

        //bind button
        btnLoginConfirm = findViewById(R.id.btn_loginConfirm);
        btnGoRegister   = findViewById(R.id.btn_register);
        btnBackToMenu   = findViewById(R.id.btn_backToMenu);

        //bind image view
        ivUserFace      = findViewById(R.id.user_face);

        //bind listener
        btnLoginConfirm.setOnClickListener(this);
        btnGoRegister.setOnClickListener(this);
        btnBackToMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loginConfirm:
                String loginId = etLoginId.getText().toString();
                String loginPwd = etLoginPwd.getText().toString();

                Map loginInfo = new HashMap();
                loginInfo.put("logName", loginId);
                loginInfo.put("logPwd", loginPwd);

                handleLogin(loginInfo);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                if(thisMsg == "") {
                    thisMsg = "登录成功";
                }

                ivUserFace.setImageResource(faces[PublicData.getUserFace()]);

                Toast errMsg = Toast.makeText(LoginActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(PublicData.getLogState()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
                break;

            case R.id.btn_register:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;

            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }
}
