package com.example.zjuwepay.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.zjuwepay.ActionHandlers.SlideMenu;
import com.example.zjuwepay.Activities.ForButton.BindButtonMainActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
import com.example.zjuwepay.Activities.ForInfo.MyFurniture.MyFurnitureActivity;
import com.example.zjuwepay.Activities.ForInfo.MyOrder.MyOrderActivity;
import com.example.zjuwepay.Activities.ForUser.LoginActivity;
import com.example.zjuwepay.Activities.ForUser.LogoutActivity;
import com.example.zjuwepay.Activities.ForUser.SettingActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.igexin.sdk.PushManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //components
    private ImageView ivOpen, ivBack, ivSetting, ivAddAks, btnUserFace;
    private SlideMenu slideMenu;
    private LinearLayout llMyButton, llMyFurniture, llMyOrder;
    private TextView tvUserName, tvUserDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PushManager.getInstance().initialize(getApplicationContext(), com.example.zjuwepay.Service.DemoPushService.class);
//        PushManager.getInstance().registerPushIntentService(getApplicationContext(), com.example.zjuwepay.Service.DemoIntentService.class);
        init();
    }

    private void init() {

        //bind slider
        slideMenu   = findViewById(R.id.slideMenu);

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

        //bind text view
        tvUserName  = findViewById(R.id.tv_userName);
        tvUserDescription = findViewById(R.id.tv_userDescription);

        //set the name
        tvUserName.setText(PublicData.getCurrentUserName());
        tvUserDescription.setText(PublicData.getCurrentUserDescription());

        //bind listener
        ivBack.setOnClickListener(this);
        ivOpen.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        ivAddAks.setOnClickListener(this);
        btnUserFace.setOnClickListener(this);
        llMyButton.setOnClickListener(this);
        llMyFurniture.setOnClickListener(this);
        llMyOrder.setOnClickListener(this);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    }
                }, 0);
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
        }
    }
}
