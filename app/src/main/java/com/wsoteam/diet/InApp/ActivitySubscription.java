package com.wsoteam.diet.InApp;


import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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

    private String subscription = null;
    CheckableCardView oneMonth;
    CheckableCardView threeMonth;
    CheckableCardView oneYear;

    CardView oneMonthB;
    CardView threeMonthB;
    CardView oneYearB;

    Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        findViewById(R.id.subsc_btn_exit).setOnClickListener(this);


        oneMonth = findViewById(R.id.sub_cv_1m);
        threeMonth = findViewById(R.id.sub_cv_3m);
        oneYear = findViewById(R.id.sub_cv_12m);

        oneMonth.setOnClickListener(this);
        threeMonth.setOnClickListener(this);
        oneYear.setOnClickListener(this);

        oneMonthB = findViewById(R.id.sub_cv_1m_b);
        threeMonthB = findViewById(R.id.sub_cv_3m_b);
        oneYearB = findViewById(R.id.sub_cv_12m_b);

        buyButton = findViewById(R.id.subsc_btn_bay);
        buyButton.setOnClickListener(this);

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

    private void checkableCard(View view){

//        switch ()

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
//                        oneMButton.setText("Купить за " + skuDetailsList.get(1).getPrice());
//                        threeMButton.setText("Купить за " + skuDetailsList.get(2).getPrice());
//                        earButton.setText("Купить за " + skuDetailsList.get(0).getPrice());


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }

            }
        });
    }

    private void buy(){

        if (subscription != null) {
            BillingFlowParams mParams = BillingFlowParams.newBuilder().
                    setSku(subscription).setType(BillingClient.SkuType.SUBS).build();

            billingClient.launchBillingFlow(this, mParams);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subsc_btn_exit:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
//            case R.id.subsc_btn_1m:
//                buy("basic_subscription_1m");
//                break;
//            case R.id.subsc_btn_3m:
//                buy("basic_subscription_3m");
//                break;
//            case R.id.subsc_btn_12m:
//                buy("basic_subscription_12m");
//                break;
            case R.id.sub_cv_1m:
                oneMonth.setCardBackgroundColor(Color.parseColor("#FFD1912D"));
                threeMonth.setChecked(false);
                oneYear.setChecked(false);

                oneMonthB.setCardBackgroundColor(Color.parseColor("#FD5B1C"));
                threeMonthB.setCardBackgroundColor(Color.parseColor("#44566D"));
                oneYearB.setCardBackgroundColor(Color.parseColor("#44566D"));

                subscription = "basic_subscription_1m";
                break;
            case R.id.sub_cv_3m:
                oneMonth.setChecked(false);
                oneYear.setChecked(false);

                threeMonthB.setCardBackgroundColor(Color.parseColor("#FD5B1C"));
                oneMonthB.setCardBackgroundColor(Color.parseColor("#44566D"));
                oneYearB.setCardBackgroundColor(Color.parseColor("#44566D"));

                subscription = "basic_subscription_3m";
                break;
            case R.id.sub_cv_12m:
                threeMonth.setChecked(false);
                oneMonth.setChecked(false);

                oneYearB.setCardBackgroundColor(Color.parseColor("#FD5B1C"));
                oneMonthB.setCardBackgroundColor(Color.parseColor("#44566D"));
                threeMonthB.setCardBackgroundColor(Color.parseColor("#44566D"));

                subscription = "basic_subscription_12m";
                break;
            case R.id.subsc_btn_bay:
                buy();
                break;
        }
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

    }
}
