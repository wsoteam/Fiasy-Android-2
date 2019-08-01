package com.wsoteam.diet.tvoytrener;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.wsoteam.diet.R;
import com.wsoteam.diet.Recipes.adding.ListAddedRecipeFragment;

public class ForTestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_test_fragment);


            Fragment fragment = new ListAddedRecipeFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.testFrame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();


    }
}
