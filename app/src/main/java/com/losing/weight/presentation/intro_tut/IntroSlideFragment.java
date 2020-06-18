package com.losing.weight.presentation.intro_tut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import com.losing.weight.R;
import com.losing.weight.presentation.intro_tut.IntroSlidesAdapter.Slide;

public class IntroSlideFragment extends Fragment {

  public static IntroSlideFragment newInstance(@NonNull Slide slide) {
    final Bundle bundle = new Bundle();
    bundle.putParcelable("slide", slide);

    final IntroSlideFragment fragment = new IntroSlideFragment();
    fragment.setArguments(bundle);
    return fragment;
  }

  @BindView(R.id.image) ImageView slideImageView;
  @BindView(R.id.title) TextView slideTitleView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_intro_one, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.bind(this, view);

    final Slide slide;

    if (getArguments() != null) {
      slide = getArguments().getParcelable("slide");
    } else {
      slide = null;
    }

    if (slide == null) {
      throw new IllegalStateException("");
    }

    //Glide.with(view)
    //    .load(slide.image())
    //    .apply(new RequestOptions()
    //        .skipMemoryCache(true)
    //        .format(DecodeFormat.PREFER_RGB_565)
    //        .centerInside())
    //    .into(slideImageView);
  //TODO check
    Picasso.get()
        .load(slide.image())
        .into(slideImageView);

    slideTitleView.setText(slide.title());
  }
}