package com.example.zjuwepay.Activities.ForInfo.MyHealth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.MainActivity;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.R;

public class MyHealthActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //components
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_health);
        init();
    }

    private void init() {
        //bind image view
        ivBack = findViewById(R.id.btn_backToMenu);

        //bind listener
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMenu:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MyHealthActivity.this, MainActivity.class);
                        intent.putExtra("keepOpen", 1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }
}
