package com.wsoteam.diet.Authenticate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.amplitude.api.Amplitude;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.AmplitudaEvents;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.EventsAdjust;
import com.wsoteam.diet.OtherActivity.ActivityPrivacyPolicy;
import com.wsoteam.diet.EntryPoint.ActivitySplash;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAuthMain extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "Authenticate";
    private static final int RC_SIGN_IN = 9001;
    private boolean isAcceptedPrivacyPolicy = false;

    private boolean createUser;
    AlertDialog alertDialogPhone;

    boolean isSendCode = false;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private DatabaseReference mDatabase;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private CallbackManager callbackManager;

    private TextView resPassTextView;
    private EditText emailEditText;
    private EditText passEditText;
    private LoginButton facebookLoginButton;
    private SignInButton mGoogleSignInButton;
    private Button signIn;
    private Button phoneButton;
    private Button googleCustomButton;
    private Button facebookCustomButton;
    private Profile profile;
    private TextView statusTextView;
    private TextView checkPPTextView;
    private CheckBox ppCheckBox;
    private Button backButton;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);

        createUser = (boolean) getIntent().getBooleanExtra("createUser", false);

        findViewById(R.id.auth_main_btn_signin).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("USER_LIST");


        resPassTextView = findViewById(R.id.auth_main_tv_respasss);
        emailEditText = findViewById(R.id.auth_main_email);
        passEditText = findViewById(R.id.auth_main_pass);
        phoneButton = findViewById(R.id.auth_main_btn_phone);
        facebookLoginButton = findViewById(R.id.auth_main_btn_facebook);
        mGoogleSignInButton = findViewById(R.id.auth_main_btn_google);
        phoneButton.setOnClickListener(this);
        mGoogleSignInButton.setOnClickListener(this);
        signIn = findViewById(R.id.auth_main_btn_signin);
        googleCustomButton = findViewById(R.id.auth_main_btn_google_custom);
        facebookCustomButton = findViewById(R.id.auth_main_btn_facebook_custom);
        statusTextView = findViewById(R.id.auth_main_tv);
        checkPPTextView = findViewById(R.id.textView82);
        ppCheckBox = findViewById(R.id.auth_main_check_pp);
        backButton = findViewById(R.id.btnBack);




        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ppCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAcceptedPrivacyPolicy = b;
                if (b) checkPPTextView.setTextColor(Color.parseColor("#A63A3A3A"));
                Log.d(TAG, "onCheckedChanged: PP = " + b);
            }
        });
        ppCheckBox.performClick();



        checkPPTextView.setOnClickListener(this);

        googleCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        facebookCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPP()) {
                    facebookLoginButton.performClick();
                }
            }
        });

        if (createUser) {
            intent = new Intent(this, ActivitySplash.class).
                    putExtra(Config.INTENT_PROFILE,getIntent().getSerializableExtra(Config.INTENT_PROFILE))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            statusTextView.setText(R.string.auth_main_reg_tv);
            signIn.setText(R.string.auth_main_btn_create);
            phoneButton.setText(R.string.auth_main_reg_phone);
            googleCustomButton.setText(R.string.auth_main_reg_google);
            facebookCustomButton.setText(R.string.auth_main_reg_facebook);
            emailEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.registration_icon_email, 0, 0, 0);

        }else {
            checkPPTextView.setVisibility(View.INVISIBLE);
            ppCheckBox.setVisibility(View.INVISIBLE);
            isAcceptedPrivacyPolicy = true;
            intent = new Intent(this, ActivitySplash.class).
                    putExtra(Config.INTENT_PROFILE,getIntent().getSerializableExtra(Config.INTENT_PROFILE));
            resPassTextView.setVisibility(View.VISIBLE);
            resPassTextView.setOnClickListener(this);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    startPrem();

                    if (getIntent().getSerializableExtra(Config.INTENT_PROFILE) != null) {
                        Adjust.trackEvent(new AdjustEvent(EventsAdjust.create_acount));
                        Amplitude.getInstance().logEvent(AmplitudaEvents.create_acount);
                        WorkWithFirebaseDB.putProfileValue((Profile) getIntent().getSerializableExtra(Config.INTENT_PROFILE));
                    }


                   checkUserExist(user.getUid());



                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                hasConnection(ActivityAuthMain.this);

            }
        });
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]

                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential

                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]

                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
//                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
//                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

private ValueEventListener getPostListener(){
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

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void signInGoogle() {

        if (!isPP()){
            return;
        }

        Log.d(TAG, "signInGoogle: 1");
        mGoogleSignInClient.signOut();

        Log.d(TAG, "signInGoogle: 2");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "signInGoogle: 3");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

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
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (email.matches("")) {
            Toast.makeText(ActivityAuthMain.this, "Пропущен email!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "signIn: fail valid");
            return;
        } else if (!Valid.isValidEmail(email)) {
            Toast.makeText(ActivityAuthMain.this, "Проверь введенный email!", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.matches("")){

            Toast.makeText(ActivityAuthMain.this, "Пропущен пароль!", Toast.LENGTH_SHORT).show();
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

    private void signOutAll(){
        Log.d(TAG, "signOutAll: ");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();

    }
    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (email.matches("")) {
            Toast.makeText(ActivityAuthMain.this, "Пропущен email!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "createAccount: fail valid");
            return;
        } else if (!Valid.isValidEmail(email)) {
            Toast.makeText(ActivityAuthMain.this, "Проверь введенный email!", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.matches("")){

            Toast.makeText(ActivityAuthMain.this, "Пропущен пароль!", Toast.LENGTH_SHORT).show();
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
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword) {
                                Log.d(TAG, "onComplete: weak_password");
                                Toast.makeText(ActivityAuthMain.this, "Слишком простой пароль!", Toast.LENGTH_SHORT).show();
                                // TODO: take your actions!
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d(TAG, "onComplete: malformed_email");

                                // TODO: Take your action
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d(TAG, "onComplete: exist_email");
                                Toast.makeText(ActivityAuthMain.this, "email уже используется!", Toast.LENGTH_SHORT).show();
                                // TODO: Take your action
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }


                    }});
    }

    private void checkUserExist(String uid){

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//            Post post = dataSnapshot.getValue(Post.class);
                profile = dataSnapshot.getValue(Profile.class);
                checkProfile(profile);
                if (profile != null)
                Log.d(TAG, "onDataChange: " + profile.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

       mDatabase.child(uid).child("profile").addListenerForSingleValueEvent(postListener);

    }

    private void checkProfile(Profile profile){

        if (alertDialogPhone != null){
            alertDialogPhone.dismiss();
        }

        if (profile == null){
            new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.auth_main_alert_title))
                .setMessage(getString(R.string.auth_main_alert_body))
                .setPositiveButton(getString(R.string.auth_main_alert_ok), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOutAll();
                    }

                })
                .show();

//            Toast.makeText(ActivityAuthMain.this, "Зарегай акк!!!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "checkUserExist: false");

        } else {
//            Toast.makeText(ActivityAuthMain.this, "Приветствую!!!", Toast.LENGTH_SHORT).show();

            if (isPP() && createUser){
                Log.d(TAG, "logEvent: acept_police");
                 Amplitude.getInstance().logEvent(AmplitudaEvents.acept_police);
                 Adjust.trackEvent(new AdjustEvent(EventsAdjust.acept_police));
            }
            Log.d(TAG, "checkUserExist: true");
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void phoneAuth(){

        isSendCode = false;

        View view = getLayoutInflater().inflate( R.layout.alert_dialog_phone_auth, null);

        TextView infoTextView = view.findViewById(R.id.auth_phone_tv);
        EditText phoneNumberEditText = view.findViewById(R.id.auth_phone_et_number);
        EditText codeEditText = view.findViewById(R.id.auth_phone_et_code);
        Button okButton = view.findViewById(R.id.auth_phone_btn_ok);
        Button cancelButton = view.findViewById(R.id.auth_phone_btn_cancel);



        alertDialogPhone = new AlertDialog.Builder(this)
                .setView(view).show();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:");
                switch (view.getId()){
                    case R.id.auth_phone_btn_cancel:
                        alertDialogPhone.dismiss();
                        break;
                    case R.id.auth_phone_btn_ok:

                        if (!isSendCode){
                            String phone = phoneNumberEditText.getText().toString();
                            if (Valid.isValidPhone(phone)){
                                startPhoneNumberVerification(phone);
                                codeEditText.setEnabled(true);
                                phoneNumberEditText.setEnabled(false);
                                infoTextView.setText(R.string.auth_main_phone_text_set_code);
                                okButton.setText(R.string.auth_main_phone_btn_sign_in);
                                isSendCode = true;
                            } else {
                                Toast.makeText(ActivityAuthMain.this, "Проверьте введенный номер!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            String code = codeEditText.getText().toString();
                            if (Valid.isValidCode(code)){
                                verifyPhoneNumberWithCode(mVerificationId, code);
                            }else {
                                Toast.makeText(ActivityAuthMain.this, "Проверьте введенный код!", Toast.LENGTH_SHORT).show();
                            }

                        }
                        break;
                }

            }
        };

        cancelButton.setOnClickListener(listener);
        okButton.setOnClickListener(listener);

    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        Log.d(TAG, "startPhoneNumberVerification: " + phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]

                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                Log.d(TAG, "onComplete: Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            Log.d(TAG, "onComplete: STATE_SIGNIN_FAILED");
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]




    private boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        Toast.makeText(ActivityAuthMain.this, "Интернет отключен!", Toast.LENGTH_SHORT).show();
        return false;
    }



    private boolean isPP(){

        if (!isAcceptedPrivacyPolicy){
            checkPPTextView.setTextColor(Color.RED);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.auth_main_btn_signin:
                if ( createUser) {
                    if (hasConnection(this) && isPP())
                    createAccount(emailEditText.getText().toString(),
                            passEditText.getText().toString());
                } else {
                    if (hasConnection(this))
                    signIn(emailEditText.getText().toString(),
                            passEditText.getText().toString());
                }
                break;
            case R.id.auth_main_btn_google:
                if (hasConnection(this))
                signInGoogle();
                break;
            case R.id.auth_main_tv_respasss:
                startActivity(new Intent(this, ActivityForgotPassword.class));
                break;
            case R.id.auth_main_btn_phone:
                if (hasConnection(this) && isPP())
                phoneAuth();
                break;
            case R.id.textView82:
                startActivity(new Intent(this, ActivityPrivacyPolicy.class));
                break;
        }
    }



    public class MyFilter implements InputFilter {
        Pattern mPattern;

        public MyFilter(String idFormatRegex) {
            mPattern = Pattern.compile(idFormatRegex);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}
