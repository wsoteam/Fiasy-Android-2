package com.wsoteam.diet.presentation.global;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.wsoteam.diet.R;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;

public abstract class BaseActivity extends MvpAppCompatActivity {

    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator = new SupportAppNavigator(this, -1);
    private MaterialDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        progressDialog = initMaterialDialog();
    }

    private MaterialDialog initMaterialDialog() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.dialog_load_title)
                .content(R.string.dialog_load_content)
                .progress(true, 0);
        return builder.build();
    }

    public void showProgressDialog(boolean show) {
        if (progressDialog == null)
            return;

        if (show) progressDialog.show();
        else progressDialog.dismiss();
    }

    public void showToastMessage(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }
}
