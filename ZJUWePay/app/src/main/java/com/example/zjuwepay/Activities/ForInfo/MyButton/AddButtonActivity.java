package com.example.zjuwepay.Activities.ForInfo.MyButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String BUTTON_ADD = "/api/button/bind";

    //components
    private ImageView ivBackToMyButton, ivNewButtonConfirm;
    private EditText etNewButtonName, etNewButtonId;

    //json helper
    private Gson myPack = new Gson();

    //async lock
    private boolean asyncKey = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_button);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMyButton    = findViewById(R.id.btn_backToMyButton);
        ivNewButtonConfirm  = findViewById(R.id.btn_newButtonConfirm);

        //bind edit text
        etNewButtonId       = findViewById(R.id.et_newButtonId);
        etNewButtonName     = findViewById(R.id.et_newButtonName);

        //bind listener
        ivBackToMyButton.setOnClickListener(this);
        ivNewButtonConfirm.setOnClickListener(this);
    }

    private void handleAddButton(Map<Object, Object> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + BUTTON_ADD)
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

                asyncKey = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMyButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AddButtonActivity.this, MyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.btn_newButtonConfirm:
                int currentId = PublicData.getCurrentUserId();
                String newButtonId = etNewButtonId.getText().toString();
                String newButtonName = etNewButtonName.getText().toString();

                Map buttonInfo = new HashMap();
                buttonInfo.put("curId", currentId);
                buttonInfo.put("buttonId", newButtonId);
                buttonInfo.put("buttonName", newButtonName);

                handleAddButton(buttonInfo);

                while(!asyncKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                String result;
                if(thisMsg == "") {
                    result = "添加成功";
                } else {
                    //TODO
                    result = thisMsg;
                }


                Toast errMsg = Toast.makeText(AddButtonActivity.this, result, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                asyncKey = false;

                if(thisMsg == "") {
                    Intent intent = new Intent(AddButtonActivity.this, MyButtonActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
        }
    }
}
