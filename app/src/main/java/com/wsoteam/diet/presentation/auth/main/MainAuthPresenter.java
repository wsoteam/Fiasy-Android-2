package com.wsoteam.diet.presentation.auth.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Authenticate.Valid;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BasePresenter;

import java.util.concurrent.TimeUnit;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainAuthPresenter extends BasePresenter<MainAuthView> {

    private static final String TAG = "Authenticate";
    DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.w(TAG, "onVerificationFailed", e);
            getViewState().showMessage(e.getMessage());
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
            } else if (e instanceof FirebaseTooManyRequestsException) {
                getViewState().showSnackBar("Quota exceeded");
            }
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            Log.d(TAG, "onCodeSent:" + verificationId);
            getViewState().authPhoneVerificationId(verificationId);
        }
    };
    private Context context;
    private Router router;

    public MainAuthPresenter(Context context, Router router) {
        this.router = router;
        this.context = context;
    }

    void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("USER_LIST");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mAuth = FirebaseAuth.getInstance();
    }

    int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    void signIn(String email, String password) {
        if (checkCredentials(email, password))
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
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
                    });

    }


    void createAccount(String email, String password) {
        if (checkCredentials(email, password))
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
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
                                getViewState().showMessage(context.getString(R.string.auth_easy_pass));
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                Log.d(TAG, "onComplete: malformed_email");
                            } catch (FirebaseAuthUserCollisionException existEmail) {
                                Log.d(TAG, "onComplete: exist_email");
                                getViewState().showMessage(context.getString(R.string.auth_in_use_email));
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        }
                    });
    }

    boolean checkCredentials(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (email.matches("")) {
            getViewState().showMessage(context.getString(R.string.auth_empty_email));
            Log.d(TAG, "signIn: fail valid");
            return false;
        } else if (!Valid.isValidEmail(email)) {
            getViewState().showMessage(context.getString(R.string.auth_wrong_email));
            return false;
        } else if (password.matches("")) {
            getViewState().showMessage(context.getString(R.string.auth_empty_pass));
            return false;
        }
        return true;
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Log.d(TAG, "onComplete: Invalid code.");
                        }
                        // Update UI
                        Log.d(TAG, "onComplete: STATE_SIGNIN_FAILED");
                    }
                });
    }

    void startPhoneNumberVerification(Activity activity, String phoneNumber) {
        Log.d(TAG, "startPhoneNumberVerification: " + phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    void resendVerificationCode(Activity activity, String phoneNumber,
                                PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    void signOutAll() {
        Log.d(TAG, "signOutAll: ");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
    }


    void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }
}