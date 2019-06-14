package com.wsoteam.diet.presentation.auth.main;

import android.content.Context;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wsoteam.diet.Authenticate.Valid;
import com.wsoteam.diet.R;
import com.wsoteam.diet.presentation.global.BasePresenter;
import com.wsoteam.diet.presentation.global.Screens;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainAuthPresenter extends BasePresenter<MainAuthView> {

    private static final String TAG = "Authenticate";
    DatabaseReference mDatabase;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    private Context context;
    private Router router;

    public MainAuthPresenter(Context context, Router router) {
        this.router = router;
        this.context = context;
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

    void signIn(String email, String password) {
        email = email.trim();
        if (checkCredentials(email, password)) {
            getViewState().showProgress(true);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        getViewState().showProgress(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithEmail:success " + user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ", task.getException());
                            getViewState().showMessage(context.getString(R.string.auth_wrong_credentials));
                        }

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "auth_failed ");
                        }
                    });
        }
    }

    void createAccount(String email, String password) {
        email = email.trim();
        if (checkCredentials(email, password)) {
            getViewState().showProgress(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        getViewState().showProgress(false);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            getViewState().showMessage(context.getString(R.string.auth_wrong_credentials));
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
    }

    void resetPassword(String email) {
        getViewState().showProgress(true);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    getViewState().showProgress(false);
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent." + email);
                        getViewState().showMessage(context.getString(R.string.forgot_pass_check_email));
                        getViewState().goBackToSignIn();
                    } else {
                        Log.d(TAG, "Error");
                    }
                }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthInvalidUserException) {
                getViewState().showMessage(context.getString(R.string.forgot_pass_wrong_user));
            }
            Log.d(TAG, String.valueOf(e.getClass()));
        });
    }

    private boolean checkCredentials(String email, String password) {
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
        } else if (password.contains(" ")) {
            getViewState().showMessage(context.getString(R.string.auth_pass_with_space));
            return false;
        }
        return true;
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

     void firebaseAuthWithInstagram(String auth_token) {
        mAuth.signInWithCustomToken(auth_token)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCustomToken:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    }
                });
    }

    void onPrivacyPolicyClicked() {
        router.navigateTo(new Screens.PrivacyPolicyScreen());
    }

}