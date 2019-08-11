package com.wsoteam.diet.presentation.plans.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

import com.wsoteam.diet.presentation.global.BaseActivity;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import ru.terrakok.cicerone.Router;

public class DetailPlansActivity extends BaseActivity implements DetailPlansView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.recycler) RecyclerView recycler;
  @BindView(R.id.btnJoin) Button btnJoin;

  @Inject
  Router router;

  @InjectPresenter
  DetailPlansPresenter presenter;

  @ProvidePresenter
  DetailPlansPresenter providePresenter() {
    return new DetailPlansPresenter(router, getIntent());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_plans);
    ButterKnife.bind(this);

    Log.d("kkk", "onCreate: " + router);

    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    getWindow().setStatusBarColor(Color.parseColor("#32000000"));

    toolbar.inflateMenu(R.menu.diet_plans_menu);
    toolbar.setNavigationOnClickListener(navigationListener);

    Menu menu = toolbar.getMenu();
    MenuItem shareMenu = menu.findItem(R.id.mShare);
    MenuItem dotMenu = menu.findItem(R.id.mDots);

    shareMenu.setOnMenuItemClickListener(menuListener);
    dotMenu.setOnMenuItemClickListener(menuListener);

    recycler.setLayoutManager(new LinearLayoutManager(this));
  }

  @OnClick({ R.id.btnJoin })
  void onClicked() {
        presenter.clickedJoin();
  }

  @Override
  public void showProgress(boolean show) {

  }

  @Override
  public void showMessage(String message) {

  }

  @Override
  public void setAdapter(RecyclerView.Adapter adapter) {
    recycler.setAdapter(adapter);
  }

  @Override public void visibilityButtonJoin(boolean value) {
    btnJoin.setVisibility(value ? View.VISIBLE : View.GONE);
  }


  View.OnClickListener navigationListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      presenter.clickedClose();
    }
  };
  MenuItem.OnMenuItemClickListener menuListener = new MenuItem.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

      return true;
    }
  };

}
