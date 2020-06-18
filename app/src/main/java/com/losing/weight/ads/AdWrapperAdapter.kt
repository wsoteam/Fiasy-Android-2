package com.losing.weight.ads

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.lang.IndexOutOfBoundsException


class AdWrapperAdapter(
    val userAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>,
    var nativeStep: Int,
    var getViewHolder: (parent: ViewGroup) -> NativeViewHolder = {
        AdVH(it)
    }
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val VIEW_HOLDER_NATIVE_AD_TYPE = 600
    private var adsIndex = 0

    private val nativeAdList = SparseArray<UnifiedNativeAd>()
    private var ads: List<UnifiedNativeAd>? = null

    fun insertAds(adsList: List<UnifiedNativeAd>?) {
        ads = adsList
        fillListWithAd()
    }

    fun fillListWithAd(){
        fillListWithAd(ads)
    }

    private fun fillListWithAd(ads: List<UnifiedNativeAd>?){
        if (ads == null) return
        nativeAdList.clear()
        var insertPosition = findNextAdPosition()

        var nativeAd: UnifiedNativeAd? = null
        while (canUseThisPosition(insertPosition)
            && (getNativeAdItem(ads).also { nativeAd = it }) != null) {
            nativeAdList.put(insertPosition, nativeAd)
            notifyItemInserted(insertPosition)
            insertPosition = findNextAdPosition()
        }
    }

    private fun getNativeAdItem(ads: List<UnifiedNativeAd>): UnifiedNativeAd? {
        if (adsIndex >= ads.size ) adsIndex = 0
        return try { ads[adsIndex++] }catch (e: IndexOutOfBoundsException){ null }
    }


    private fun findNextAdPosition(): Int {
        return if (nativeAdList.size() > 0) {
            nativeAdList.keyAt(nativeAdList.size() - 1) + nativeStep
        } else nativeStep - 1
    }


    private fun canUseThisPosition(position: Int): Boolean {
        return (nativeAdList[position] == null) && (itemCount > position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_HOLDER_NATIVE_AD_TYPE) {
            getViewHolder(parent)
        } else {
            userAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        var resultCount = 0

        resultCount += getNativeAdsCount()
        resultCount += getUserAdapterItemCount()

        return resultCount
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NativeViewHolder) {
            holder.bind(nativeAdList[position])
        } else {
            userAdapter.onBindViewHolder(holder, getPositionInUserAdapter(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isNativeAdPosition(position)) {
            VIEW_HOLDER_NATIVE_AD_TYPE
        } else {
            userAdapter.getItemViewType(getPositionInUserAdapter(position))
        }
    }

    private fun isNativeAdPosition(position: Int): Boolean {
        return nativeAdList[position] != null
    }

    private fun getPositionInUserAdapter(position: Int): Int {
        return position - nativeAdList.size().coerceAtMost(position / nativeStep)
    }

    private fun getUserAdapterItemCount(): Int {
        return userAdapter.itemCount
    }

    private fun getNativeAdsCount(): Int {
        return nativeAdList.size()
    }

    abstract class NativeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(ad: UnifiedNativeAd)
    }
}