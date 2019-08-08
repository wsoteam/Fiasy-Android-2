package com.wsoteam.diet.presentation.plans.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.wsoteam.diet.R;
import com.wsoteam.diet.di.CiceroneModule;
import ru.terrakok.cicerone.Cicerone;

public class PlanRecipeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plan_recipe);
    ButterKnife.bind(this);

  }
}
