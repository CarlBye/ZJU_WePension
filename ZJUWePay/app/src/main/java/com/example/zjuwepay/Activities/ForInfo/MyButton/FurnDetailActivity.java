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


public class FurnDetailActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String FURN_DETAIL = "/api/button/detail/furniture";

    //components
    private ImageView ivBtnBackToMyButton, ivFurnState, ivFurnType;
    private TextView tvButtonName, tvButtonId, tvButtonType, tvFurnName, tvFurnState, tvFurnType, tvFurnId;
    private LinearLayout llFurnState;

    private String buttonName = "";
    private String buttonId = "0";
    private String buttonType = "";
    private String furnName = "";
    private String furnId = "0";
    private String furnState = "0";
    private String furnType = "0";

    //async lock
    private boolean wait_lock = false;

    //json helper
    private Gson myPack = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furn_button_detail);
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
        ivFurnState = findViewById(R.id.iv_furnState);
        switch(furnState) {
            case "0":
                ivFurnState.setImageResource(R.drawable.red_circle);
                break;
            case "1":
                ivFurnState.setImageResource(R.drawable.green_circle);
                break;
        }
        ivFurnType = findViewById(R.id.iv_furnType);
        switch(furnType) {
            case "0":
                ivFurnType.setImageResource(R.drawable.unknown);
                break;
            case "1":
                ivFurnType.setImageResource(R.drawable.furniture);
                break;
            case "2":
                ivFurnType.setImageResource(R.drawable.tv);
                break;
            case "3":
                ivFurnType.setImageResource(R.drawable.curtain);
                break;
        }

        //bind linear layout
        llFurnState = findViewById(R.id.ll_furnState);
        switch(furnState) {
                /*
                    0 - 关
                    1 - 开
                 */
            case "0":
                llFurnState.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_state_false_shape));
                break;
            case "1":
                llFurnState.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_rec_state_true_shape));
                break;
        }

        //bind text view
        tvButtonName = findViewById(R.id.tv_buttonName);
        tvButtonName.setText(buttonName);
        tvButtonId = findViewById(R.id.tv_buttonId);
        String str = String.format("%08d", Integer.parseInt(buttonId));
        str = "B" + str.substring(0,3) + "-" + str.substring(3);
        tvButtonId.setText(str);
        tvFurnId = findViewById(R.id.tv_furnId);
        String f_str = String.format("%08d", Integer.parseInt(furnId));
        f_str = "F" + f_str.substring(0,3) + "-" + f_str.substring(3);
        tvFurnId.setText(f_str);
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
        tvFurnName = findViewById(R.id.tv_furnName);
        tvFurnName.setText(furnName);
        tvFurnState = findViewById(R.id.tv_furnState);
        switch(furnState) {
                /*
                    0 - 关
                    1 - 开
                 */
            case "0":
                tvFurnState.setText("关闭状态");
                break;
            case "1":
                tvFurnState.setText("开启状态");
                break;
        }
        tvFurnType = findViewById(R.id.tv_furnType);
        switch(furnType) {
            case "0":
                tvFurnType.setText("未知类型");
                break;
            case "1":
                tvFurnType.setText("灯具");
                break;
            case "2":
                tvFurnType.setText("电视");
                break;
            case "3":
                tvFurnType.setText("窗帘");
                break;
        }

        //bind listener
        ivBtnBackToMyButton.setOnClickListener(this);
    }

    private void getButtonInfo() {
        int idForFetch = PublicData.getCurrentUserId();

        Map map = new HashMap();
        map.put("curId", idForFetch);
        map.put("buttonId", PublicData.getFurnDetailButtonId());

        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + FURN_DETAIL)
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
                buttonId = (String)responseInfo.get("buttonId");
                buttonType = (String)responseInfo.get("buttonType");
                furnName = (String)responseInfo.get("furnName");
                furnId = (String)responseInfo.get("furnId");
                furnState = (String)responseInfo.get("furnState");
                furnType = (String)responseInfo.get("furnType");

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
                        Intent intent = new Intent(FurnDetailActivity.this, MyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }

}
