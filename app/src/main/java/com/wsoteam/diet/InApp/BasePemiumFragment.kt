package com.wsoteam.diet.InApp

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.wsoteam.diet.Authenticate.POJO.Box
import java.util.*


abstract class BasePemiumFragment(@LayoutRes id: Int): Fragment(id), PurchasesUpdatedListener {

    companion object{
        internal val TAG_BOX = "TAG_BOX"
    }

    private var box: Box? = null
    private var billingClient: BillingClient? = null

    internal fun initBilling(){
        box = arguments?.getSerializable(TAG_BOX) as Box?

        billingClient = BillingClient.newBuilder(requireContext())
                .setListener(this)
                .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(responseCode: Int) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    getSKU()
                }
            }

            override fun onBillingServiceDisconnected() {}
        })

    }

    internal abstract fun getCurrentSKU() : String

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        TODO("Not yet implemented")
    }

    internal  fun buy(sku: String) {
        val mParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(BillingClient.SkuType.SUBS)
                .build()
        billingClient?.launchBillingFlow(activity, mParams)
    }

    internal fun getSKU() {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(IDs.ID_ONE_YEAR_WITH_TRIAL)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient?.querySkuDetailsAsync(params.build()) { responseCode, skuDetailsList -> /*if (responseCode == BillingClient.BillingResponse.OK && skuDetailsList != null) {
                    Log.e("LOL", skuDetailsList.get(0).toString());
                    try {
                        setPrice(skuDetailsList.get(0).getPrice());
                    }catch (Exception ex){
                        Log.d(TAG, "onSkuDetailsResponse: FAIL");
                    }


                } else {
                    Log.d(TAG, "onSkuDetailsResponse: FAIL");
                }*/
        }
    }
}