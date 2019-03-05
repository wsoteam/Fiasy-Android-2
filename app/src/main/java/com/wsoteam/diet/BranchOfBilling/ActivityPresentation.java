package com.wsoteam.diet.BranchOfBilling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.wsoteam.diet.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityPresentation extends AppCompatActivity {
    private Button btnBuy;
    private BillingClient billingClient;
    private List<String> skuList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        btnBuy = findViewById(R.id.btnBuy);



        billingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

            }
        }).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                if (responseCode == BillingClient.BillingResponse.OK){
                    requestForBuy();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void requestForBuy() {
        skuList = new ArrayList<>();
        skuList.add("premium");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                Log.e("LOL", String.valueOf(skuDetailsList.size()));
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSku("premium")
                        .setType(BillingClient.SkuType.SUBS)
                        .build();
                billingClient.launchBillingFlow(ActivityPresentation.this, flowParams);
            }
        });
    }

}
