package com.example.zjuwepay.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.zjuwepay.ActionHandlers.SlideMenu;
import com.example.zjuwepay.Activities.ForButton.BindButtonMainActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
import com.example.zjuwepay.Activities.ForInfo.MyFurniture.MyFurnitureActivity;
import com.example.zjuwepay.Activities.ForInfo.MyHealth.MyHealthActivity;
import com.example.zjuwepay.Activities.ForInfo.MyHelp.MyHelpActivity;
import com.example.zjuwepay.Activities.ForInfo.MyOrder.MyOrderActivity;
import com.example.zjuwepay.Activities.ForUser.LoginActivity;
import com.example.zjuwepay.Activities.ForUser.LogoutActivity;
import com.example.zjuwepay.Activities.ForUser.SettingActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.example.zjuwepay.Service.DemoIntentService;
import com.example.zjuwepay.Service.DemoPushService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.igexin.sdk.PushManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


public class MainActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String USER_BUTTONS_ALL = "/api/button/list";
    private static final String USER_FURNITURE_ALL = "/api/furniture/list";
    private static final String USER_ORDERS_ALL = "/api/commodity/orderList";
    private static final String USER_HELP_ALL = "/api/alert/list";

    //components
    private ImageView ivOpen, ivBack, ivSetting, ivAddAks, btnUserFace;
    private SlideMenu slideMenu;
    private LinearLayout llMyButton, llMyFurniture, llMyOrder, llMyHelp, llMain, llMyHealth;
    private TextView tvUserName, tvUserDescription, tvToMyButton, tvToMyFurn, tvToMyOrder,
                        tvButtonAmount, tvFurnAmount, tvOrderAmount, tvToMyHelp, tvHelpAmount;

    //async lock
    private boolean wait_lock = false;

    //json helper
    private Gson myPack = new Gson();

    //Amount info
    private String buttonAmount, furnAmount, orderAmount, helpAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        init();
    }

    private void init() {

        //bind slider
        slideMenu = findViewById(R.id.slideMenu);

        //From others activity
        final Intent intent = getIntent();
        int keepActivate = intent.getIntExtra("keepOpen", 0);
        if(keepActivate == 1) {
            slideMenu.switchMenu();
        }

        //bind image view
        ivBack          = findViewById(R.id.btn_back);
        ivOpen          = findViewById(R.id.btn_menu);
        ivSetting       = findViewById(R.id.btn_setting);
        ivAddAks        = findViewById(R.id.btn_addAKS);

        //bind buttons
        btnUserFace     = findViewById(R.id.user_face);
        btnUserFace.setImageResource(faces[PublicData.getUserFace()]);

        //bind linear layout (button)
        llMyButton      = findViewById(R.id.ll_myButton);
        llMyFurniture   = findViewById(R.id.ll_myFurniture);
        llMyOrder       = findViewById(R.id.ll_myOrder);
        llMyHelp        = findViewById(R.id.ll_myHelp);
        llMain          = findViewById(R.id.ll_mainInfo);
        llMyHealth      = findViewById(R.id.ll_myHealth);

        if(!PublicData.getLogState()) {
            llMain.setVisibility(View.INVISIBLE);
        } else {
            //fetch amount info
            getButtonAmountInfo();
            while(!wait_lock){
                SystemClock.sleep(10);
                System.out.println("tick");
            }
            wait_lock = false;

            getFurnAmountInfo();
            while(!wait_lock){
                SystemClock.sleep(10);
                System.out.println("tick");
            }
            wait_lock = false;

            getOrderAmountInfo();
            while(!wait_lock){
                SystemClock.sleep(10);
                System.out.println("tick");
            }
            wait_lock = false;

            getHelpAmountInfo();
            while(!wait_lock){
                SystemClock.sleep(10);
                System.out.println("tick");
            }
            wait_lock = false;

            llMain.setVisibility(View.VISIBLE);
        }

        //bind text view
        tvUserName  = findViewById(R.id.tv_userName);
        tvUserDescription = findViewById(R.id.tv_userDescription);
        tvToMyButton  = findViewById(R.id.tv_toMyButton);
        tvToMyFurn = findViewById(R.id.tv_toMyFurn);
        tvToMyOrder = findViewById(R.id.tv_toMyOrder);
        tvButtonAmount = findViewById(R.id.tv_buttonAmount);
        tvFurnAmount = findViewById(R.id.tv_FurnAmount);
        tvOrderAmount = findViewById(R.id.tv_orderAmount);

        //set the name
        tvUserName.setText(PublicData.getCurrentUserName());
        tvUserDescription.setText(PublicData.getCurrentUserDescription());
        tvButtonAmount.setText(buttonAmount);
        tvFurnAmount.setText(furnAmount);
        tvOrderAmount.setText(orderAmount);
        tvHelpAmount.setText(helpAmount);

        //bind listener
        ivBack.setOnClickListener(this);
        ivOpen.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        ivAddAks.setOnClickListener(this);
        btnUserFace.setOnClickListener(this);
        llMyButton.setOnClickListener(this);
        llMyFurniture.setOnClickListener(this);
        llMyOrder.setOnClickListener(this);
        llMyHelp.setOnClickListener(this);
        llMyHealth.setOnClickListener(this);
        tvToMyOrder.setOnClickListener(this);
        tvToMyFurn.setOnClickListener(this);
        tvToMyButton.setOnClickListener(this);
    }

    private void getButtonAmountInfo() {
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
                buttonAmount = (String)buttonList.get("num");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void getFurnAmountInfo() {
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
                furnAmount = (String)furnitureList.get("num");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void getOrderAmountInfo() {
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
                orderAmount = (String)orderList.get("num");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void getHelpAmountInfo() {
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
                helpAmount = (String)helpList.get("num");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                slideMenu.switchMenu();
                break;

            case R.id.btn_back:
                slideMenu.switchMenu();
                break;

            case R.id.btn_setting:
                if(!PublicData.getLogState()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    }, 0);
                }
                break;

            case R.id.user_face:
                if(!PublicData.getLogState()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.btn_addAKS:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, BindButtonMainActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.ll_myButton:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyButtonActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.tv_toMyButton:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyButtonActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.ll_myFurniture:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyFurnitureActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.tv_toMyFurn:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyFurnitureActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.ll_myOrder:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.tv_toMyOrder:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.ll_myHelp:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyHelpActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;

            case R.id.ll_myHealth:
                if(PublicData.getCurrentUserId() == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, MyHealthActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
                break;
        }
    }
}
