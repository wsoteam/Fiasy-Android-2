package com.wsoteam.diet.presentation.plans.detail.blocked;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockedDetailPlansActivity extends MvpAppCompatActivity implements BlockedDetailPlansView {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @InjectPresenter
    BlockedDetailPlansPresenter presenter;

    @ProvidePresenter
    BlockedDetailPlansPresenter providePresenter(){
        return new BlockedDetailPlansPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_detail_plans);
        ButterKnife.bind(this);

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

    }

    View.OnClickListener navigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    MenuItem.OnMenuItemClickListener menuListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            return true;
        }
    };

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showMessage(String message) {

    }
}
