package com.losing.weight.presentation.search.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.losing.weight.R;
import com.losing.weight.ads.FiasyAds;
import com.losing.weight.ads.nativetemplates.NativeTemplateStyle;
import com.losing.weight.ads.nativetemplates.TemplateView;
import com.losing.weight.presentation.search.sections.controller.SectionAdapter;

public class SectionFragment extends Fragment {
  //@BindView(R.id.rvSections) RecyclerView rvSections;
  private SectionAdapter adapter;
  private Unbinder unbinder;

  @BindView(R.id.nativeAd) TemplateView nativeAd;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_section, container, false);
    unbinder = ButterKnife.bind(this, view);
    //rvSections.setLayoutManager(new LinearLayoutManager(getContext()));
    //updateUI();

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

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

  private void updateUI() {
    adapter = new SectionAdapter(getContext());
    //rvSections.setAdapter(adapter);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
