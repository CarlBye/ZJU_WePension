package com.example.zjuwepay.Activities.ForInfo.MyButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Constant;
import com.example.zjuwepay.R;

public class ButtonDetailActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //components
    private ImageView ivBtnBackToMyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furn_button_detail);
        init();
    }

    private void init() {
        //bind image view
        ivBtnBackToMyButton = findViewById(R.id.btn_backToMyButton);

        //bind listener
        ivBtnBackToMyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToMyButton:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ButtonDetailActivity.this, MyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }

}
