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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.MainActivity;
import com.example.zjuwepay.Components.MyImageView;
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

public class SettingActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_UPDATE_NAME = "/api/user/update/name";
    private static final String USER_UPDATE_FACE = "/api/user/update/faceId";
    private static final String USER_UPDATE_PWD = "/api/user/update/pwd";
    private static final String USER_UPDATE_DES = "/api/user/update/description";

    //async control
    private boolean msgKey = false;

    private boolean has_changed_pwd = false;

    private ImageView btnBack, ivNameConfirm, ivPwdConfirm, ivDesConfirm;
    private MyImageView iv_img;
    private LinearLayout btnSetFace;
    private EditText etNewName, etOldPwd, etNewPwd1, etNewPwd2, etNewDes;

    private Gson myPack = new Gson();

    //setting from other activity
    private int face_user_chosen = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        //From others activity
        final Intent intent = getIntent();
        face_user_chosen = intent.getIntExtra("face_id", 0);
        if(face_user_chosen != 0) {
            handleSettingFace();
            while(!msgKey) {
                SystemClock.sleep(10);
                System.out.println("tick");
            }

            String thisMsg = PublicData.getFeedback();
            if(thisMsg == "true") {
                thisMsg = "修改头像成功";
                PublicData.setUserFace(face_user_chosen);
            } else {
                thisMsg = "修改头像失败";
            }

            Toast errMsg = Toast.makeText(SettingActivity.this, thisMsg, Toast.LENGTH_LONG);
            errMsg.setGravity(Gravity.BOTTOM, 0, 120);
            errMsg.show();

            SystemClock.sleep(1000);

            msgKey = false;
        }

        //bind button
        btnSetFace      = findViewById(R.id.btn_setFace);
        btnBack         = findViewById(R.id.btn_backToMenu);

        //bind edit text
        etNewName       = findViewById(R.id.et_newName);
        etOldPwd        = findViewById(R.id.et_oldPwd);
        etNewPwd1       = findViewById(R.id.et_newPwd1);
        etNewPwd2       = findViewById(R.id.et_newPwd2);
        etNewDes        = findViewById(R.id.et_newDescription);

        //bind image view
        iv_img = findViewById(R.id.user_face);
        if(face_user_chosen == 0) {
            iv_img.setImageResource(faces[PublicData.getUserFace()]);
        } else {
            iv_img.setImageResource(faces[face_user_chosen]);
        }
        ivNameConfirm = findViewById(R.id.btn_setNameConfirm);
        ivPwdConfirm = findViewById(R.id.btn_setPwdConfirm);
        ivDesConfirm = findViewById(R.id.btn_setDesConfirm);

        //refresh edit view info
        etNewName.setText(PublicData.getSettingName());
        etOldPwd.setText(PublicData.getSettingPwdOld());
        etNewPwd1.setText(PublicData.getSettingPwd1());
        etNewPwd2.setText(PublicData.getSettingPwd2());
        etNewDes.setText(PublicData.getSettingDes());

        //bind listener
        btnBack.setOnClickListener(this);
        btnSetFace.setOnClickListener(this);
        ivNameConfirm.setOnClickListener(this);
        ivPwdConfirm.setOnClickListener(this);
        ivDesConfirm.setOnClickListener(this);
    }

    private void handleSettingFace() {
        Map map = new HashMap<>();
        map.put("curId", PublicData.getCurrentUserId());
        map.put("newFaceId", face_user_chosen);
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(URL_ROOT + USER_UPDATE_FACE)
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

                PublicData.setFeedback(responseInfo.get("IsSuccess").toString());
                msgKey = true;
                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void handleSettingName(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(URL_ROOT + USER_UPDATE_NAME)
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

                PublicData.setFeedback(responseInfo.get("IsSuccess").toString());
                msgKey = true;
                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void handleSettingPwd(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(URL_ROOT + USER_UPDATE_PWD)
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

                PublicData.setFeedback(responseInfo.get("IsSuccess").toString());
                msgKey = true;
                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void handleSettingDes(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(URL_ROOT + USER_UPDATE_DES)
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

                PublicData.setFeedback(responseInfo.get("IsSuccess").toString());
                msgKey = true;
                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                if(has_changed_pwd) {
                    //reset current user info
                    PublicData.setLogState(false);
                    PublicData.setCurrentUserName(null);
                    PublicData.setCurrentUserDescription(null);
                    PublicData.setUserFace(0);
                    PublicData.setCurrentUserId(0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                        }
                    }, 30);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                            intent.putExtra("keepOpen", 1);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                        }
                    }, 30);
                }
                break;
            case R.id.btn_setFace:
                //save to cache
                PublicData.setSettingName(etNewName.getText().toString());
                PublicData.setSettingPwdOld(etOldPwd.getText().toString());
                PublicData.setSettingPwd1(etNewPwd1.getText().toString());
                PublicData.setSettingPwd2(etNewPwd2.getText().toString());
                PublicData.setSettingDes(etNewDes.getText().toString());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SettingActivity.this, FaceSelectActivity.class);
                        intent.putExtra("super", 2);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 0);
                break;
            case R.id.btn_setPwdConfirm:

                String newPwd1 = etNewPwd1.getText().toString();
                String newPwd2 = etNewPwd2.getText().toString();
                String oldPwd = etOldPwd.getText().toString();

                if(newPwd1.equals("")) {
                    Toast errorMsg = Toast.makeText(SettingActivity.this, "新密码不能为空", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                } else if(!newPwd1.equals(newPwd2)) {
                    Toast errorMsg = Toast.makeText(SettingActivity.this, "两次密码不一致", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                } else if(oldPwd.equals("")) {
                    Toast errorMsg = Toast.makeText(SettingActivity.this, "请输入原密码", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                }

                Map setInfo = new HashMap();
                setInfo.put("curId", PublicData.getCurrentUserId());
                setInfo.put("oldPwd", oldPwd);
                setInfo.put("newPwd", newPwd1);
                handleSettingPwd(setInfo);

                PublicData.setSettingPwdOld(null);
                PublicData.setSettingPwd1(null);
                PublicData.setSettingPwd2(null);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                if(thisMsg == "true") {
                    thisMsg = "修改密码成功";
                    has_changed_pwd = true;
                } else {
                    thisMsg = "修改密码失败,请回忆正确的原密码";
                }

                Toast errMsg = Toast.makeText(SettingActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 120);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;
                break;
            case R.id.btn_setNameConfirm:

                String newName = etNewName.getText().toString();

                if(newName.equals("")) {
                    Toast errorMsg = Toast.makeText(SettingActivity.this, "新用户名不能为空", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                }

                setInfo = new HashMap();
                setInfo.put("curId", PublicData.getCurrentUserId());
                setInfo.put("newName", newName);
                handleSettingName(setInfo);

                PublicData.setSettingName(null);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                thisMsg = PublicData.getFeedback();
                if(thisMsg == "true") {
                    thisMsg = "修改用户名成功";
                    PublicData.setCurrentUserName(newName);
                } else {
                    thisMsg = "修改用户名失败";
                }

                errMsg = Toast.makeText(SettingActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 120);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;
                break;
            case R.id.btn_setDesConfirm:

                String newDes = etNewDes.getText().toString();

                if(newDes.equals("")) {
                    Toast errorMsg = Toast.makeText(SettingActivity.this, "新签名不能为空", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                }

                setInfo = new HashMap();
                setInfo.put("curId", PublicData.getCurrentUserId());
                setInfo.put("newDescription", newDes);
                handleSettingDes(setInfo);

                PublicData.setSettingDes(null);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                thisMsg = PublicData.getFeedback();
                if(thisMsg == "true") {
                    thisMsg = "修改签名成功";
                    PublicData.setCurrentUserDescription(newDes);
                } else {
                    thisMsg = "修改签名失败";
                }

                errMsg = Toast.makeText(SettingActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 120);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;
                break;
        }
    }
}
