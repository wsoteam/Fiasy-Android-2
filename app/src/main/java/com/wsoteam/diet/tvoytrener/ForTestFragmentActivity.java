package com.wsoteam.diet.tvoytrener;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.v2.GroupsFragment;

public class ForTestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_test_fragment);


            Fragment fragment = new GroupsFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.testFrame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();


    }
}
