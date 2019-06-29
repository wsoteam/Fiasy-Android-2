package com.wsoteam.diet.BranchOfAnalyzer.CustomFood;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentMainInfo;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentOutlay;
import com.wsoteam.diet.BranchOfAnalyzer.CustomFood.Fragments.FragmentResult;
import com.wsoteam.diet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityCreateFood extends AppCompatActivity {
    @BindView(R.id.flFragmentContainer) FrameLayout flFragmentContainer;
    public CustomFood customFood;
    public boolean isPublicFood;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_food);
        ButterKnife.bind(this);
        customFood = new CustomFood();
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentContainer,
                FragmentMainInfo.newInstance(customFood)).commit();
    }

    private boolean isCanMoveForward() {
        return ((SayForward) getSupportFragmentManager().findFragmentById(R.id.flFragmentContainer)).forward();
    }

    @OnClick({R.id.ibClose, R.id.btnBack, R.id.btnForward})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibClose:
                break;
            case R.id.btnBack:
                break;
            case R.id.btnForward:
                if (isCanMoveForward()){
                    Log.e("LOL", customFood.toString());
                    Log.e("LOL", String.valueOf(isPublicFood));
                }
                break;
        }
    }
}
