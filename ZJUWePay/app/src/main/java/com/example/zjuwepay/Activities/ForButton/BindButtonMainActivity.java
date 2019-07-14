package com.example.zjuwepay.Activities.ForButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.ForButton.BindBuy.BindBuyButtonActivity;
import com.example.zjuwepay.Activities.ForButton.BindFurniture.BindFurnitureButtonActivity;
import com.example.zjuwepay.Activities.MainActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.R;

public class BindButtonMainActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //components
    private ImageView ivBackToMain;
    private LinearLayout llAddBuyButton, llBindFurnitureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_button_main);
        init();
    }

    private void init() {
        //bind image view
        ivBackToMain            = findViewById(R.id.btn_backToMain);

        //bind buttons
        llAddBuyButton          = findViewById(R.id.ll_addBuyButton);
        llBindFurnitureButton   = findViewById(R.id.ll_bindFurButton);

        //bind listener
        ivBackToMain.setOnClickListener(this);
        llAddBuyButton.setOnClickListener(this);
        llBindFurnitureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMain:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindButtonMainActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 0);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;

            case R.id.ll_addBuyButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindButtonMainActivity.this, BindBuyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;

            case R.id.ll_bindFurButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindButtonMainActivity.this, BindFurnitureButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
        }
    }
}
