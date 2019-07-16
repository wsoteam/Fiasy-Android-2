package com.wsoteam.diet.presentation.profile.about;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends MvpAppCompatActivity implements AboutView {
    @Inject
    @InjectPresenter
    AboutPresenter aboutPresenter;
    private static final int CAMERA_REQUEST = 1888;
    @BindView(R.id.civProfile) CircleImageView civProfile;
    @BindView(R.id.edtName) EditText edtName;
    @BindView(R.id.edtSecondName) EditText edtSecondName;
    @BindView(R.id.edtEmail) EditText edtEmail;
    @BindView(R.id.edtAge) EditText edtAge;
    @BindView(R.id.edtWeight) EditText edtWeight;
    @BindView(R.id.edtHeight) EditText edtHeight;


    @ProvidePresenter
    AboutPresenter providePresenter() {
        return aboutPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
    }

    @Override
    public void bindFields(Profile profile) {
        if (profile.getFirstName() != null && !profile.getFirstName().equals("default")){
            edtName.setText(profile.getFirstName());
        }
        if (profile.getLastName() != null && !profile.getLastName().equals("default")){
            edtSecondName.setText(profile.getLastName());
        }
        if (profile.getEmail() != null){
            edtEmail.setText(profile.getEmail());
        }
        edtAge.setText(String.valueOf(profile.getAge()));
        edtWeight.setText(String.valueOf(profile.getWeight()));
        edtHeight.setText(String.valueOf(profile.getHeight()));
        if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().equals("default")) {
            Glide.with(this).load(profile.getPhotoUrl()).into(civProfile);
        } else {
            if (profile.isFemale()) {
                Glide.with(this).load(R.drawable.female_avatar).into(civProfile);
            } else {
                Glide.with(this).load(R.drawable.male_avatar).into(civProfile);
            }
        }
    }

    @OnClick({R.id.ibBack, R.id.ibMakeImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                onBackPressed();
                break;
            case R.id.ibMakeImage:
                callCamera();
                break;
        }
    }

    private void callCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            aboutPresenter.uploadPhoto((Bitmap) data.getExtras().get("data"));
            Glide.with(this).load((Bitmap) data.getExtras().get("data")).into(civProfile);
        }
    }
}
