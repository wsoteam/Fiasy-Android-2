package com.wsoteam.diet.presentation.auth.restore;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.wsoteam.diet.Authenticate.Valid;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class ActivityForgotPassword extends BaseActivity {

    @BindView(R.id.etForgotEmail) EditText emailEditText;
    private String TAG = "ActivityForgotPassword";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick({R.id.btnBackFP, R.id.btnResetPass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackFP:
                onBackPressed();
                break;
            case R.id.btnResetPass:
                String email = emailEditText.getText().toString();
                if (!TextUtils.isEmpty(email) && Valid.isValidEmail(email))
                    resetPassword();
                else
                    showToastMessage(getString(R.string.auth_wrong_email));
                break;
        }
    }

    private void resetPassword() {
        showProgressDialog(true);
        mAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent." + emailEditText.getText().toString());
                        showToastMessage(getString(R.string.forgot_pass_check_email));
                        finish();
                    } else {
                        Log.d(TAG, "Error");
                    }
                }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthInvalidUserException) {
                showToastMessage(getString(R.string.forgot_pass_wrong_user));
            }
            Log.d(TAG, String.valueOf(e.getClass()));
        });
    }

}
