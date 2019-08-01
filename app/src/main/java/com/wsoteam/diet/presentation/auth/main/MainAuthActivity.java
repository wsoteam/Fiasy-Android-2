package com.wsoteam.diet.presentation.auth.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Authenticate.POJO.Box;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;
import com.wsoteam.diet.common.helpers.AsteriskPasswordTransformationMethod;
import com.wsoteam.diet.common.helpers.BodyCalculates;
import com.wsoteam.diet.di.CiceroneModule;
import com.wsoteam.diet.presentation.auth.dialogs.InstaAuthDialogFragment;
import com.wsoteam.diet.presentation.global.BaseActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainAuthActivity extends BaseActivity implements MainAuthView, InstaAuthDialogFragment.InstaAuthListener {

    private static final String TAG = "Authenticate";
    private static final int RC_SIGN_IN = 9001;

    MainAuthPresenter presenter;

    @BindView(R.id.auth_main_tv_respasss) TextView resPassTextView;
    @BindView(R.id.textView82) TextView checkPPTextView;
    @BindView(R.id.auth_main_email) EditText emailEditText;
    @BindView(R.id.auth_main_pass) EditText passEditText;
    @BindView(R.id.auth_main_pass_dub) EditText passDubEditText;
    @BindView(R.id.password_input_layout) TextInputLayout passwordLayout;
    @BindView(R.id.password_dub_input_layout) TextInputLayout passwordDubLayout;
    @BindView(R.id.auth_main_btn_signin) Button signIn;
    @BindView(R.id.auth_main_btn_facebook) LoginButton facebookLoginButton;
    @BindView(R.id.auth_main_check_pp) CheckBox ppCheckBox;
    @BindView(R.id.rgAuth) RadioGroup rgAuth;
    @BindView(R.id.rbReg) RadioButton rbReg;
    @BindView(R.id.rbSignIn) RadioButton rbSignIn;

    private boolean isAcceptedPrivacyPolicy = false;
    private boolean createUser, forgotPass;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Profile profile;
    private Intent intent;
    private CallbackManager callbackManager;

    @ProvidePresenter
    MainAuthPresenter providePresenter() {
        return presenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main_new);
        ButterKnife.bind(this);

        presenter = new MainAuthPresenter(this, CiceroneModule.router());

        createUser = getIntent().getBooleanExtra(Config.CREATE_PROFILE, false);

        ppCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            isAcceptedPrivacyPolicy = b;
            if (b) checkPPTextView.setTextColor(Color.parseColor("#A63A3A3A"));
        });
        ppCheckBox.performClick();

        rgAuth.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.rbReg:
                    prepareViewsForSignUp();
                    break;
                case R.id.rbSignIn:
                    prepareViewsForSignIn();
                    break;
            }
        });

        if (createUser) {
            //first enter and need show onboard
            if (getIntent().getSerializableExtra(Config.TAG_BOX) != null) {
                Box box = (Box) getIntent().getSerializableExtra(Config.TAG_BOX);
                box.setProfile((Profile) getIntent().getSerializableExtra(Config.INTENT_PROFILE));
                intent = new Intent(this, ActivitySubscription.class).putExtra(Config.TAG_BOX, box)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                intent = new Intent(this, ActivitySplash.class).
                        putExtra(Config.INTENT_PROFILE, getIntent().getSerializableExtra(Config.INTENT_PROFILE))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            rbReg.setChecked(true);
        } else {
            isAcceptedPrivacyPolicy = true;
            intent = new Intent(this, ActivitySplash.class).
                    putExtra(Config.INTENT_PROFILE, getIntent().getSerializableExtra(Config.INTENT_PROFILE))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            rbSignIn.setChecked(true);
        }

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());

                if (createUser && presenter.mAuth.getCurrentUser().getProviderData().size() > 0) {
                    AmplitudaEvents.logEventReg(presenter.mAuth.getCurrentUser().getProviderData().get(0).toString());
                } else {
                    AmplitudaEvents.logEventReg("unknown");
                }

                if (createUser && getIntent().getSerializableExtra(Config.INTENT_PROFILE) != null) {
                    WorkWithFirebaseDB.putProfileValue((Profile) getIntent().getSerializableExtra(Config.INTENT_PROFILE));
                } else if (createUser && getIntent().getSerializableExtra(Config.INTENT_PROFILE) == null) {
                    checkUserExist(user.getUid());
                    return;
                }
                startActivity(intent);
            } else {
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                presenter.handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                hasConnection(MainAuthActivity.this);
            }
        });

        passEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        passDubEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    private void prepareViewsForSignIn() {
        resPassTextView.setVisibility(View.VISIBLE);
        passwordLayout.setVisibility(View.VISIBLE);
        passwordDubLayout.setVisibility(View.GONE);
        ppCheckBox.setVisibility(View.INVISIBLE);
        checkPPTextView.setVisibility(View.INVISIBLE);
        signIn.setText(R.string.auth_main_btn_signin);
        rbSignIn.setChecked(true);
        forgotPass = false;
        createUser = false;
    }

    private void prepareViewsForForgotPass() {
        resPassTextView.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);
        passwordDubLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);
        ppCheckBox.setVisibility(View.INVISIBLE);
        checkPPTextView.setVisibility(View.INVISIBLE);
        signIn.setText(R.string.auth_main_btn_send_pass);
        forgotPass = true;
        createUser = false;
    }

    private void prepareViewsForSignUp() {
        resPassTextView.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.VISIBLE);
        passwordDubLayout.setVisibility(View.VISIBLE);
        ppCheckBox.setVisibility(View.VISIBLE);
        checkPPTextView.setVisibility(View.VISIBLE);
        signIn.setText(R.string.auth_main_btn_create);
        rbReg.setChecked(true);
        forgotPass = false;
        createUser = true;
    }

    @OnClick({R.id.auth_main_btn_signin, R.id.auth_main_btn_google, R.id.auth_main_tv_respasss, R.id.textView82, R.id.auth_main_btn_google_custom,
            R.id.auth_main_btn_facebook_custom, R.id.auth_main_btn_insta_custom})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auth_main_btn_signin:
                if (createUser) {
                    if (hasConnection(this) && isPP()) {
                        if (passEditText.getText().toString().equals(passDubEditText.getText().toString()))
                            presenter.createAccount(emailEditText.getText().toString(),
                                    passEditText.getText().toString());
                        else
                            showMessage(getString(R.string.auth_main_pass_not_match));
                    }
                } else if (forgotPass) {
                    if (hasConnection(this))
                        presenter.resetPassword(emailEditText.getText().toString());
                } else {
                    if (hasConnection(this))
                        presenter.signIn(emailEditText.getText().toString(),
                                passEditText.getText().toString());
                }
                break;
            case R.id.auth_main_btn_google:
                if (hasConnection(this)) {
                    signInGoogle();
                }
                break;
            case R.id.auth_main_tv_respasss:
                prepareViewsForForgotPass();
                break;
            case R.id.textView82:
                presenter.onPrivacyPolicyClicked();
                break;
            case R.id.auth_main_btn_google_custom:
                signInGoogle();
                break;
            case R.id.auth_main_btn_facebook_custom:
                if (isPP()) {
                    facebookLoginButton.performClick();
                }
                break;
            case R.id.auth_main_btn_insta_custom:
                instaAuth();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    private ValueEventListener getPostListener() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//            Post post = dataSnapshot.getValue(Post.class);
                profile = dataSnapshot.getValue(Profile.class);
                Log.d(TAG, "onDataChange: " + profile.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        return postListener;
    }

    private void signInGoogle() {
        try {
            if (!isPP())
                return;

            Log.d(TAG, "signInGoogle: 1");
            presenter.mGoogleSignInClient.signOut();

            Log.d(TAG, "signInGoogle: 2");
            Intent signInIntent = presenter.mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
            Log.d(TAG, "signInGoogle: 3");
        } catch (Exception e) {
            showMessage(e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: 3");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: 4");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d(TAG, "onActivityResult: 5");
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(TAG, "onActivityResult: 6");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "onActivityResult: 6.1");
                presenter.firebaseAuthWithGoogle(account);
                Log.d(TAG, "onActivityResult: 6.2");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed", e);
                // ...
            }
        }
        Log.d(TAG, "onActivityResult: finish");
    }

    private void checkUserExist(String uid) {
        Log.d(TAG, "User uid: " + uid);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile = dataSnapshot.getValue(Profile.class);
                checkProfile(profile);
                if (profile != null)
                    Log.d(TAG, "onDataChange: " + profile.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        presenter.mDatabase.child(uid).child("profile").addListenerForSingleValueEvent(postListener);
    }

    private void checkProfile(Profile profile) {
        if (profile == null) {
//            new AlertDialog.Builder(this)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setTitle(getString(R.string.auth_main_alert_title))
//                    .setMessage(getString(R.string.auth_main_alert_body))
//                    .setPositiveButton(getString(R.string.auth_main_alert_ok), (dialog, which) -> presenter.signOutAll())
//                    .show();
            WorkWithFirebaseDB.putProfileValue(BodyCalculates.generateProfile(this));

            Log.d(TAG, "checkUserExist: false. Created New");
        }
        if (isPP() && createUser) Log.d(TAG, "logEvent: accept_police");
        Log.d(TAG, "checkUserExist: true");
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            presenter.mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void instaAuth() {
        DialogFragment newFragment = InstaAuthDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    private boolean isPP() {
        if (!isAcceptedPrivacyPolicy) {
            checkPPTextView.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void showProgress(boolean show) {
        showProgressDialog(show);
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void goBackToSignIn() {
        prepareViewsForSignIn();
    }

    @Override
    public void onInstaTokenReceived(String auth_token) {
        presenter.firebaseAuthWithInstagram(auth_token);
    }

    @Override
    public void onBackPressed() {
        if (forgotPass) {
            rbSignIn.setChecked(true);
        } else
            super.onBackPressed();

    }
}