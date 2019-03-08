package com.wsoteam.diet.InApp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.android.billingclient.api.BillingClient;
import com.wsoteam.diet.InApp.util.IabHelper;
import com.wsoteam.diet.InApp.util.IabResult;
import com.wsoteam.diet.InApp.util.Inventory;
import com.wsoteam.diet.InApp.util.Purchase;
import com.wsoteam.diet.MainScreen.MainActivity;
import com.wsoteam.diet.R;


public class ActivitySubscription extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "inappbilling";
    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }

        }
    };

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                try {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
//                        clickButton.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };



    @Override
    protected void onStart() {
        super.onStart();
//        buyButton = (Button)findViewById(R.id.buyButton);
//        clickButton = (Button)findViewById(R.id.clickButton);
//        clickButton.setEnabled(false);

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2AzC65ehidQm+POruTv0Tj9tmOvgtbCIbGL" +
                        "5gScb6YDZT9n6YB2Ev2j/X3WRcYWRU2skUOoTjEwQ26riy5OojeCy52GqYBBG2+SwbVTmKOM" +
                        "xJPy7QmTuT/F+c0wUnjPBf6hv2Ft9HqUUCjamRabqkyT84C2VLK9K4bBlD46c1KDXL2wBjtPD" +
                        "KgBnotuY0Dh24KLDXllv3tK5I45rU3wtJ9O56Z/hEG134aeGHt+d3/IQ9G8v12rmfkJ7Hqit1" +
                        "5MIl5acIyMrWjdN34H6PA33gRO9Fy02IDLBe0VZ4JhyofTS+O2Swruny5xBOpRA4n5oDwVIMBz" +
                        "DEbn9yhrllalATwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });
    }

    public void buyClick(View view)  {
        try {
            mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                    mPurchaseFinishedListener, "mypurchasetoken");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        findViewById(R.id.subsc_btn).setOnClickListener(this);
        findViewById(R.id.subsc_btn_exit).setOnClickListener(this);

    }

    public void consumeItem() {
        try {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        mHelper = null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subsc_btn_exit:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.subsc_btn:
                buyClick(view);
                break;
        }
    }



}
