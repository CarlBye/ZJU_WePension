package com.example.zjuwepay.Activities.ForUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.MainActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;

public class LogoutActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    private LinearLayout btnLogout;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        init();
    }

    private void init() {
        //bind button
        btnLogout = findViewById(R.id.btn_logout);
        btnBack = findViewById(R.id.btn_backToMenu);

        //bind listener
        btnLogout.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                //reset current user info
                PublicData.setLogState(false);
                PublicData.setCurrentUserName(null);
                PublicData.setCurrentUserDescription(null);
                PublicData.setUserFace(0);
                PublicData.setCurrentUserId(0);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;

            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }
}
