package com.example.zjuwepay.Activities.ForInfo.MyButton;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class MyButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_BUTTONS_ALL = "/api/button/list";

    //config for button list
    private int listSize = 0;
    private ArrayList<Map<String, String>> buttons;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToMenu, ivAddButton;
    private LinearLayout llAllButtons;

    //async lock
    private boolean wait_lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_button);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMenu = findViewById(R.id.btn_backToMenu);
        ivAddButton  = findViewById(R.id.btn_addButton);

        //bind linear layout
        llAllButtons = findViewById(R.id.ll_allButtons);

        //bind listener
        ivBackToMenu.setOnClickListener(this);
        ivAddButton.setOnClickListener(this);

        //fetch button list
        getButtonsInfo();
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadButtonInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyButtonActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.btn_addButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyButtonActivity.this, AddButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
        }
    }

    private void getButtonsInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_BUTTONS_ALL)
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
                Map buttonList = (Map)responseInfo.get("buttonList");

                //get button amount
                listSize = Integer.parseInt((String)buttonList.get("num"));

                //get button list
                buttons = (ArrayList<Map<String, String>>) buttonList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void LoadButtonInfo() {
        //build button views;
        for (int i = 0; i < listSize; i++) {
            final int index = i;
            //main layout for a button info
            LinearLayout layoutMain = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.VERTICAL);

            //inner up layout
            LinearLayout layoutUp = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutUpParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUpParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutUpParams.height = dp2px(this, 60);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUp.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for button name
            LinearLayout layoutName = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutNameParams.width = dp2px(this, 230);
            layoutNameParams.leftMargin = dp2px(this, 10);
            layoutName.setLayoutParams(layoutNameParams);
            layoutName.setOrientation(LinearLayout.HORIZONTAL);

            //image for button name
            ImageView nameImg = new ImageView(MyButtonActivity.this);
            LinearLayout.LayoutParams nameImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMain.setLayoutParams(layoutMainParams);
            nameImgParams.width = dp2px(this, 35);
            nameImgParams.height = dp2px(this, 35);
            nameImgParams.gravity = Gravity.CENTER_VERTICAL;
            nameImg.setLayoutParams(nameImgParams);
            nameImg.setImageResource(R.drawable.tag);

            //text view for button name
            TextView nameText = new TextView(MyButtonActivity.this);
            LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            nameTextParams.gravity = Gravity.CENTER_VERTICAL;
            nameTextParams.leftMargin = dp2px(this, 12);
            nameText.setLayoutParams(nameTextParams);
            nameText.setTextSize(15);
            nameText.setText("按钮名称: \n" + buttons.get(i).get("buttonName"));

            layoutName.addView(nameImg);
            layoutName.addView(nameText);

            //inner layout for button id
            LinearLayout layoutId = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutIdParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutIdParams.leftMargin = dp2px(this, 20);
            layoutId.setLayoutParams(layoutIdParams);
            layoutId.setOrientation(LinearLayout.HORIZONTAL);

            //image for button id
            ImageView idImg = new ImageView(MyButtonActivity.this);
            LinearLayout.LayoutParams idImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            idImgParams.width = dp2px(this, 15);
            idImgParams.height = dp2px(this, 15);
            idImgParams.gravity = Gravity.CENTER_VERTICAL;
            idImgParams.bottomMargin = dp2px(this, 5);
            idImg.setLayoutParams(idImgParams);
            idImg.setImageResource(R.drawable.info);

            //text view for button id
            TextView idText = new TextView(MyButtonActivity.this);
            LinearLayout.LayoutParams idTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            idTextParams.gravity = Gravity.CENTER_VERTICAL;
            idTextParams.leftMargin = dp2px(this, 5);
            idTextParams.bottomMargin = dp2px(this, 5);
            idText.setLayoutParams(idTextParams);
            idText.setTextSize(13);
            String str = String.format("%08d", Integer.parseInt(buttons.get(i).get("buttonId")));
            str = "B" + str.substring(0,3) + "-" + str.substring(3);
            idText.setText(str);

            layoutId.addView(idImg);
            layoutId.addView(idText);

            layoutUp.addView(layoutName);
            layoutUp.addView(layoutId);

            //inner down layout
            LinearLayout layoutDown = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutDownParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDownParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutDownParams.height = dp2px(this, 60);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDown.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for button type
            LinearLayout layoutType = new LinearLayout(MyButtonActivity.this);
            LinearLayout.LayoutParams layoutTypeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutTypeParams.width = dp2px(this, 220);
            layoutTypeParams.leftMargin = dp2px(this, 10);
            layoutType.setLayoutParams(layoutTypeParams);
            layoutType.setOrientation(LinearLayout.HORIZONTAL);

            //image for button type
            ImageView typeImg = new ImageView(MyButtonActivity.this);
            LinearLayout.LayoutParams typeImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeImgParams.width = dp2px(this, 25);
            typeImgParams.height = dp2px(this, 25);
            typeImgParams.gravity = Gravity.CENTER_VERTICAL;
            typeImg.setLayoutParams(typeImgParams);
            typeImg.setImageResource(R.drawable.type);

            //text view for button type
            TextView typeText = new TextView(MyButtonActivity.this);
            LinearLayout.LayoutParams typeTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeTextParams.gravity = Gravity.CENTER_VERTICAL;
            typeTextParams.leftMargin = dp2px(this, 12);
            typeText.setLayoutParams(typeTextParams);
            typeText.setTextSize(15);
            switch (buttons.get(i).get("buttonType")) {
                /*
                    0 - 无
                    1 - 购物
                    2 - 家具
                    3 - 求助
                    4 - 体征检测
                 */
                case "0":
                    typeText.setText("丨未绑定");
                    typeText.setTextColor(Color.rgb(0x80, 0x80, 0x80));
                    break;
                case "1":
                    typeText.setText("丨自动下单");
                    typeText.setTextColor(Color.rgb(0x99, 0xCC, 0xFF));
                    break;
                case "2":
                    typeText.setText("丨家具控制");
                    typeText.setTextColor(Color.rgb(0x66, 0xCC, 0x99));
                    break;
                case "3":
                    typeText.setText("丨紧急求助");
                    typeText.setTextColor(Color.rgb(0xFF, 0x30, 0x00));
                    break;
                case "4":
                    typeText.setText("丨体征检测");
                    typeText.setTextColor(Color.rgb(0xFF, 0xCC, 0x33));
                    break;
            }

            layoutType.addView(typeImg);
            layoutType.addView(typeText);

            layoutDown.addView(layoutType);

            if(buttons.get(i).get("buttonType").equals("1") || buttons.get(i).get("buttonType").equals("2")) {
                //inner layout for button details
                LinearLayout layoutDetails = new LinearLayout(MyButtonActivity.this);
                LinearLayout.LayoutParams layoutDetailsParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                layoutDetailsParams.width = dp2px(this, 130);
                layoutDetailsParams.height = dp2px(this, 40);
                layoutDetailsParams.leftMargin = dp2px(this, 20);
                layoutDetailsParams.topMargin = dp2px(this, 5);
                layoutDetailsParams.gravity = Gravity.CENTER_VERTICAL;
                layoutDetails.setLayoutParams(layoutDetailsParams);
                layoutDetails.setOrientation(LinearLayout.HORIZONTAL);
//                layoutDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_details_shape));

                //image for details button
                ImageView detailsImg = new ImageView(MyButtonActivity.this);
                LinearLayout.LayoutParams detailsImgParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                detailsImgParams.width = dp2px(this, 20);
                detailsImgParams.height = dp2px(this, 20);
                detailsImgParams.gravity = Gravity.CENTER_VERTICAL;
                detailsImg.setLayoutParams(detailsImgParams);
                detailsImg.setImageResource(R.drawable.fast);

                //text view details button
                TextView detailsText = new TextView(MyButtonActivity.this);
                LinearLayout.LayoutParams detailsTextParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                detailsTextParams.gravity = Gravity.CENTER_VERTICAL;
                detailsTextParams.leftMargin = dp2px(this, 10);
                detailsText.setLayoutParams(detailsTextParams);
                detailsText.setTextSize(18);
                detailsText.setText("查看详情");

                layoutDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //家具
                        if(buttons.get(index).get("buttonType").equals("2")) {
                            PublicData.setFurnDetailButtonId(buttons.get(index).get("buttonId"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MyButtonActivity.this, FurnDetailActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                                }
                            }, 30);
                        } else if(buttons.get(index).get("buttonType").equals("1")) {
                            PublicData.setBuyDetailButtonId(buttons.get(index).get("buttonId"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MyButtonActivity.this, BuyDetailActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                                }
                            }, 30);
                        }
                    }
                });

                layoutDetails.addView(detailsImg);
                layoutDetails.addView(detailsText);

                layoutDown.addView(layoutDetails);
            }

            //Divider
            View line_1 = new View(MyButtonActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(MyButtonActivity.this);
            LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line2Params.height = dp2px(this, 0.5f);
            line2Params.topMargin = dp2px(this, 2.5f);
            line_2.setLayoutParams(line2Params);
            line_2.setBackgroundColor(Color.rgb(0, 0, 0));

            layoutMain.addView(layoutUp);
            layoutMain.addView(layoutDown);

            llAllButtons.addView(layoutMain);
            llAllButtons.addView(line_1);
            llAllButtons.addView(line_2);
        }
    }
}
