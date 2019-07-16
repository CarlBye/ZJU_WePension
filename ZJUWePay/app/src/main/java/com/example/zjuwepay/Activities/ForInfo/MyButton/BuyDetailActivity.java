package com.example.zjuwepay.Activities.ForInfo.MyButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.zjuwepay.Components.MyImageView;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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


public class BuyDetailActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String ORDER_DETAIL = "/api/button/detail/commodity";

    //components
    private ImageView ivBtnBackToMyButton;
    private MyImageView ivComImg;
    private TextView tvButtonName, tvButtonId, tvButtonType, tvComName, tvComNum, tvComId, tvComPrice, tvComDeliveryName, tvComDeliveryAddress, tvComDeliveryPhone;

    private String buttonName = "";
    private String buttonId = "0";
    private String buttonType = "";
    private String comName = "";
    private String comImg = "";
    private String comNum = "0";
    private String comId = "0";
    private String comPrice = "0.0";
    private String comDeliveryAddress = "";
    private String comDeliveryPhone = "";
    private String comDeliveryName = "";


    //async lock
    private boolean wait_lock = false;

    //json helper
    private Gson myPack = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_button_detail);
        init();
    }

    private void init() {
        //get button info
        getButtonInfo();

        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }

        wait_lock = false;

        //bind image view
        ivBtnBackToMyButton = findViewById(R.id.btn_backToMyButton);
        ivComImg = findViewById(R.id.iv_thisItemImg);
//        ivComImg.setImageURL(comImg);

        //bind text view
        tvButtonName = findViewById(R.id.tv_buttonName);
        tvButtonName.setText(buttonName);
        tvButtonId = findViewById(R.id.tv_buttonId);
        String str = String.format("%08d", Integer.parseInt(buttonId));
        str = "B" + str.substring(0,3) + "-" + str.substring(3);
        tvButtonId.setText(str);
        tvButtonType = findViewById(R.id.tv_buttonType);
        switch(buttonType) {
            case "0":
                tvButtonType.setText("未绑定");
                tvButtonType.setTextColor(Color.rgb(0x80, 0x80, 0x80));
                break;
            case "1":
                tvButtonType.setText("自动下单");
                tvButtonType.setTextColor(Color.rgb(0x99, 0xCC, 0xFF));
                break;
            case "2":
                tvButtonType.setText("家具控制");
                tvButtonType.setTextColor(Color.rgb(0x66, 0xCC, 0x99));
                break;
            case "3":
                tvButtonType.setText("紧急求助");
                tvButtonType.setTextColor(Color.rgb(0xFF, 0x30, 0x00));
                break;
            case "4":
                tvButtonType.setText("体征检测");
                tvButtonType.setTextColor(Color.rgb(0xFF, 0xCC, 0x33));
                break;
        }
        tvComName = findViewById(R.id.tv_ItemName);
        tvComName.setText("| " + comName);
        tvComId = findViewById(R.id.tv_ItemId);
        String str_item = String.format("%08d", Integer.parseInt(comId));
        str_item = "O" + str_item.substring(0,3) + "-" + str_item.substring(3);
        tvComId.setText(str_item);
        tvComNum = findViewById(R.id.tv_ItemAmount);
        tvComNum.setText("购买数量: " + comNum + "件");
        tvComPrice = findViewById(R.id.tv_ItemPrice);
        DecimalFormat format = new DecimalFormat("0.00");
        String str_price = String.valueOf(Double.parseDouble(comPrice) * Integer.parseInt(comNum));
        String price = format.format(new BigDecimal(str_price));
        tvComPrice.setText("总价: " + price + "￥");
        tvComDeliveryAddress = findViewById(R.id.tv_AddressConfirm);
        tvComDeliveryAddress.setText("收货人地址: " + comDeliveryAddress);
        tvComDeliveryPhone = findViewById(R.id.tv_ContactConfirm);
        tvComDeliveryPhone.setText("收货人联系方式: " + comDeliveryPhone);
        tvComDeliveryName = findViewById(R.id.tv_UserConfirm);
        tvComDeliveryName.setText("收货人: " + comDeliveryName);

        //bind listener
        ivBtnBackToMyButton.setOnClickListener(this);
    }

    private void getButtonInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);
        map.put("buttonId", PublicData.getBuyDetailButtonId());

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + ORDER_DETAIL)
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

                buttonName = (String)responseInfo.get("buttonName");
                System.out.println(buttonName);
                buttonId = (String)responseInfo.get("buttonId");
                buttonType = (String)responseInfo.get("buttonType");
//                comImg = (String)responseInfo.get("");

                comName = (String)responseInfo.get("comName");
                System.out.println(comName);
                comId = (String)responseInfo.get("comId");
                comNum = (String)responseInfo.get("comNum");
                comPrice = (String)responseInfo.get("comPrice");
                comDeliveryAddress = (String)responseInfo.get("comDeliveryAddress");
                comDeliveryPhone = (String)responseInfo.get("comDeliveryPhone");
                comDeliveryName = (String)responseInfo.get("comDeliveryName");

                wait_lock = true;

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
                        Intent intent = new Intent(BuyDetailActivity.this, MyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }
}
