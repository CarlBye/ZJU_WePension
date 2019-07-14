package com.example.zjuwepay.Activities.ForUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Constant;
import com.example.zjuwepay.R;

public class FaceSelectActivity extends AppCompatActivity implements Constant {

    private ImageView btnBack;
    private ImageView[] btnFaces = new ImageView[15];

    private static int[] face_id = {R.id.img_face_1,    R.id.img_face_2,    R.id.img_face_3,
                                    R.id.img_face_4,    R.id.img_face_5,    R.id.img_face_6,
                                    R.id.img_face_7,    R.id.img_face_8,    R.id.img_face_9,
                                    R.id.img_face_10,   R.id.img_face_11,   R.id.img_face_12,
                                    R.id.img_face_13,   R.id.img_face_14,   R.id.img_face_15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_select);
        init();
    }

    private void init() {

        //From others activity
        final Intent intent = getIntent();
        final int pageBackTo = intent.getIntExtra("super", 0);

        for(int i = 1; i <= 15; i++) {
            final int temp_id = i;
            btnFaces[i - 1] = findViewById(face_id[i - 1]);
            btnFaces[i - 1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(FaceSelectActivity.this, RegisterActivity.class);
                            intent.putExtra("face_id", temp_id);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }
                    }, 0);
                }
            });
        }

        btnBack = findViewById(R.id.btn_backToFront);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(pageBackTo == 1) {
                            Intent intent = new Intent(FaceSelectActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                        }
                    }
                }, 0);
            }
        });
    }
}
