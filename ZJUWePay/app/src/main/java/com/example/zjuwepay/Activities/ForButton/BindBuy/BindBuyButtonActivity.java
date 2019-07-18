package com.example.zjuwepay.Activities.ForButton.BindBuy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.ForButton.BindButtonMainActivity;
import com.example.zjuwepay.Activities.ForButton.BindFurniture.BindFurnitureButtonActivity;
import com.example.zjuwepay.Activities.ForButton.ChooseAction.ChooseButtonActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
import com.example.zjuwepay.Activities.ForShopping.ShoppingMainActivity;
import com.example.zjuwepay.Activities.ForUser.LoginActivity;
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

public class BindBuyButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String BIND_BUY = "/api/button/bind/commodity";

    //components
    private ImageView ivBackToButtonMain;
    private MyImageView ivThisItemImg;
    private TextView tvChooseButton, tvGoShopping, tvItemName, tvItemId, tvItemAmount, tvItemPrice, tvAddressConfirm,
                        tvContactConfirm, tvUserConfirm, tvThisButtonId;
    private EditText etComPhone, etComAddress, etComUserName;
    private LinearLayout llBindConfirm;

    //async control
    private boolean msgKey = false;

    //json helper
    private Gson myPack = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_buy_button);
        init();
    }

    private void init() {
        //bind image view
        ivBackToButtonMain  = findViewById(R.id.btn_backToButtonAddMain);
        ivThisItemImg       = findViewById(R.id.iv_thisItemImg);
        String img = PublicData.getComImg();
        if(img != null) {
            ivThisItemImg.setImageURL(img);
        }

        //bind linear layout
        llBindConfirm           = findViewById(R.id.ll_addBuyButton);

        //bind text view
        tvGoShopping        = findViewById(R.id.tv_goShopping);
        tvChooseButton      = findViewById(R.id.tv_chooseButton);
        tvItemName          = findViewById(R.id.tv_ItemName);
        tvItemId            = findViewById(R.id.tv_ItemId);
        tvItemAmount        = findViewById(R.id.tv_ItemAmount);
        tvItemPrice         = findViewById(R.id.tv_ItemPrice);
        tvAddressConfirm    = findViewById(R.id.tv_AddressConfirm);
        tvContactConfirm    = findViewById(R.id.tv_ContactConfirm);
        tvUserConfirm       = findViewById(R.id.tv_UserConfirm);
        tvThisButtonId      = findViewById(R.id.tv_thisButtonId);

        //bind edit text
        etComAddress        = findViewById(R.id.et_newAddress);
        etComPhone          = findViewById(R.id.et_newContact);
        etComUserName       = findViewById(R.id.et_newUserName);

        etComAddress.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                PublicData.setComAddress(etComAddress.getText().toString());
                if(PublicData.getComAddress() != null) tvAddressConfirm.setText("收货人地址：" + PublicData.getComAddress());
                return false;
            }
        });

        etComPhone.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                PublicData.setComPhone(etComPhone.getText().toString());
                if(PublicData.getComPhone() != null) tvContactConfirm.setText("收货人联系方式：" + PublicData.getComPhone());
                return false;
            }
        });

        etComUserName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                PublicData.setComSendName(etComUserName.getText().toString());
                if(PublicData.getComSendName() != null) tvUserConfirm.setText("收货人：" + PublicData.getComSendName());
                return false;
            }
        });

        //set texts
        if(!PublicData.getComName().equals("")) tvItemName.setText(PublicData.getComName());
        if(!PublicData.getComId().equals("")) tvItemId.setText(PublicData.getComId());
        if(!PublicData.getComAmount().equals("")) tvItemAmount.setText(PublicData.getComAmount() + "件");
        if(!PublicData.getComPrice().equals("") && !PublicData.getComAmount().equals("")) {
            DecimalFormat format = new DecimalFormat("0.00");
            String str = String.valueOf(Double.parseDouble(PublicData.getComPrice()) * Integer.parseInt(PublicData.getComAmount()));
            String price = format.format(new BigDecimal(str));
            tvItemPrice.setText(price + "￥");
        }
        if(!PublicData.getComAddress().equals("")) tvAddressConfirm.setText("收货人地址：" + PublicData.getComAddress());
        if(!PublicData.getComPhone().equals("")) tvContactConfirm.setText("收货人联系方式：" + PublicData.getComPhone());
        if(!PublicData.getComSendName().equals("")) tvUserConfirm.setText("收货人：" + PublicData.getComSendName());
        String str_button = PublicData.getButtonIdTemp();
        if(!str_button.equals("未选择按钮")) {
            str_button = "B" + str_button.substring(0,3) + "-" + str_button.substring(3);
        }
        tvThisButtonId.setText(str_button);

        //refresh
        etComAddress.setText(PublicData.getComAddress());
        etComPhone.setText(PublicData.getComPhone());
        etComUserName.setText(PublicData.getComSendName());

        //bind listener
        ivBackToButtonMain.setOnClickListener(this);
        tvGoShopping.setOnClickListener(this);
        tvChooseButton.setOnClickListener(this);
        llBindConfirm.setOnClickListener(this);
    }

    private void handleBuyBind(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + BIND_BUY)
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
            case R.id.btn_backToButtonAddMain:
                PublicData.cancelItem();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindBuyButtonActivity.this, BindButtonMainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.tv_goShopping:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindBuyButtonActivity.this, ShoppingMainActivity.class);
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
                        Intent intent = new Intent(BindBuyButtonActivity.this, ChooseButtonActivity.class);
                        intent.putExtra("fromPage", 2);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.ll_addBuyButton:
                int curId = PublicData.getCurrentUserId();
                String buttonId = PublicData.getButtonIdTemp();
                String comId = PublicData.getComId();
                String num = PublicData.getComAmount();
                String comAddress = PublicData.getComAddress();
                String comPhone = PublicData.getComPhone();
                String comName = PublicData.getComSendName();

                if(comAddress.equals("")) {
                    Toast errMsg = Toast.makeText(BindBuyButtonActivity.this, "请输入收货人地址", Toast.LENGTH_SHORT);
                    errMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errMsg.show();
                    break;
                } else if(comPhone.equals("")) {
                    Toast errMsg = Toast.makeText(BindBuyButtonActivity.this, "请输入收货人联系方式", Toast.LENGTH_SHORT);
                    errMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errMsg.show();
                    break;
                } else if(comName.equals("")) {
                    Toast errMsg = Toast.makeText(BindBuyButtonActivity.this, "请输入收货人姓名", Toast.LENGTH_SHORT);
                    errMsg.setGravity(Gravity.BOTTOM, 0, 10);
                    errMsg.show();
                    break;
                }

                Map bindInfo = new HashMap();
                bindInfo.put("curId", curId);
                bindInfo.put("buttonId", buttonId);
                bindInfo.put("comId", comId);
                bindInfo.put("num", num);
                bindInfo.put("comDeliveryAddress", comAddress);
                bindInfo.put("comDeliveryPhone", comPhone);
                bindInfo.put("comDeliveryName", comName);

                System.out.println(bindInfo.toString());

                handleBuyBind(bindInfo);

                PublicData.setButtonIdTemp("未选择按钮");
                PublicData.setButtonNameTemp("未选择按钮");
                PublicData.cancelItem();

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


                Toast errMsg = Toast.makeText(BindBuyButtonActivity.this, result, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(thisMsg == "") {
                    Intent intent = new Intent(BindBuyButtonActivity.this, MyButtonActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
                break;
        }
    }
}
