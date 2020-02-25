package com.wsoteam.diet.ads;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.wsoteam.diet.R;
import com.wsoteam.diet.ads.nativetemplates.NativeTemplateStyle;
import com.wsoteam.diet.ads.nativetemplates.TemplateView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AdmobWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DEFAULT_NATIVE_STEP = 5;

    private static final int VIEW_HOLDER_NATIVE_AD_TYPE = 600;


    private RecyclerView.Adapter<RecyclerView.ViewHolder> userAdapter;
    private int nativeStep = DEFAULT_NATIVE_STEP;

    private SparseArray<UnifiedNativeAd> nativeAdList = new SparseArray<>();


    public AdmobWrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> userAdapter, int nativeStep) {
        this.userAdapter = userAdapter;
        this.nativeStep = nativeStep + 1;

        userAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();

                AdmobWrapperAdapter.this.notifyDataSetChanged();

                fillListWithAd();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                AdmobWrapperAdapter.this.notifyDataSetChanged();

                fillListWithAd();
            }
        });

//        Appodeal.setNativeCallbacks(this);

        fillListWithAd();
    }

    public AdmobWrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> userAdapter) {
        this(userAdapter, FiasyAds.NATIVE_STEP_IN_RECYCLER);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_HOLDER_NATIVE_AD_TYPE) {
            return new NativeCustomAdViewHolder(parent);
        } else {
            return userAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NativeCustomAdViewHolder) {
            ((NativeCustomAdViewHolder) holder).fillNative(nativeAdList.get(position));
        } else {
            userAdapter.onBindViewHolder(holder, getPositionInUserAdapter(position));
        }
    }

    @Override
    public int getItemCount() {
        int resultCount = 0;

        resultCount += getNativeAdsCount();
        resultCount += getUserAdapterItemCount();

        return resultCount;
    }

    private int getNativeAdsCount() {
        if (nativeAdList != null) {
            return nativeAdList.size();
        }

        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isNativeAdPosition(position)) {
            return VIEW_HOLDER_NATIVE_AD_TYPE;
        } else {
            return userAdapter.getItemViewType(getPositionInUserAdapter(position));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof NativeCustomAdViewHolder) {
            ((NativeCustomAdViewHolder) holder).unregisterViewForInteraction();
        }
    }

//    public void destroyNativeAds() {
//        if (nativeAdList != null) {
//            for (int i = 0; i < nativeAdList.size(); i++) {
//                NativeAd nativeAd = nativeAdList.valueAt(i);
//                nativeAd.destroy();
//            }
//
//            nativeAdList.clear();
//        }
//    }

    private int getUserAdapterItemCount() {
        if (userAdapter != null) {
            return userAdapter.getItemCount();
        }

        return 0;
    }

    private boolean isNativeAdPosition(int position) {
        return nativeAdList.get(position) != null;
    }

    private int getPositionInUserAdapter(int position) {
        return position - Math.min(nativeAdList.size(), position / nativeStep);
    }

    private void fillListWithAd() {
        int insertPosition = findNextAdPosition();

        UnifiedNativeAd nativeAd;
        while (canUseThisPosition(insertPosition) && (nativeAd = getNativeAdItem()) != null) {
            nativeAdList.put(insertPosition, nativeAd);
            notifyItemInserted(insertPosition);

            insertPosition = findNextAdPosition();
        }
    }

    @Nullable
    private UnifiedNativeAd getNativeAdItem() {
        List<UnifiedNativeAd> ads = new ArrayList<>();
        ads.add(FiasyAds.getLiveDataAdView().getValue());
        return !ads.isEmpty() ? ads.get(0) : null;
    }

    private int findNextAdPosition() {
        if (nativeAdList.size() > 0) {
            return nativeAdList.keyAt(nativeAdList.size() - 1) + nativeStep;
        }
        return nativeStep - 1;
    }

    private boolean canUseThisPosition(int position) {
        return nativeAdList.get(position) == null && getItemCount() > position;
    }

    static class NativeCustomAdViewHolder extends RecyclerView.ViewHolder {

        private TemplateView nativeAd;

        NativeCustomAdViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_native_medium, parent, false));
            nativeAd = itemView.findViewById(R.id.my_template);
            nativeAd.setStyles(new NativeTemplateStyle.Builder().build());

        }

        void fillNative(UnifiedNativeAd ad){
            Log.d("kkk", "fillNative");
            UnifiedNativeAd adNative = FiasyAds.getLiveDataAdView().getValue();
            nativeAd.setNativeAd(adNative);
            FiasyAds.refreshAd(getContext());
        }

        private Context getContext(){
            return itemView.getContext();
        }



        void unregisterViewForInteraction() {
//            nativeAdView.unregisterViewForInteraction();
        }

    }
}
