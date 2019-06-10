package com.wsoteam.diet.presentation.auth.restore;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.wsoteam.diet.Authenticate.Valid;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BaseActivity;

public class ActivityForgotPassword extends BaseActivity {

    private String TAG = "ActivityForgotPassword";

    private FirebaseAuth mAuth;

    private Button backButon;
    private Button resetButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        backButon = findViewById(R.id.btnBackFP);
        resetButton = findViewById(R.id.btnResetPass);
        emailEditText = findViewById(R.id.etForgotEmail);

        backButon.setOnClickListener(view -> onBackPressed());

        resetButton.setOnClickListener(view -> {
            if (Valid.isValidEmail(emailEditText.getText().toString())) {
                resetPassword();
            } else
                showToastMessage(getString(R.string.forgot_pass_wrong_email));
        });

    }

    private void resetPassword() {
        mAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                .addOnCompleteListener(task -> {
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
