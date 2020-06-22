package com.losing.weight.InApp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.android.billingclient.api.*
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.losing.weight.Authenticate.POJO.Box
import com.losing.weight.BuildConfig
import com.losing.weight.Config
import com.losing.weight.InApp.properties.CheckAndSetPurchase
import com.losing.weight.InApp.properties.SingletonMakePurchase
import com.losing.weight.common.Analytics.EventProperties
import com.losing.weight.common.Analytics.Events
import com.losing.weight.common.Analytics.SavedConst
import com.losing.weight.utils.IntentUtils



abstract class BasePremiumFragment(@LayoutRes id: Int): Fragment(id), PurchasesUpdatedListener {

    companion object{
        internal val TAG_BOX = "TAG_BOX"
        internal const val BUY_NOW = "BUY_NOW"
    }

    private var box: Box? = null
    private var billingClient: BillingClient? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBilling()

    }

    private fun initBilling(){
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

    internal abstract fun setPrice(sku: SkuDetails?)

    override fun onPurchasesUpdated(responseCode: Int, purchases: MutableList<Purchase>?) {
        if (responseCode != BillingClient.BillingResponse.OK) {
            Events.logBillingError(responseCode)
        }
        if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
            //send data about purchase into firebase (and prepareToSave into profile subInfo)
            SingletonMakePurchase.getInstance().isMakePurchaseNow = true
            val p = purchases[0]
            if (BuildConfig.DEBUG) {
                Log.d("Fiasy", "Purchased, $p")
            }
            CheckAndSetPurchase(activity).execute(p.sku, p.purchaseToken, p.packageName, BUY_NOW)
            try {
                if (p.isAutoRenewing) {
                    Events.logBuy(box!!.buyFrom, EventProperties.auto_renewal_true)
                } else {
                    Events.logBuy(box!!.buyFrom, EventProperties.auto_renewal_false)
                }
            } catch (ex: Exception) {
                Events.logSetBuyError(ex.message)
            }
            logTrial()
            requireContext().getSharedPreferences(Config.STATE_BILLING, Context.MODE_PRIVATE).edit().putBoolean(Config.STATE_BILLING, true).apply()
            sharedPreferences = requireContext().getSharedPreferences(Config.ALERT_BUY_SUBSCRIPTION,
                    Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences?.edit() ?: throw IllegalArgumentException()
            editor.putBoolean(Config.ALERT_BUY_SUBSCRIPTION, true)
            editor.apply()
            if (box!!.isOpenFromIntrodaction) {
                box!!.isSubscribe = true
                context?.getSharedPreferences(SavedConst.HOW_END, Context.MODE_PRIVATE)?.edit()?.putString(SavedConst.HOW_END, EventProperties.onboarding_success_trial)?.apply()
            }
            IntentUtils.openMainActivity(requireContext())
        }
    }

    internal  fun buy(sku: String) {
        val mParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(BillingClient.SkuType.SUBS)
                .build()
        billingClient?.launchBillingFlow(activity, mParams)
    }

    internal fun getSKU() {
        val skuList  = listOf(getCurrentSKU())
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)


        billingClient!!.querySkuDetailsAsync(params.build()) { responseCode, skuDetailsList ->
            if (responseCode == BillingClient.BillingResponse.OK && !skuDetailsList.isNullOrEmpty()) {
                Log.e("LOL", skuDetailsList[0].toString())
                try {
                    setPrice(skuDetailsList[0])
                } catch (ex: java.lang.Exception) {
                    Log.d("kkk", "onSkuDetailsResponse: FAIL")
                }
            } else {
                setPrice(null)
                Log.d("kkk", "onSkuDetailsResponse: FAIL")
            }
        }
    }

    private fun logTrial() {
        val appEventsLogger = AppEventsLogger.newLogger(activity)
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "RUB")
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_START_TRIAL, 990.0, params)
    }
}