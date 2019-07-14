package com.example.zjuwepay.Activities.ForInfo.MyFurniture;

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

public class MyFurnitureActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_FURNITURE_ALL = "/api/furniture/list";

    //config for furniture list
    private int listSize = 0;
    private ArrayList<Map<String, String>> furniture;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToMenu, ivAddFurniture;
    private LinearLayout llAllFurniture;

    //async lock
    private boolean wait_lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_furniture);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMenu = findViewById(R.id.btn_backToMenu);
        ivAddFurniture  = findViewById(R.id.btn_addFurniture);

        //bind linear layout
        llAllFurniture = findViewById(R.id.ll_allFurniture);

        //bind listener
        ivBackToMenu.setOnClickListener(this);
        ivAddFurniture.setOnClickListener(this);

        //fetch furniture list
        getFurnitureInfo();
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadFurnitureInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyFurnitureActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.btn_addFurniture:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyFurnitureActivity.this, AddFurnitureActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
        }
    }

    private void getFurnitureInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_FURNITURE_ALL)
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
                Map furnitureList = (Map)responseInfo.get("furnList");

                //get button amount
                listSize = Integer.parseInt((String)furnitureList.get("num"));

                //get button list
                furniture = (ArrayList<Map<String, String>>) furnitureList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void LoadFurnitureInfo() {
        //build furniture views
        for (int i = 0; i < listSize; i++) {
            //main layout for a button info
            LinearLayout layoutMain = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.VERTICAL);

            //inner up layout
            LinearLayout layoutUp = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutUpParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUpParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutUpParams.height = dp2px(this, 60);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUp.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for furniture name
            LinearLayout layoutName = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutNameParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutNameParams.width = dp2px(this, 230);
            layoutNameParams.leftMargin = dp2px(this, 10);
            layoutName.setLayoutParams(layoutNameParams);
            layoutName.setOrientation(LinearLayout.HORIZONTAL);

            //image for furniture name
            ImageView nameImg = new ImageView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams nameImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMain.setLayoutParams(layoutMainParams);
            nameImgParams.width = dp2px(this, 35);
            nameImgParams.height = dp2px(this, 35);
            nameImgParams.gravity = Gravity.CENTER_VERTICAL;
            nameImg.setLayoutParams(nameImgParams);
            nameImg.setImageResource(R.drawable.tag);

            //text view for furniture name
            TextView nameText = new TextView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            nameTextParams.gravity = Gravity.CENTER_VERTICAL;
            nameTextParams.leftMargin = dp2px(this, 12);
            nameText.setLayoutParams(nameTextParams);
            nameText.setTextSize(20);
            nameText.setText(furniture.get(i).get("furnName"));

            //inner layout for furniture id
            LinearLayout layoutId = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutIdParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutIdParams.leftMargin = dp2px(this, 20);
            layoutId.setLayoutParams(layoutIdParams);
            layoutId.setOrientation(LinearLayout.HORIZONTAL);

            //image for furniture id
            ImageView idImg = new ImageView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams idImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            idImgParams.width = dp2px(this, 15);
            idImgParams.height = dp2px(this, 15);
            idImgParams.gravity = Gravity.CENTER_VERTICAL;
            idImgParams.bottomMargin = dp2px(this, 5);
            idImg.setLayoutParams(idImgParams);
            idImg.setImageResource(R.drawable.info);

            //text view for furniture id
            TextView idText = new TextView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams idTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            idTextParams.gravity = Gravity.CENTER_VERTICAL;
            idTextParams.leftMargin = dp2px(this, 5);
            idTextParams.bottomMargin = dp2px(this, 5);
            idText.setLayoutParams(idTextParams);
            idText.setTextSize(13);
            String str = String.format("%08d", Integer.parseInt(furniture.get(i).get("furnId")));
            str = "F" + str.substring(0,3) + "-" + str.substring(3);
            idText.setText(str);

            //inner down layout
            LinearLayout layoutDown = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutDownParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDownParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutDownParams.height = dp2px(this, 60);
            layoutDown.setLayoutParams(layoutDownParams);
            layoutDown.setOrientation(LinearLayout.HORIZONTAL);

            //inner layout for furniture type
            LinearLayout layoutType = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutTypeParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutTypeParams.width = dp2px(this, 230);
            layoutTypeParams.leftMargin = dp2px(this, 10);
            layoutType.setLayoutParams(layoutTypeParams);
            layoutType.setOrientation(LinearLayout.HORIZONTAL);

            //image for furniture type
            ImageView typeImg = new ImageView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams typeImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeImgParams.width = dp2px(this, 35);
            typeImgParams.height = dp2px(this, 35);
            typeImgParams.gravity = Gravity.CENTER_VERTICAL;
            typeImg.setLayoutParams(typeImgParams);
            /*
                0 - 无
                1 - 灯
                2 - 电视
                3 - 窗帘
             */
            switch(furniture.get(i).get("furnType")) {
                case "0":
                    typeImg.setImageResource(R.drawable.unknown);
                    break;
                case "1":
                    typeImg.setImageResource(R.drawable.furniture);
                    break;
                case "2":
                    typeImg.setImageResource(R.drawable.tv);
                    break;
                case "3":
                    typeImg.setImageResource(R.drawable.curtain);
                    break;
            }

            //text view for furniture type
            TextView typeText = new TextView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams typeTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeTextParams.gravity = Gravity.CENTER_VERTICAL;
            typeTextParams.leftMargin = dp2px(this, 12);
            typeText.setLayoutParams(typeTextParams);
            typeText.setTextSize(20);
            /*
                0 - 无
                1 - 灯
                2 - 电视
                3 - 窗帘
             */
            switch(furniture.get(i).get("furnType")) {
                case "0":
                    typeText.setText("未知类型");
                    break;
                case "1":
                    typeText.setText("灯具");
                    break;
                case "2":
                    typeText.setText("电视");
                    break;
                case "3":
                    typeText.setText("窗帘");
                    break;
            }


            //inner layout for furniture state
            LinearLayout layoutDetails = new LinearLayout(MyFurnitureActivity.this);
            LinearLayout.LayoutParams layoutDetailsParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutDetailsParams.width = dp2px(this, 130);
            layoutDetailsParams.height = dp2px(this, 40);
            layoutDetailsParams.leftMargin = dp2px(this, 20);
            layoutDetailsParams.gravity = Gravity.CENTER_VERTICAL;
            layoutDetails.setLayoutParams(layoutDetailsParams);
            layoutDetails.setOrientation(LinearLayout.HORIZONTAL);

            switch(furniture.get(i).get("furnState")) {
                /*
                    0 - 关
                    1 - 开
                 */
                case "0":
                    layoutDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_state_false_shape));
                    break;
                case "1":
                    layoutDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_state_true_shape));
                    break;
            }

            //image for furniture state
            ImageView detailsImg = new ImageView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams detailsImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsImgParams.width = dp2px(this, 20);
            detailsImgParams.height = dp2px(this, 20);
            detailsImgParams.gravity = Gravity.CENTER_VERTICAL;
            detailsImg.setLayoutParams(detailsImgParams);

            switch(furniture.get(i).get("furnState")) {
                /*
                    0 - 关
                    1 - 开
                 */
                case "0":
                    detailsImg.setImageResource(R.drawable.red_circle);
                    break;
                case "1":
                    detailsImg.setImageResource(R.drawable.green_circle);
                    break;
            }

            //text view furniture state
            TextView detailsText = new TextView(MyFurnitureActivity.this);
            LinearLayout.LayoutParams detailsTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsTextParams.gravity = Gravity.CENTER_VERTICAL;
            detailsTextParams.leftMargin = dp2px(this, 10);
            detailsText.setLayoutParams(detailsTextParams);
            detailsText.setTextSize(18);

            switch(furniture.get(i).get("furnState")) {
                /*
                    0 - 关
                    1 - 开
                 */
                case "0":
                    detailsText.setText("关闭状态");
                    break;
                case "1":
                    detailsText.setText("开启状态");
                    break;
            }

            //Divider
            View line_1 = new View(MyFurnitureActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(MyFurnitureActivity.this);
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

            llAllFurniture.addView(layoutMain);
            llAllFurniture.addView(line_1);
            llAllFurniture.addView(line_2);
        }
    }
}
