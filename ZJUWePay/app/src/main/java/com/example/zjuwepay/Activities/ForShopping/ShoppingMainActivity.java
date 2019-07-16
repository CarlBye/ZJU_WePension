package com.example.zjuwepay.Activities.ForShopping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zjuwepay.Activities.ForButton.BindBuy.BindBuyButtonActivity;
import com.example.zjuwepay.Components.MyImageView;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import java.io.IOException;
import java.util.ArrayList;
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
import static com.example.zjuwepay.PublicData.dp2px;

public class ShoppingMainActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //connection
    private static final String URL_ROOT = "http://47.100.98.181:32006";
    private static final String ITEMS_ALL = "/api/commodity/list";
    private static final String SEARCH_ITEM = "/api/commodity/listByName";

    //config for button list
    private int listSize = 0;
    private ArrayList<Map<String, String>> items;

    //json helper
    private Gson myPack = new Gson();

    //components
    private ImageView ivBackToButtonSetting;
    private RollPagerView mRollViewPager;
    private LinearLayout llAllItems;
    private EditText etSearchTag;
    private TextView tvSearchConfirm;

    //async lock
    private boolean wait_lock = false;

    //default lock
    private boolean is_first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_main);
        init();
    }

    private void init() {
        //bind image view
        ivBackToButtonSetting   = findViewById(R.id.btn_backToButtonSetting);

        //bind roll pager view
        mRollViewPager          = findViewById(R.id.roll_view_pager);

        //bind linear layout
        llAllItems              = findViewById(R.id.ll_allItems);

        //bind edit text
        etSearchTag             = findViewById(R.id.et_searchTag);

        //bind text view
        tvSearchConfirm         = findViewById(R.id.tv_searchConfirm);

        //setting for pager
        mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager));
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.GRAY, Color.WHITE));
        mRollViewPager.setAnimationDurtion(750);

        //bind listener
        ivBackToButtonSetting.setOnClickListener(this);
        tvSearchConfirm.setOnClickListener(this);

        //fetch button list
        if(is_first) {
            is_first = false;
            getItemsInfo();
        }
        while(!wait_lock){
            SystemClock.sleep(10);
            System.out.println("tick");
        }
        if(listSize != 0) LoadItemsInfo();
        wait_lock = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToButtonSetting:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ShoppingMainActivity.this, BindBuyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.tv_searchConfirm:
                String searchTag = etSearchTag.getText().toString();
                Map searchInfo = new HashMap();
                searchInfo.put("comName", searchTag);
                goRefresh(searchInfo);
                while(!wait_lock){
                    SystemClock.sleep(10);
                    System.out.println("tick");
                }
                if(listSize != 0) LoadItemsInfo();
                wait_lock = false;
                break;
        }
    }

    private void goRefresh(Map<Object, Object> map) {
        String params = myPack.toJson(map);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + SEARCH_ITEM)
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
                Map itemList = (Map)responseInfo.get("commodityList");
                System.out.println(mainInfo);

                //get button amount
                listSize = Integer.parseInt((String)itemList.get("num"));

                //get button list
                items = (ArrayList<Map<String, String>>) itemList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void getItemsInfo() {

        //no request body
        String params = "";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, params);

        Request request = new Request.Builder()
                .url(URL_ROOT + ITEMS_ALL)
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
                Map itemList = (Map)responseInfo.get("commodityList");

                //get button amount
                listSize = Integer.parseInt((String)itemList.get("num"));

                //get button list
                items = (ArrayList<Map<String, String>>) itemList.get("list");

                wait_lock = true;

                Log.d(TAG, "onResponse: " + mainInfo);
            }
        });
    }

    private void LoadItemsInfo() {
        llAllItems.removeAllViews();
        //build item views;
        for (int i = 0; i < listSize; i++) {
            final int index = i;
            //main layout for an item info
            LinearLayout layoutMain = new LinearLayout(ShoppingMainActivity.this);
            LinearLayout.LayoutParams layoutMainParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutMainParams.height = dp2px(this, 180);
            layoutMain.setLayoutParams(layoutMainParams);
            layoutMain.setOrientation(LinearLayout.HORIZONTAL);

            //image for this item
            MyImageView itemImg = new MyImageView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams itemImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            itemImgParams.width = dp2px(this, 90);
            itemImgParams.height = dp2px(this, 90);
            itemImgParams.topMargin = dp2px(this, 10);
            itemImgParams.leftMargin = dp2px(this, 10);
            itemImg.setLayoutParams(itemImgParams);
            String imgStr = items.get(i).get("comImgPath");
            itemImg.setImageURL(imgStr);

            //inner layout for item info
            LinearLayout layoutInfo = new LinearLayout(ShoppingMainActivity.this);
            LinearLayout.LayoutParams layoutInfoParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutInfoParams.width = dp2px(this, 220);
            layoutInfoParams.leftMargin = dp2px(this, 10);
            layoutInfo.setLayoutParams(layoutInfoParams);
            layoutInfo.setOrientation(LinearLayout.VERTICAL);

            //item name
            TextView nameText = new TextView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams nameTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            nameTextParams.leftMargin = dp2px(this, 10);
            nameTextParams.topMargin = dp2px(this, 20);
            nameText.setTextSize(18);
            nameText.setLayoutParams(nameTextParams);
            nameText.setText(items.get(i).get("comName"));

            //item type
            TextView typeText = new TextView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams typeTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            typeTextParams.leftMargin = dp2px(this, 10);
            typeTextParams.topMargin = dp2px(this, 5);
            typeText.setTextSize(15);
            typeText.setLayoutParams(typeTextParams);
            typeText.setText(items.get(i).get("comType"));

            //inner layout for item stock
            LinearLayout layoutStock = new LinearLayout(ShoppingMainActivity.this);
            LinearLayout.LayoutParams layoutStockParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutStockParams.topMargin = dp2px(this, 18);
            layoutStock.setLayoutParams(layoutStockParams);
            layoutStock.setOrientation(LinearLayout.HORIZONTAL);

            //image for item stock
            ImageView stockImg = new ImageView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams stockImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            stockImgParams.width = dp2px(this, 30);
            stockImgParams.height = dp2px(this, 30);
            stockImgParams.gravity = Gravity.CENTER_VERTICAL;
            stockImgParams.leftMargin = dp2px(this, 10);
            stockImg.setLayoutParams(stockImgParams);
            stockImg.setImageResource(R.drawable.stock);

            //text view for item stock
            TextView stockText = new TextView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams stockTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            stockTextParams.gravity = Gravity.CENTER_VERTICAL;
            stockTextParams.leftMargin = dp2px(this, 10);
            stockText.setLayoutParams(stockTextParams);
            stockText.setTextSize(20);
            stockText.setText(items.get(i).get("comStack") + " 件");

            layoutStock.addView(stockImg);
            layoutStock.addView(stockText);

            //inner layout for item price
            LinearLayout layoutPrice = new LinearLayout(ShoppingMainActivity.this);
            LinearLayout.LayoutParams layoutPriceParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutPriceParams.topMargin = dp2px(this, 10);
            layoutPrice.setLayoutParams(layoutPriceParams);
            layoutPrice.setOrientation(LinearLayout.HORIZONTAL);

            //image for item price
            ImageView priceImg = new ImageView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams priceImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            priceImgParams.width = dp2px(this, 30);
            priceImgParams.height = dp2px(this, 30);
            priceImgParams.gravity = Gravity.CENTER_VERTICAL;
            priceImgParams.leftMargin = dp2px(this, 10);
            priceImg.setLayoutParams(priceImgParams);
            priceImg.setImageResource(R.drawable.yuan);

            //text view for item price
            TextView priceText = new TextView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams priceTextParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            priceTextParams.gravity = Gravity.CENTER_VERTICAL;
            priceTextParams.leftMargin = dp2px(this, 10);
            priceText.setLayoutParams(priceTextParams);
            priceText.setTextSize(20);
            priceText.setText(items.get(i).get("comPrice") + " 元");

            layoutPrice.addView(priceImg);
            layoutPrice.addView(priceText);

            layoutInfo.addView(nameText);
            layoutInfo.addView(typeText);
            layoutInfo.addView(layoutStock);
            layoutInfo.addView(layoutPrice);

            //linear layout for details button
            LinearLayout layoutButton = new LinearLayout(ShoppingMainActivity.this);
            LinearLayout.LayoutParams layoutButtonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutButton.setLayoutParams(layoutButtonParams);
            layoutButton.setOrientation(LinearLayout.VERTICAL);
            layoutButton.setBackgroundColor(Color.rgb(0xF0, 0xF0, 0xF0));

            //the button image
            ImageView buttonImg = new ImageView(ShoppingMainActivity.this);
            LinearLayout.LayoutParams buttonImgParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            buttonImgParams.width = dp2px(this, 30);
            buttonImgParams.height = dp2px(this, 30);
            buttonImgParams.topMargin = dp2px(this, 75);
            buttonImg.setLayoutParams(buttonImgParams);
            buttonImg.setImageResource(R.drawable.more);
            layoutButton.addView(buttonImg);
            layoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String thisItemImg = items.get(index).get("comImgPath");
                    final String thisItemName = items.get(index).get("comName");
                    final String thisItemId   = items.get(index).get("comId");
                    final String thisItemPrice = items.get(index).get("comPrice");
                    final String thisItemStock = items.get(index).get("comStack");
                    final String thisItemDescription = items.get(index).get("comDescription");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ShoppingMainActivity.this, ItemDetailsActivity.class);

                            PublicData.setItemImgUrlStr(thisItemImg);
                            PublicData.setItemName(thisItemName);
                            PublicData.setItemId(thisItemId);
                            PublicData.setItemPrice(thisItemPrice);
                            PublicData.setItemStack(thisItemStock);
                            PublicData.setItemDescription(thisItemDescription);

                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                    }, 30);
                }
            });

            //Divider
            View line_1 = new View(ShoppingMainActivity.this);
            LinearLayout.LayoutParams line1Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line1Params.height = dp2px(this, 0.5f);
            line_1.setLayoutParams(line1Params);
            line_1.setBackgroundColor(Color.rgb(0, 0, 0));

            View line_2 = new View(ShoppingMainActivity.this);
            LinearLayout.LayoutParams line2Params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            line2Params.height = dp2px(this, 0.5f);
            line2Params.topMargin = dp2px(this, 2.5f);
            line_2.setLayoutParams(line2Params);
            line_2.setBackgroundColor(Color.rgb(0, 0, 0));

            //add in
            layoutMain.addView(itemImg);
            layoutMain.addView(layoutInfo);
            layoutMain.addView(layoutButton);

            llAllItems.addView(layoutMain);
            llAllItems.addView(line_1);
            llAllItems.addView(line_2);
        }
    }

    //Adapter for roll view pager
    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] images = {
                R.drawable.ad_1,
                R.drawable.ad_2,
                R.drawable.ad_3,
        };

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(images[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return images.length;
        }
    }
}


