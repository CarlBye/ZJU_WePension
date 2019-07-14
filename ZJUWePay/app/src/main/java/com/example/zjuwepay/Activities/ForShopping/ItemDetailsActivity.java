package com.example.zjuwepay.Activities.ForShopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zjuwepay.Activities.ForButton.BindBuy.BindBuyButtonActivity;
import com.example.zjuwepay.Activities.ForButton.BindFurniture.BindFurnitureButtonActivity;
import com.example.zjuwepay.Activities.ForInfo.MyButton.MyButtonActivity;
import com.example.zjuwepay.Components.MyImageView;
import com.example.zjuwepay.Constant;
import com.example.zjuwepay.PublicData;
import com.example.zjuwepay.R;

import java.util.HashMap;
import java.util.Map;

public class ItemDetailsActivity extends AppCompatActivity implements Constant, View.OnClickListener {

    //components
    private ImageView ivBackToShopping;
    private MyImageView ivItemImg;
    private TextView tvItemName, tvItemId, tvItemPrice, tvItemStock, tvItemDes;
    private EditText et_amount;
    private LinearLayout llOrderConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        init();
    }

    private void init() {
        //bind image view
        ivBackToShopping = findViewById(R.id.btn_backToShopping);
        ivItemImg        = findViewById(R.id.iv_itemImg);
        ivItemImg.setImageURL(PublicData.getItemImgUrlStr());

        //bind text view
        tvItemName      = findViewById(R.id.tv_ItemName);
        tvItemId        = findViewById(R.id.tv_ItemId);
        tvItemPrice     = findViewById(R.id.tv_ItemPrice);
        tvItemStock     = findViewById(R.id.tv_ItemStock);
        tvItemDes       = findViewById(R.id.tv_ItemDes);

        //bind edit text
        et_amount       = findViewById(R.id.et_amount);

        //bind linear layout
        llOrderConfirm  = findViewById(R.id.ll_orderConfirm);

        //set text
        tvItemName.setText(PublicData.getItemName());
        String str = PublicData.getItemId();
        if(str != null) {
            str = String.format("%08d", Integer.parseInt(str));
            str = "C" + str.substring(0,3) + "-" + str.substring(3);
        }

        tvItemId.setText(str);
        tvItemPrice.setText("价格：￥" + PublicData.getItemPrice());
        tvItemStock.setText("库存：" + PublicData.getItemStack() + "件");
        tvItemDes.setText("商品描述：" + PublicData.getItemDescription());

        //bind listener
        ivBackToShopping.setOnClickListener(this);
        llOrderConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_backToShopping:
                PublicData.resetItemInfo();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ItemDetailsActivity.this, ShoppingMainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
            case R.id.ll_orderConfirm:
                String amount = et_amount.getText().toString();
                PublicData.sendItemInfo(amount);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(ItemDetailsActivity.this, BindBuyButtonActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
                    }
                }, 30);
                break;
        }
    }
}
