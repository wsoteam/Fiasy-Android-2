package com.wsoteam.diet.Authenticate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.wsoteam.diet.R;

public class ActivityForgotPassword extends AppCompatActivity {

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

        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Valid.isValidEmail(emailEditText.getText().toString())){
                    resetPassword();
                } else {
                    Toast.makeText(ActivityForgotPassword.this, "Ошибка в e-mail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void resetPassword(){
        mAuth.sendPasswordResetEmail(emailEditText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent." + emailEditText.getText().toString());
                            Toast.makeText(ActivityForgotPassword.this, "Проверьте вашу почту!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Log.d(TAG, "Error");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(ActivityForgotPassword.this, "Пользователь не найден.", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, String.valueOf(e.getClass()));
            }
        });
    }
}
