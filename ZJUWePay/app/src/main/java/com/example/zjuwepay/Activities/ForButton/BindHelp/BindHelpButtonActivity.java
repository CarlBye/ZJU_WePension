package com.example.zjuwepay.Activities.ForButton.BindHelp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
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
import com.example.zjuwepay.Activities.ForButton.ChooseAction.ChooseFurnitureActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
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

public class BindHelpButtonActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String BIND_HELP = "/api/button/bind/alert";

    //components
    private ImageView ivBack;
    private TextView tvChooseButton, tvThisButtonName, tvThisButtonId;
    private EditText etPhone, etMsg;
    private LinearLayout llBindConfirm;

    //async control
    private boolean msgKey = false;

    //json helper
    private Gson myPack = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_help_button);
        init();
    }

    private void init() {
        //bind image view
        ivBack = findViewById(R.id.btn_backToButtonBindMain);

        //bind text view
        tvChooseButton = findViewById(R.id.tv_chooseButton);
        tvThisButtonName = findViewById(R.id.tv_thisButtonName);
        tvThisButtonId = findViewById(R.id.tv_thisButtonId);

        //bind edit text
        etPhone = findViewById(R.id.et_helpPhone);
        etMsg = findViewById(R.id.et_helpMsg);

        //bind linear layout
        llBindConfirm = findViewById(R.id.ll_BindHelpConfirm);

        //set text
        tvThisButtonName.setText(PublicData.getButtonNameTemp());
        String str_button = PublicData.getButtonIdTemp();
        if(!str_button.equals("未选择按钮")) {
            str_button = "B" + str_button.substring(0,3) + "-" + str_button.substring(3);
        }
        tvThisButtonId.setText(str_button);

        //bind listener
        ivBack.setOnClickListener(this);
        tvChooseButton.setOnClickListener(this);
        llBindConfirm.setOnClickListener(this);

        //refresh edit view info
        etPhone.setText(PublicData.getHelpPhone());
        etMsg.setText(PublicData.getHelpMsg());
    }

    private void handleHelpBind(Map<String, String> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        final RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + BIND_HELP)
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
            case R.id.btn_backToButtonBindMain:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindHelpButtonActivity.this, BindButtonMainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.tv_chooseButton:
                //save input info
                PublicData.setHelpMsg(etMsg.getText().toString());
                PublicData.setHelpPhone(etPhone.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BindHelpButtonActivity.this, ChooseButtonActivity.class);
                        intent.putExtra("fromPage", 3);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                    }
                }, 30);
                break;
            case R.id.ll_BindHelpConfirm:
                int curId = PublicData.getCurrentUserId();
                String buttonId = PublicData.getButtonIdTemp();
                String helpPhone = etPhone.getText().toString();
                String helpMsg = etMsg.getText().toString();

                if(helpMsg.equals("")) {
                    helpMsg = "快救我！！！！";
                }

                if(helpPhone.equals("")) {
                    Toast errorMsg = Toast.makeText(BindHelpButtonActivity.this, "请输入联系方式", Toast.LENGTH_SHORT);
                    errorMsg.setGravity(Gravity.BOTTOM, 0, 50);
                    errorMsg.show();
                    break;
                }

                Map bindInfo = new HashMap();
                bindInfo.put("curId", curId);
                bindInfo.put("buttonId", buttonId);
                bindInfo.put("alertPhone", helpPhone);
                bindInfo.put("alertMessage", helpMsg);

                handleHelpBind(bindInfo);

                PublicData.setButtonIdTemp("未选择按钮");
                PublicData.setButtonNameTemp("未选择按钮");

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


                Toast errMsg = Toast.makeText(BindHelpButtonActivity.this, result, Toast.LENGTH_LONG);
                errMsg.setGravity(Gravity.BOTTOM, 0, 500);
                errMsg.show();

                SystemClock.sleep(1000);

                msgKey = false;

                if(thisMsg == "") {
                    Intent intent = new Intent(BindHelpButtonActivity.this, MyButtonActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                }
                break;
        }
    }

}
