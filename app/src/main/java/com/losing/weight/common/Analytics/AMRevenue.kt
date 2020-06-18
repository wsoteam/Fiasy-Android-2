package com.losing.weight.common.Analytics

import com.android.billingclient.api.Purchase
import com.yandex.metrica.Revenue
import com.yandex.metrica.YandexMetrica
import java.util.*

class AMRevenue {

    companion object {
        fun trackRevenue(purchase: Purchase) {
            var revenueReceipt = Revenue.Receipt.newBuilder()
                    .withData(purchase.originalJson)
                    .withSignature(purchase.signature)
                    .build()

            var revenue = Revenue.newBuilderWithMicros(699, Currency.getInstance("RUB"))
                    .withProductID("com.wild.diet")
                    .withQuantity(1)
                    .withReceipt(revenueReceipt)
                    .withPayload("{\"source\":\"Google Play\"}")
                    .build()

            YandexMetrica.reportRevenue(revenue)
        }
    }
}