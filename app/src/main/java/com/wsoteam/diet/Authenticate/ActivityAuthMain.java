package com.wsoteam.diet.Authenticate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wsoteam.diet.Config;
import com.wsoteam.diet.InApp.ActivitySubscription;
import com.wsoteam.diet.OtherActivity.ActivitySplash;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.Sync.UserDataHolder;
import com.wsoteam.diet.Sync.WorkWithFirebaseDB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityAuthMain extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "Authenticate";
    private static final int RC_SIGN_IN = 9001;

    private boolean createUser;


    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    private CallbackManager callbackManager;

    private EditText emailEditText;
    private EditText passEditText;
    LoginButton facebookLoginButton;
    SignInButton mGoogleSignInButton;
    Button signIn;

    private Profile profile;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_main);

        createUser = (boolean) getIntent().getBooleanExtra("createUser", false);

        findViewById(R.id.auth_main_btn_signin).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("USER_LIST");


        emailEditText = findViewById(R.id.auth_main_email);
        passEditText = findViewById(R.id.auth_main_pass);
        facebookLoginButton = findViewById(R.id.auth_main_btn_facebook);
        mGoogleSignInButton = findViewById(R.id.auth_main_btn_google);
        mGoogleSignInButton.setOnClickListener(this);
        setGooglePlusButtonText(mGoogleSignInButton, getString(R.string.auth_main_signin_google));
        signIn = findViewById(R.id.auth_main_btn_signin);


        if (createUser) {
            signIn.setText(R.string.auth_main_btn_create);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        intent = new Intent(this, ActivitySplash.class).
                putExtra(Config.INTENT_PROFILE,getIntent().getSerializableExtra(Config.INTENT_PROFILE));
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                    startPrem();

                    if (getIntent().getSerializableExtra(Config.INTENT_PROFILE) != null) {
                        Amplitude.getInstance().logEvent(Config.REGISTRATION);
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
        facebookLoginButton.setText(R.string.auth_main_signin_facebook);
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

            }
        });
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
        } else if (!isValidEmail(email)) {
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
        } else if (!isValidEmail(email)) {
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


//    @Override
//    public void onBackPressed() {
////        new AlertDialog.Builder(this)
////                .setIcon(android.R.drawable.ic_dialog_alert)
////                .setTitle(getString(R.string.exit_alerdialog_title))
////                .setMessage(getString(R.string.exit_alertdialog_body))
////                .setPositiveButton(getString(R.string.exit_alertdialog_btn_yes), new DialogInterface.OnClickListener()
////                {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        finish();
////                    }
////
////                })
////                .setNegativeButton(getString(R.string.exit_alertdialog_btn_no), null)
////                .show();
//    }


    public boolean isValidEmail(String string) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.auth_main_btn_signin:
                if (createUser) {
                    createAccount(emailEditText.getText().toString(),
                            passEditText.getText().toString());
                } else {
                    signIn(emailEditText.getText().toString(),
                            passEditText.getText().toString());
                }
                break;
            case R.id.auth_main_btn_google:
                signInGoogle();
                break;
        }

    }

    private void startPrem(){
        Intent intent = new Intent(ActivityAuthMain.this, ActivitySubscription.class);
        startActivity(intent);
    }
}
