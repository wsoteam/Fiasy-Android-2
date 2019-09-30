package com.wsoteam.diet.presentation.measurment.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.measurment.MeasurmentView;

public class HistoryActivity extends MvpAppCompatActivity implements HistoryView {
    HistoryPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        presenter = new HistoryPresenter();
        presenter.attachView(this);
    }
}
