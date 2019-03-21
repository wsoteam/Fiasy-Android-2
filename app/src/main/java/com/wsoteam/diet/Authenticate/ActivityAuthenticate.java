package com.wsoteam.diet.Authenticate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.wsoteam.diet.BranchProfile.ActivityEditProfile;
import com.wsoteam.diet.R;

import java.util.Arrays;

public class ActivityAuthenticate extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="Authenticate";

    private FragmentManager fm;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);

        /**snip **/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.wsoteam.diet.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
        //** snip **//

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.auth_frame_layout);
        if (fragment == null) {
            fragment = new FragmentAuthFirst();
            fm.beginTransaction()
                    .add(R.id.auth_frame_layout, fragment)
                    .commit();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.auth_first_btn_registration:
                startActivity(new Intent(this, ActivityEditProfile.class).putExtra("registration",true));
                break;
            case R.id.auth_first_btn_signin:
                startActivity(new Intent(ActivityAuthenticate.this, ActivityAuthMain.class));
                break;
            case R.id.auth_main_btn_google:
//                signInGoogle();
                break;
            case R.id.auth_main_btn_signin:
            {
                String email = ((EditText) fragment.getView().findViewById(R.id.auth_main_email)).getText().toString();
                String pass = ((EditText) fragment.getView().findViewById(R.id.auth_main_pass)).getText().toString();
//                signIn(email, pass);
            }

                break;
            case R.id.auth_main_btn_facebook: {
//                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
//                signInFB();
            }
            case R.id.auth_gender_m:
                fragment = new FragmentAuthMission();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_gender_f:
                fragment = new FragmentAuthMission();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_mission_btn_lose:
                fragment = new FragmentAuthMission();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_mission_btn_get:
                fragment = new FragmentAuthData();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_mission_btn_save:
                fragment = new FragmentAuthData();
                fm.beginTransaction().replace(R.id.auth_frame_layout, fragment).commit();
                break;
            case R.id.auth_data_btn_next:
                Intent intent = new Intent(ActivityAuthenticate.this, ActivityAuthMain.class);
                intent.putExtra("createUser", true);
                startActivity(intent);
                break;

                default:
                    Log.d(TAG, "onClick: switch default");
                    break;
        }
    }
}
