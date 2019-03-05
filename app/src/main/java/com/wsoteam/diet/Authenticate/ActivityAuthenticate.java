package com.wsoteam.diet.Authenticate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wsoteam.diet.R;

public class ActivityAuthenticate extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="ActivityAuthenticate";
    private static final int RC_SIGN_IN = 9001;

    private FragmentManager fm;
    private Fragment fragment;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.auth_frame_layout);
        if (fragment == null) {
            fragment = new FragmentAuthFerst();
            fm.beginTransaction()
                    .add(R.id.auth_frame_layout, fragment)
                    .commit();
        }

        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (email.matches("") ||
            password.matches("")) {
            Log.d(TAG, "signIn: fail valid");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithEmail:success " + user.getEmail());


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ", task.getException());

                        }


                        if (!task.isSuccessful()) {
                            Log.d(TAG, "auth_failed ");
                        }
                    }
                });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (email.matches("") ||
                password.matches("")) {
            Log.d(TAG, "createAccount: fail valid");
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
    }

    private void signOut() {

        Log.d(TAG, "signOut: ");

        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.auth_first_btn_registration:
                fragment = new FragmentAuthHello();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_first_btn_signin:
                fragment = new FragmentAuthSignIn();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).addToBackStack("auth_signIn").commit();
                break;
            case R.id.auth_signin_btn_signin: {
                String email = ((EditText) fragment.getView().findViewById(R.id.auth_signin_et_email)).getText().toString();
                String pass = ((EditText) fragment.getView().findViewById(R.id.auth_signin_et_password)).getText().toString();
                signIn(email, pass);
            }
                break;
            case R.id.auth_signin_btn_signout:
                signOut();
                break;
            case R.id.auth_signin_btn_google:
                signInGoogle();
                break;
            case R.id.auth_hello_btn_next:
                fragment = new FragmentAuthRegistration();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;

            case R.id.auth_reg_btn_reg: {
                String email = ((EditText) fragment.getView().findViewById(R.id.auth_reg_et_email)).getText().toString();
                String pass = ((EditText) fragment.getView().findViewById(R.id.auth_reg_et_pass)).getText().toString();
                createAccount(email, pass);
            }
                break;
            case R.id.auth_reg_btn_google:
                signInGoogle();
                break;

                default:
                    Log.d(TAG, "onClick: switch default");
                    break;
        }
    }
}
