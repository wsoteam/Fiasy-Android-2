package com.wsoteam.diet.presentation.search;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wsoteam.diet.R;

public class ParentActivity extends AppCompatActivity {
  @BindView(R.id.ibStartAction) ImageButton ibStartAction;
  @BindView(R.id.spnEatingList) Spinner spnEatingList;
  @BindView(R.id.edtActivityListAndSearchCollapsingSearchField) EditText
      edtSearch;
  @BindView(R.id.ibActivityListAndSearchCollapsingCancelButton) ImageView
      ibCancel;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_parent);
    ButterKnife.bind(this);
    getSupportFragmentManager().beginTransaction().replace(R.id.searchFragmentContainer, new SectionFragment()).commit();
  }

}
