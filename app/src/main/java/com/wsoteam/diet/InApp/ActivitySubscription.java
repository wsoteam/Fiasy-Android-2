package com.wsoteam.diet.InApp;


import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;


public class ActivitySubscription extends AppCompatActivity implements View.OnClickListener, PurchasesUpdatedListener {

    private BillingClient billingClient;

    private static final String TAG = "inappbilling";

    private TextView oneMTextView;
    private TextView threeMTextView;
    private TextView earTextView;

    private Button oneMButton;
    private Button threeMButton;
    private Button earButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        oneMButton = findViewById(R.id.subsc_btn_1m);
        threeMButton = findViewById(R.id.subsc_btn_3m);
        earButton = findViewById(R.id.subsc_btn_12m);
        findViewById(R.id.subsc_btn_exit).setOnClickListener(this);

        oneMButton.setOnClickListener(this);
        threeMButton.setOnClickListener(this);
        earButton.setOnClickListener(this);

        oneMTextView = findViewById(R.id.subsc_tv_1m);
        threeMTextView = findViewById(R.id.subsc_tv_3m);
        earTextView = findViewById(R.id.subsc_tv_12m);

        oneMTextView.setText("Премиум на месяц");
        threeMTextView.setText("Премиум на три месяца");
        earTextView.setText("Премиум на год");

        billingClient = BillingClient.newBuilder(this).setListener((PurchasesUpdatedListener)this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK){
                    Log.d(TAG, "onBillingSetupFinished: OK");
                    getSKU();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });



    }

    private void getSKU(){
        List<String> skuList = new ArrayList<>();
        skuList.add("basic_subscription_1m");
        skuList.add("basic_subscription_3m");
        skuList.add("basic_subscription_12m");

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);

        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {

                if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null){
                    Log.d(TAG, "onSkuDetailsResponse: OK");
//                    nameTextView.setText(skuDetailsList.get(0).getSku());
//                    priceTextView.setText(skuDetailsList.get(0).getPrice());
                        oneMButton.setText("Купить за " + skuDetailsList.get(1).getPrice());
                        threeMButton.setText("Купить за " + skuDetailsList.get(2).getPrice());
                        earButton.setText("Купить за " + skuDetailsList.get(0).getPrice());


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }

            }
        });
    }

    private void buy(String sku){
        BillingFlowParams mParams = BillingFlowParams.newBuilder().
                setSku(sku).setType(BillingClient.SkuType.SUBS).build();

        billingClient.launchBillingFlow(this, mParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subsc_btn_exit:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.subsc_btn_1m:
                buy("basic_subscription_1m");
                break;
            case R.id.subsc_btn_3m:
                buy("basic_subscription_3m");
                break;
            case R.id.subsc_btn_12m:
                buy("basic_subscription_12m");
                break;
        }
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }
}
