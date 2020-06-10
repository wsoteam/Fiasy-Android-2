package com.wsoteam.diet.ads

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.*
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.util.ArrayList
import com.wsoteam.diet.R

object AdWorker {


    private val nativeAds = MutableLiveData<List<UnifiedNativeAd>>()


    fun getNativeAds(): List<UnifiedNativeAd>? = nativeAds.value


    fun loadNative(context: Context, callback: (isLoaded: Boolean) -> Unit = {}) {
        if (nativeAds.value.isNullOrEmpty()) {
            val adsList = ArrayList<UnifiedNativeAd>()
            val builder: AdLoader.Builder = AdLoader.Builder(context, context.getString(R.string.admob_native))
            var adLoader: AdLoader? = null
            adLoader = builder.forUnifiedNativeAd { ad ->
                adsList.add(ad)
                if (adLoader?.isLoading == true) {
                    Log.d("kkk", "!adLoader!!.isLoading1 ${nativeAds.value?.size}")
                    nativeAds.value = adsList
                    callback(true)
                }

            }.withAdListener(object : AdListener() {

                override fun onAdFailedToLoad(p0: Int) {
                    if (adLoader?.isLoading == true) {
                        Log.d("kkk", "!adLoader!!.isLoading2")
                        nativeAds.value = adsList
                        callback(false)
                    }
                }
            }).build()
            adLoader?.loadAds(AdRequest.Builder().build(), 5)
        }else{
            Log.d("kkk", "adLoader isNullOrEmpty")
            callback(true)
        }
    }
}