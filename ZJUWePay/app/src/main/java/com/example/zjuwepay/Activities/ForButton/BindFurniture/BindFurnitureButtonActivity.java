package com.example.zjuwepay.Activities.ForButton.BindFurniture;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.ForButton.BindButtonMainActivity;
import com.example.zjuwepay.Activities.ForButton.ChooseAction.ChooseButtonActivity;
import com.example.zjuwepay.Activities.ForButton.ChooseAction.ChooseFurnitureActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
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

public class BindFurnitureButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String BIND_FURNITURE = "/api/button/bind/furniture";

    //components
    private ImageView ivBackToButtonMain, ivThisFurnitureTypeImg;
    private TextView tvGoChooseButton, tvGoChooseFurniture, tvThisButtonName, tvThisButtonId, tvThisFurnitureName, tvThisFurnitureId;
    private LinearLayout llBindConfirm;

    //async control
    private boolean msgKey = false;

    //json helper
    private Gson myPack = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_furniture_button);
        init();
    }

    private void init() {
        //bind image view
        ivBackToButtonMain      = findViewById(R.id.btn_backToButtonBindMain);
        ivThisFurnitureTypeImg  = findViewById(R.id.iv_thisFurnitureType);

        //bind text view
        tvGoChooseButton        = findViewById(R.id.tv_chooseButton);
        tvGoChooseFurniture     = findViewById(R.id.tv_chooseFurniture);
        tvThisButtonName        = findViewById(R.id.tv_thisButtonName);
        tvThisButtonId          = findViewById(R.id.tv_thisButtonId);
        tvThisFurnitureName     = findViewById(R.id.tv_thisFurnitureName);
        tvThisFurnitureId       = findViewById(R.id.tv_thisFurnitureId);

        //bind linear layout
        llBindConfirm           = findViewById(R.id.ll_BindFurnitureConfirm);

        //set texts
        tvThisButtonName.setText(PublicData.getButtonNameTemp());
        String str_button = PublicData.getButtonIdTemp();
        if(!str_button.equals("未选择按钮")) {
            str_button = "B" + str_button.substring(0,3) + "-" + str_button.substring(3);
        }
        tvThisButtonId.setText(str_button);

        tvThisFurnitureName.setText(PublicData.getFurnitureNameTemp());
        String str_furniture = PublicData.getFurnitureIdTemp();
        if(!str_furniture.equals("未选择家具")) {
            str_furniture = "F" + str_furniture.substring(0,3) + "-" + str_furniture.substring(3);
        }
        tvThisFurnitureId.setText(str_furniture);

        //set image
        switch (PublicData.getFurnitureTypeTemp()) {
            case "0":
                ivThisFurnitureTypeImg.setImageResource(R.drawable.unknown);
                break;
            case "1":
                ivThisFurnitureTypeImg.setImageResource(R.drawable.furniture);
                break;
            case "2":
                ivThisFurnitureTypeImg.setImageResource(R.drawable.tv);
                break;
            case "3":
                ivThisFurnitureTypeImg.setImageResource(R.drawable.curtain);
                break;
        }


        //bind listener
        ivBackToButtonMain.setOnClickListener(this);
        tvGoChooseButton.setOnClickListener(this);
        tvGoChooseFurniture.setOnClickListener(this);
        llBindConfirm.setOnClickListener(this);
    }

    private void handleFurnBind(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + BIND_FURNITURE)
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToButtonBindMain:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindFurnitureButtonActivity.this, BindButtonMainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.tv_chooseButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindFurnitureButtonActivity.this, ChooseButtonActivity.class);
                        intent.putExtra("fromPage", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.tv_chooseFurniture:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindFurnitureButtonActivity.this, ChooseFurnitureActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.ll_BindFurnitureConfirm:
                int curId = PublicData.getCurrentUserId();
                String buttonId = PublicData.getButtonIdTemp();
                String furnitureId = PublicData.getFurnitureIdTemp();

                Map bindInfo = new HashMap();
                bindInfo.put("curId", curId);
                bindInfo.put("buttonId", buttonId);
                bindInfo.put("furnId", furnitureId);

                handleFurnBind(bindInfo);

                PublicData.setButtonIdTemp("未选择按钮");
                PublicData.setButtonNameTemp("未选择按钮");
                PublicData.setFurnitureIdTemp("未选择家具");
                PublicData.setFurnitureNameTemp("未选择家具");
                PublicData.setFurnitureTypeTemp("0");

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                String result;
                if(thisMsg == "") {
                    result = "绑定成功";
                } else {
                    //TODO
                    result = thisMsg;
                }


                Toast errMsg = Toast.makeText(BindFurnitureButtonActivity.this, result, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(thisMsg == "") {
                    Intent intent = new Intent(BindFurnitureButtonActivity.this, MyButtonActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
                break;
        }
    }
}
