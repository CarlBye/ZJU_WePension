package com.example.zjuwepay.Activities.ForButton.ChooseAction;

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

import com.example.zjuwepay.Activities.ForButton.BindBuy.BindBuyButtonActivity;
import com.example.zjuwepay.Activities.ForButton.BindFurniture.BindFurnitureButtonActivity;
import com.example.zjuwepay.Activities.ForButton.BindHelp.BindHelpButtonActivity;
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

public class ChooseButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_BUTTONS_ALL = "/api/button/list";

    //config for button list
    private int listSize = 0;
    private ArrayList<Map<String, String>> buttons;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToMenu;
    private LinearLayout llAllUnbindButtons;

    //async lock
    private boolean wait_lock = false;

    //destination
    private int toPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_button);
        init();
    }

    private void init() {
        //From others activity
        final Intent intent = getIntent();
        toPage = intent.getIntExtra("fromPage", 0);

        //bind image view
        ivBackToMenu = findViewById(R.id.btn_backToFront);

        //bind linear layout
        llAllUnbindButtons = findViewById(R.id.ll_allUnbindButtons);

        //bind listener
        ivBackToMenu.setOnClickListener(this);

        getUnbindButtonsInfo();
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadUnbindButtonInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToFront:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(toPage == 1) {
                            Intent intent = new Intent(ChooseButtonActivity.this, BindFurnitureButtonActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        } else if(toPage == 2) {
                            Intent intent = new Intent(ChooseButtonActivity.this, BindBuyButtonActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        } else if(toPage == 3) {
                            Intent intent = new Intent(ChooseButtonActivity.this, BindHelpButtonActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        }

                    }
                }, 30);
                break;
        }
    }

    private void getUnbindButtonsInfo() {
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

    private void LoadUnbindButtonInfo() {
        //build button views;
        for (int i = 0; i < listSize; i++) {
            final int index = i;
            //filter
//            if(!buttons.get(i).get("buttonType").equals("0")) {
//                continue;
//            }
            //main layout for a button info
            LinearLayout layoutMain = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.VERTICAL);

            //inner up layout
            LinearLayout layoutUp = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutUpParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUpParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutUpParams.height = dp2px(this, 60);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUp.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for button name
            LinearLayout layoutName = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutNameParams.width = dp2px(this, 230);
            layoutNameParams.leftMargin = dp2px(this, 10);
            layoutName.setLayoutParams(layoutNameParams);
            layoutName.setOrientation(LinearLayout.HORIZONTAL);

            //image for button name
            ImageView nameImg = new ImageView(ChooseButtonActivity.this);
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
            TextView nameText = new TextView(ChooseButtonActivity.this);
            LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            nameTextParams.gravity = Gravity.CENTER_VERTICAL;
            nameTextParams.leftMargin = dp2px(this, 12);
            nameText.setLayoutParams(nameTextParams);
            nameText.setTextSize(20);
            nameText.setText(buttons.get(i).get("buttonName"));

            //inner layout for button id
            LinearLayout layoutId = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutIdParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutIdParams.leftMargin = dp2px(this, 20);
            layoutId.setLayoutParams(layoutIdParams);
            layoutId.setOrientation(LinearLayout.HORIZONTAL);

            //image for button id
            ImageView idImg = new ImageView(ChooseButtonActivity.this);
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
            TextView idText = new TextView(ChooseButtonActivity.this);
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

            //inner down layout
            LinearLayout layoutDown = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutDownParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDownParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutDownParams.height = dp2px(this, 60);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDown.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for button type
            LinearLayout layoutType = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutTypeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutTypeParams.width = dp2px(this, 220);
            layoutTypeParams.leftMargin = dp2px(this, 10);
            layoutType.setLayoutParams(layoutTypeParams);
            layoutType.setOrientation(LinearLayout.HORIZONTAL);

            //image for button type
            ImageView typeImg = new ImageView(ChooseButtonActivity.this);
            LinearLayout.LayoutParams typeImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeImgParams.width = dp2px(this, 35);
            typeImgParams.height = dp2px(this, 35);
            typeImgParams.gravity = Gravity.CENTER_VERTICAL;
            typeImg.setLayoutParams(typeImgParams);
            typeImg.setImageResource(R.drawable.type);

            //text view for button type
            TextView typeText = new TextView(ChooseButtonActivity.this);
            LinearLayout.LayoutParams typeTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeTextParams.gravity = Gravity.CENTER_VERTICAL;
            typeTextParams.leftMargin = dp2px(this, 12);
            typeText.setLayoutParams(typeTextParams);
            typeText.setTextSize(20);
            switch (buttons.get(i).get("buttonType")) {
                /*
                    0 - 无
                    1 - 购物
                    2 - 家具
                    3 - 求助
                    4 - 体征检测
                 */
                case "0":
                    typeText.setText("未绑定");
                    typeText.setTextColor(Color.rgb(0x80, 0x80, 0x80));
                    break;
                case "1":
                    typeText.setText("自动下单");
                    typeText.setTextColor(Color.rgb(0x99, 0xCC, 0xFF));
                    break;
                case "2":
                    typeText.setText("家具控制");
                    typeText.setTextColor(Color.rgb(0x66, 0xCC, 0x99));
                    break;
                case "3":
                    typeText.setText("紧急求助");
                    typeText.setTextColor(Color.rgb(0xFF, 0x30, 0x00));
                    break;
                case "4":
                    typeText.setText("体征检测");
                    typeText.setTextColor(Color.rgb(0xFF, 0xCC, 0x33));
                    break;
            }

            //inner layout for choose button
            LinearLayout layoutDetails = new LinearLayout(ChooseButtonActivity.this);
            LinearLayout.LayoutParams layoutDetailsParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutDetailsParams.width = dp2px(this, 130);
            layoutDetailsParams.height = dp2px(this, 40);
            layoutDetailsParams.leftMargin = dp2px(this, 20);
            layoutDetailsParams.gravity = Gravity.CENTER_VERTICAL;
            layoutDetails.setLayoutParams(layoutDetailsParams);
            layoutDetails.setOrientation(LinearLayout.HORIZONTAL);
            layoutDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_details_shape));

            //image for button choose
            ImageView detailsImg = new ImageView(ChooseButtonActivity.this);
            LinearLayout.LayoutParams detailsImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsImgParams.width = dp2px(this, 20);
            detailsImgParams.height = dp2px(this, 20);
            detailsImgParams.gravity = Gravity.CENTER_VERTICAL;
            detailsImg.setLayoutParams(detailsImgParams);
            detailsImg.setImageResource(R.drawable.ok);

            //text view button choose
            TextView detailsText = new TextView(ChooseButtonActivity.this);
            LinearLayout.LayoutParams detailsTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsTextParams.gravity = Gravity.CENTER_VERTICAL;
            detailsTextParams.leftMargin = dp2px(this, 10);
            detailsText.setLayoutParams(detailsTextParams);
            detailsText.setTextSize(18);
            detailsText.setText("选定按钮");

            //Divider
            View line_1 = new View(ChooseButtonActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(ChooseButtonActivity.this);
            LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line2Params.height = dp2px(this, 0.5f);
            line2Params.topMargin = dp2px(this, 2.5f);
            line_2.setLayoutParams(line2Params);
            line_2.setBackgroundColor(Color.rgb(0, 0, 0));

            //add in
            layoutName.addView(nameImg);
            layoutName.addView(nameText);

            layoutId.addView(idImg);
            layoutId.addView(idText);

            layoutType.addView(typeImg);
            layoutType.addView(typeText);

            layoutDetails.addView(detailsImg);
            layoutDetails.addView(detailsText);

            layoutUp.addView(layoutName);
            layoutUp.addView(layoutId);

            layoutDown.addView(layoutType);
            layoutDown.addView(layoutDetails);

            layoutMain.addView(layoutUp);
            layoutMain.addView(layoutDown);

            llAllUnbindButtons.addView(layoutMain);
            llAllUnbindButtons.addView(line_1);
            llAllUnbindButtons.addView(line_2);

            layoutDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String thisButtonName = buttons.get(index).get("buttonName");
                    final String thisButtonId   = buttons.get(index).get("buttonId");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(toPage == 1) {
                                Intent intent = new Intent(ChooseButtonActivity.this, BindFurnitureButtonActivity.class);
                                PublicData.setButtonIdTemp(thisButtonId);
                                PublicData.setButtonNameTemp(thisButtonName);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                            } else if(toPage == 2) {
                                Intent intent = new Intent(ChooseButtonActivity.this, BindBuyButtonActivity.class);
                                PublicData.setButtonIdTemp(thisButtonId);
                                PublicData.setButtonNameTemp(thisButtonName);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                            } else if(toPage == 3) {
                                Intent intent = new Intent(ChooseButtonActivity.this, BindHelpButtonActivity.class);
                                PublicData.setButtonIdTemp(thisButtonId);
                                PublicData.setButtonNameTemp(thisButtonName);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                            }

                        }
                    }, 30);
                }
            });
        }
    }

}
