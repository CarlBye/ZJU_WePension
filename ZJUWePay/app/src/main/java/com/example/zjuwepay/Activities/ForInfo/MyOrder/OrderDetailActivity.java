package com.example.zjuwepay.Activities.ForInfo.MyOrder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class OrderDetailActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String CHANGE_STATE = "/api/commodity/changeOrderState";

    //components
    private ImageView ivBackToOrders, ivOk, ivCancel;
    private MyImageView ivOrderImg;
    private TextView tvOrderName, tvOrderId, tvOrderPrice, tvOrderDes, tvOrderAmount, tvOrderDate, tvOrderState;

    //json helper
    private Gson myPack = new Gson();

    //async control
    private boolean msgKey = false;

    private void handleChangeState(Map<Object, Object> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + CHANGE_STATE)
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
    }

    private void init() {
        //bind image view
        ivBackToOrders = findViewById(R.id.btn_backToOrders);
        ivOrderImg = findViewById(R.id.iv_orderImg);
        ivOrderImg.setImageURL(PublicData.getOrderImgUrlStr());
        ivOk = findViewById(R.id.tv_ok);
        ivCancel = findViewById(R.id.tv_cancel);

        //bind text view
        tvOrderName = findViewById(R.id.tv_OrderName);
        tvOrderName.setText(PublicData.getOrderName());
        tvOrderId = findViewById(R.id.tv_OrderId);
        String str = PublicData.getOrderId();
        if(str != null) {
            str = String.format("%08d", Integer.parseInt(str));
            str = "O" + str.substring(0,3) + "-" + str.substring(3);
        }
        tvOrderId.setText(str);
        tvOrderPrice = findViewById(R.id.tv_OrderPrice);
        tvOrderPrice.setText("价格：" + PublicData.getOrderPrice() + "￥");
        tvOrderDes = findViewById(R.id.tv_OrderDes);
        tvOrderDes.setText("商品描述：" + PublicData.getOrderDes());
        tvOrderAmount = findViewById(R.id.tv_OrderAmount);
        tvOrderAmount.setText("购买数量: " + PublicData.getOrderAmount() + "件");
        tvOrderDate = findViewById(R.id.tv_OrderDate);
        tvOrderDate.setText("购买时间：" + PublicData.getOrderDate());
        tvOrderState = findViewById(R.id.tv_OrderState);

        switch (PublicData.getOrderState()) {
            case "0":
                ivOk.setAlpha(0);
                ivCancel.setAlpha(0);
                break;
            case "1":
                tvOrderState.setTextColor(Color.rgb(0xff, 0x33, 0x00));
                tvOrderState.setText("确认下单");
                ivOk.setOnClickListener(this);
                break;
            case "2":
                ivOk.setAlpha(0);
                ivCancel.setAlpha(0);
                break;
            case "3":
                tvOrderState.setTextColor(Color.rgb(0x33, 0x99, 0xff));
                tvOrderState.setText("确认签收");
                ivOk.setOnClickListener(this);
                break;
            case "4":
                ivOk.setAlpha(0);
                ivCancel.setAlpha(0);
                break;
        }

        //bind listener
        ivBackToOrders.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToOrders:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(OrderDetailActivity.this, MyOrderActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.tv_ok:
                Map changeInfo = new HashMap();
                changeInfo.put("curId", PublicData.getCurrentUserId());
                changeInfo.put("orderId", PublicData.getOrderId());

                handleChangeState(changeInfo);

                while(!msgKey) {
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }

                String thisMsg = PublicData.getFeedback();
                String str = thisMsg;
                if(thisMsg == "") {
                    thisMsg = "确认成功";
                }

                Toast errMsg = Toast.makeText(OrderDetailActivity.this, thisMsg, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(str.equals("")) {
                    Intent intent = new Intent(OrderDetailActivity.this, MyOrderActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
                break;
        }
    }
}
