package com.example.zjuwepay.Activities.ForInfo.MyHelp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
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

import static com.example.zjuwepay.PublicData.dp2px;

public class MyHelpActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_HELP_ALL = "/api/alert/list";
    private static final String HELP_HANDLE = "/api/alert/changeState";

    //config for order list
    private int listSize = 0;
    private ArrayList<Map<String, Object>> helps;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToMenu;
    private LinearLayout llAllHelps;

    //async lock
    private boolean wait_lock = false;

    //async control
    private boolean msgKey = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_help);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMenu = findViewById(R.id.btn_backToMenu);

        //bind linear layout
        llAllHelps = findViewById(R.id.ll_allHelps);

        //bind listener
        ivBackToMenu.setOnClickListener(this);

        //fetch order list
        getHelpsInfo();
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadHelpInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyHelpActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }

    private void getHelpsInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_HELP_ALL)
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

                //get button list map
                Map helpList = (Map)responseInfo.get("alertHistory");

                //get button amount
                listSize = Integer.parseInt((String)helpList.get("num"));

                //get button list
                helps = (ArrayList<Map<String, Object>>) helpList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void handleHelpCheck(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(URL_ROOT + HELP_HANDLE)
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

    private void LoadHelpInfo() {
        //build button views;
        for (int i = 0; i < listSize; i++) {
            final int index = i;
            //main layout for a order info
            LinearLayout layoutMain = new LinearLayout(MyHelpActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp2px(this, 100));
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.VERTICAL);

            //up layout
            LinearLayout layoutUp = new LinearLayout(MyHelpActivity.this);
            LinearLayout.LayoutParams layoutUpParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp2px(this, 50));
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUp.setOrientation(LinearLayout.HORIZONTAL);

            //image view for message
            ImageView msgImage = new ImageView(MyHelpActivity.this);
            LinearLayout.LayoutParams msgImageParams = new LinearLayout.LayoutParams(
                    dp2px(this, 30),
                    dp2px(this, 30));
            msgImageParams.gravity = Gravity.CENTER_VERTICAL;
            msgImageParams.leftMargin = dp2px(this, 10);
            msgImage.setLayoutParams(msgImageParams);
            msgImage.setImageResource(R.drawable.push);

            //message text
            TextView msgText = new TextView(MyHelpActivity.this);
            LinearLayout.LayoutParams msgTextParams = new LinearLayout.LayoutParams(
                    dp2px(this, 220),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            msgTextParams.gravity = Gravity.CENTER_VERTICAL;
            msgTextParams.leftMargin = dp2px(this, 10);
            msgText.setLayoutParams(msgTextParams);
            msgText.setTextSize(17);
            msgText.setText((String)helps.get(i).get("historyMessage"));

            layoutUp.addView(msgImage);
            layoutUp.addView(msgText);

            //down layout
            LinearLayout layoutDown = new LinearLayout(MyHelpActivity.this);
            LinearLayout.LayoutParams layoutDownParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp2px(this, 50));
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDown.setOrientation(LinearLayout.HORIZONTAL);

            //image view for date
            ImageView dateImage = new ImageView(MyHelpActivity.this);
            LinearLayout.LayoutParams dateImageParams = new LinearLayout.LayoutParams(
                    dp2px(this, 30),
                    dp2px(this, 30));
            dateImageParams.gravity = Gravity.CENTER_VERTICAL;
            dateImageParams.leftMargin = dp2px(this, 10);
            dateImage.setLayoutParams(dateImageParams);
            dateImage.setImageResource(R.drawable.watch);

            //date text
            TextView dateText = new TextView(MyHelpActivity.this);
            LinearLayout.LayoutParams dateTextParams = new LinearLayout.LayoutParams(
                    dp2px(this, 230),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dateTextParams.gravity = Gravity.CENTER_VERTICAL;
            dateTextParams.leftMargin = dp2px(this, 10);
            dateText.setLayoutParams(dateTextParams);
            dateText.setTextSize(15);
            dateText.setText("发送时间: " + helps.get(i).get("historyDate"));

            //state text
            TextView checkText = new TextView(MyHelpActivity.this);
            LinearLayout.LayoutParams checkTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            checkTextParams.gravity = Gravity.CENTER_VERTICAL;
            if(helps.get(i).get("isConfirmed").toString().equals("false")) {
                checkTextParams.leftMargin = dp2px(this, 10);
                checkText.setLayoutParams(checkTextParams);
                checkText.setTextSize(15);
                checkText.setBackgroundResource(R.drawable.rounded_rec_confirm_shape);
                checkText.setText("确认处理");
                checkText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map helpInfo = new HashMap();
                        helpInfo.put("curId", PublicData.getCurrentUserId());
                        helpInfo.put("historyId", helps.get(index).get("historyId"));
                        handleHelpCheck(helpInfo);

                        while(!msgKey) {
                            SystemClock.sleep(10);
                            System.out.println("tick");
                        }

                        String thisMsg = PublicData.getFeedback();
                        if(thisMsg == "true") {
                            thisMsg = "更新成功";
                        } else {
                            thisMsg = "网络错误";
                        }

                        Toast errMsg = Toast.makeText(MyHelpActivity.this, thisMsg, Toast.LENGTH_LONG);
                        errMsg.setGravity(Gravity.BOTTOM, 0, 120);
                        errMsg.show();

                        SystemClock.sleep(1000);

                        msgKey = false;

                        if(thisMsg.equals("更新成功")) {
                            Intent intent = new Intent(MyHelpActivity.this, MyHelpActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }
                });
            } else {
                checkTextParams.leftMargin = dp2px(this, 30);
                checkText.setLayoutParams(checkTextParams);
                checkText.setTextSize(15);
                checkText.setText("已处理");
            }

            layoutDown.addView(dateImage);
            layoutDown.addView(dateText);
            layoutDown.addView(checkText);

            //Divider
            View line_1 = new View(MyHelpActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(MyHelpActivity.this);
            LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line2Params.height = dp2px(this, 0.5f);
            line2Params.topMargin = dp2px(this, 2.5f);
            line_2.setLayoutParams(line2Params);
            line_2.setBackgroundColor(Color.rgb(0, 0, 0));

            layoutMain.addView(layoutUp);
            layoutMain.addView(layoutDown);

            llAllHelps.addView(layoutMain);
            llAllHelps.addView(line_1);
            llAllHelps.addView(line_2);
        }
    }

}
