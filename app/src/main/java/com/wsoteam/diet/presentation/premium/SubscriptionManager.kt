package com.wsoteam.diet.presentation.premium

import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.wsoteam.diet.App

object SubscriptionManager {

  private val purchaseListener = PurchasesUpdatedListener { responseCode, purchases ->
    val purchases = purchases?.map { it.toString() }?.joinToString("\n") ?: "no_purchases"
    Log.d("PurchaseUpdated", "responseCode=$responseCode, purchases=$purchases")
  }

  private val billingClient = BillingClient.newBuilder(App.instance)
    .setListener(purchaseListener)
    .build()

  init {
    billingClient.startConnection(object : BillingClientStateListener {
      override fun onBillingServiceDisconnected() {

      }

      override fun onBillingSetupFinished(responseCode: Int) {
        if (responseCode == BillingClient.BillingResponse.OK) {

        }
      }
    })
  }

  fun buy() {

  }

}
