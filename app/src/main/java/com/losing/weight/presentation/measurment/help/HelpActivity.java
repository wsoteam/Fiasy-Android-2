package com.losing.weight.presentation.measurment.help;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losing.weight.R;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;

public class HelpActivity extends AppCompatActivity {

  @BindView(R.id.nativeAd) TemplateView nativeAd;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meas_help);
    ButterKnife.bind(this);

    FiasyAds.getLiveDataAdView().observe(this, ad -> {
      if (ad != null) {
        nativeAd.setVisibility(View.VISIBLE);
        nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
        nativeAd.setNativeAd(ad);
      }else {
        nativeAd.setVisibility(View.GONE);
      }
    });
  }

  @OnClick(R.id.ibBack) public void onViewClicked() {
    onBackPressed();
  }
}
