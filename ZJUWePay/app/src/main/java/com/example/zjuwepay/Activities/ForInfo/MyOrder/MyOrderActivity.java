package com.example.zjuwepay.Activities.ForInfo.MyOrder;

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

public class MyOrderActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_ORDERS_ALL = "/api/commodity/orderList";

    //config for order list
    private int listSize = 0;
    private ArrayList<Map<String, String>> orders;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToMenu;
    private LinearLayout llAllOrders;

    //async lock
    private boolean wait_lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMenu = findViewById(R.id.btn_backToMenu);

        //bind linear layout
        llAllOrders  = findViewById(R.id.ll_allOrders);

        //bind listener
        ivBackToMenu.setOnClickListener(this);

        //fetch order list
        getOrdersInfo();
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadOrderInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }

    private void getOrdersInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + USER_ORDERS_ALL)
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
                Map orderList = (Map)responseInfo.get("orderList");

                //get button amount
                listSize = Integer.parseInt((String)orderList.get("num"));

                //get button list
                orders = (ArrayList<Map<String, String>>) orderList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void LoadOrderInfo() {
        //build button views;
        for (int i = 0; i < listSize; i++) {
            final int index = i;
            //main layout for a order info
            LinearLayout layoutMain = new LinearLayout(MyOrderActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp2px(this, 100));
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.VERTICAL);

            //text view for order name
            TextView nameText = new TextView(MyOrderActivity.this);
            LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    dp2px(this, 40));
            nameTextParams.leftMargin = dp2px(this, 10);
            nameTextParams.topMargin = dp2px(this, 10);
            nameText.setLayoutParams(nameTextParams);
            nameText.setTextSize(25);
            nameText.setSingleLine(true);
            nameText.setText(orders.get(i).get("comName"));

            //inner layout
            LinearLayout layoutUp = new LinearLayout(MyOrderActivity.this);
            LinearLayout.LayoutParams layoutUpParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutUp.setLayoutParams(layoutUpParams);
            layoutUp.setOrientation(LinearLayout.HORIZONTAL);

            //text view for order state
            TextView stateText = new TextView(MyOrderActivity.this);
            LinearLayout.LayoutParams stateTextParams = new LinearLayout.LayoutParams(
                    dp2px(this, 220),
                    dp2px(this, 40));
            stateTextParams.leftMargin = dp2px(this, 10);
            stateTextParams.topMargin = dp2px(this, 10);
            stateText.setLayoutParams(stateTextParams);
            stateText.setTextSize(25);
            switch (orders.get(i).get("orderState")) {
                case "0":
                    stateText.setTextColor(Color.rgb(0x91, 0x91, 0x91));
                    stateText.setText("未知");
                    break;
                case "1":
                    stateText.setTextColor(Color.rgb(0xff, 0x33, 0x00));
                    stateText.setText("待确认");
                    break;
                case "2":
                    stateText.setTextColor(Color.rgb(0xff, 0x99, 0x33));
                    stateText.setText("运送中");
                    break;
                case "3":
                    stateText.setTextColor(Color.rgb(0x33, 0x99, 0xff));
                    stateText.setText("签收");
                    break;
                case "4":
                    stateText.setTextColor(Color.rgb(0x66, 0xcc, 0x66));
                    stateText.setText("完成");
                    break;
            }

            //inner layout for order details
            LinearLayout layoutDetails = new LinearLayout(MyOrderActivity.this);
            LinearLayout.LayoutParams layoutDetailsParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutDetailsParams.width = dp2px(this, 130);
            layoutDetailsParams.height = dp2px(this, 40);
            layoutDetailsParams.leftMargin = dp2px(this, 0);
            layoutDetailsParams.gravity = Gravity.CENTER_VERTICAL;
            layoutDetails.setLayoutParams(layoutDetailsParams);
            layoutDetails.setOrientation(LinearLayout.HORIZONTAL);
            layoutDetails.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_details_shape));

            //image for details button
            ImageView detailsImg = new ImageView(MyOrderActivity.this);
            LinearLayout.LayoutParams detailsImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsImgParams.width = dp2px(this, 20);
            detailsImgParams.height = dp2px(this, 20);
            detailsImgParams.gravity = Gravity.CENTER_VERTICAL;
            detailsImg.setLayoutParams(detailsImgParams);
            detailsImg.setImageResource(R.drawable.right_circular);

            //text view details button
            TextView detailsText = new TextView(MyOrderActivity.this);
            LinearLayout.LayoutParams detailsTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            detailsTextParams.gravity = Gravity.CENTER_VERTICAL;
            detailsTextParams.leftMargin = dp2px(this, 10);
            detailsText.setLayoutParams(detailsTextParams);
            detailsText.setTextSize(18);
            detailsText.setText("查看详情");

            //Divider
            View line_1 = new View(MyOrderActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(MyOrderActivity.this);
            LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line2Params.height = dp2px(this, 0.5f);
            line2Params.topMargin = dp2px(this, 2.5f);
            line_2.setLayoutParams(line2Params);
            line_2.setBackgroundColor(Color.rgb(0, 0, 0));

            //add in
            layoutDetails.addView(detailsImg);
            layoutDetails.addView(detailsText);
            layoutDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String thisOrderImg = orders.get(index).get("comImgPath");
                    final String thisOrderName = orders.get(index).get("comName");
                    final String thisOrderId   = orders.get(index).get("orderId");
                    final String thisOrderPrice = orders.get(index).get("comPrice");
                    final String thisOrderDate = orders.get(index).get("orderDate");
                    final String thisOrderDescription = orders.get(index).get("comDescription");
                    final String thisOrderAmount = orders.get(index).get("orderNum");
                    final String thisOrderState = orders.get(index).get("orderState");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MyOrderActivity.this, OrderDetailActivity.class);

                            PublicData.setOrderImgUrlStr(thisOrderImg);
                            PublicData.setOrderName(thisOrderName);
                            PublicData.setOrderId(thisOrderId);
                            PublicData.setOrderPrice(thisOrderPrice);
                            PublicData.setOrderDate(thisOrderDate);
                            PublicData.setOrderDes(thisOrderDescription);
                            PublicData.setOrderAmount(thisOrderAmount);
                            PublicData.setOrderState(thisOrderState);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    }, 30);
                }
            });

            layoutUp.addView(stateText);
            layoutUp.addView(layoutDetails);

            layoutMain.addView(nameText);
            layoutMain.addView(layoutUp);

            llAllOrders.addView(layoutMain);
            llAllOrders.addView(line_1);
            llAllOrders.addView(line_2);
        }
    }
}

