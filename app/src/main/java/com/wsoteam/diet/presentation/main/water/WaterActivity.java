package com.wsoteam.diet.presentation.main.water;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.wsoteam.diet.R;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.global.BaseActivity;

public class WaterActivity extends BaseActivity implements WaterView {

    public static final float PROGRESS_MAX_GLASS = 0.25f;
    public static final float PROGRESS_MAX_BOTTLE = 0.25f;
    private static final float PROGRESS_MIN = 1.5f;
    public static final float PROGRESS_MAX = 3;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.pbWater) SeekBar pbWater;
    @BindView(R.id.tvWater) TextView tvWater;


    @InjectPresenter
    WaterPresenter presenter;

    @ProvidePresenter
    WaterPresenter providePresenter() {
        return new WaterPresenter(this, CiceroneModule.router());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        ButterKnife.bind(this);


        toolbar.setTitle(R.string.water_screen_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pbWater.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                calculateWaterX();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        setDefaultProgress();

        pbWater.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pbWater.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                calculateWaterX();
            }
        });
    }

    private void calculateWaterX() {
        tvWater.setText(((float) pbWater.getProgress() * (false ? PROGRESS_MAX_GLASS : PROGRESS_MAX_BOTTLE) + 1) + " л");
        tvWater.setX(presenter.calcXPosition(pbWater, pbWater.getProgress(), tvWater));
    }

    private void setDefaultProgress() {
        //rbGlass.setChecked(presenter.getWaterPackParameter());
        //rbBottle.setChecked(!presenter.getWaterPackParameter());
        //
        //swWaterReminder.setChecked(presenter.getWaterNotificationParameter());

        changePack(presenter.getWaterProgressStepParameter());
    }

    //@OnClick({R.id.btnDefault, R.id.rbGlass, R.id.rbBottle})
    //void onClick(View view) {
    //    switch (view.getId()) {
    //        case R.id.btnDefault:
    //            rbGlass.setChecked(true);
    //            rbBottle.setChecked(false);
    //            swWaterReminder.setChecked(true);
    //            break;
    //        case R.id.rbGlass:
    //            rbBottle.setChecked(false);
    //            break;
    //        case R.id.rbBottle:
    //            rbGlass.setChecked(false);
    //            break;
    //    }
    //    changePack(0);
    //}

    private void changePack(int waterProgress) {
        int steps = (int) ((PROGRESS_MAX - PROGRESS_MIN) / (false ? PROGRESS_MAX_GLASS : PROGRESS_MAX_BOTTLE));
        pbWater.setMax(steps);
        pbWater.setProgress(1);
        pbWater.setProgress(waterProgress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_water, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                presenter.saveWaterParameters(false, pbWater.getProgress(), false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void settingSaved() {
        finish();
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }
}