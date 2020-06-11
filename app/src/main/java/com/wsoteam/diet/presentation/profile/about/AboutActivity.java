package com.wsoteam.diet.presentation.profile.about;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Spannable;
import android.text.TextUtils;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.R;
import com.wsoteam.diet.ads.FiasyAds;
import com.wsoteam.diet.ads.nativetemplates.NativeTemplateStyle;
import com.wsoteam.diet.ads.nativetemplates.TemplateView;
import com.wsoteam.diet.presentation.auth.MainAuthNewActivity;
import com.wsoteam.diet.utils.RichTextUtils;
import com.wsoteam.diet.utils.RichTextUtilsKt;


import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends MvpAppCompatActivity implements AboutView {
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    private static final int IMAGE_WIDTH = 390;
    private static final int IMAGE_HEIGHT = 390;

    @BindView(R.id.civProfile)
    CircleImageView civProfile;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtSecondName)
    EditText edtSecondName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilSecondName)
    TextInputLayout tilSecondName;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.nativeAd)
    TemplateView nativeAd;
    @BindView(R.id.signInContainer)
    LinearLayout signInContainer;
    @BindView(R.id.infoForAnonim)
    TextView infoForAnonim;
    @BindView(R.id.signIn)
    Button signIn;
    @BindView(R.id.personalSave)
    TextView personalSave;

    AboutPresenter aboutPresenter;
    private boolean isPhotoUpdate = false;

    @ProvidePresenter
    AboutPresenter providePresenter() {
        return aboutPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);

        aboutPresenter = new AboutPresenter(this);
        aboutPresenter.attachView(this);

        prepareForAnonymous();

        FiasyAds.getLiveDataAdView().observe(this, ad -> {
            if (ad != null) {
                nativeAd.setVisibility(View.VISIBLE);
                nativeAd.setStyles(new NativeTemplateStyle.Builder().build());
                nativeAd.setNativeAd(ad);
            }else {
                nativeAd.setVisibility(View.GONE);
            }
        });
    }


    private void prepareForAnonymous(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.isAnonymous()){
            tilEmail.setVisibility(View.GONE);
            signInContainer.setVisibility(View.VISIBLE);


            RichTextUtils.RichText actionSignIn = new  RichTextUtils.RichText(getString(R.string.signIn).toUpperCase())
                   .colorRes(this, R.color.pumpkin_orange);
            Spannable spannable = RichTextUtilsKt.formatSpannable(getString(R.string.personal_info_text), actionSignIn.text());
            infoForAnonim.setText(spannable);

            signIn.setOnClickListener(v -> startActivity(MainAuthNewActivity.getIntent(this)));

        } else {
            tilEmail.setVisibility(View.VISIBLE);
            signInContainer.setVisibility(View.GONE);
        }

    }


    @OnTextChanged(value = R.id.edtName, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void nameChanged(CharSequence text) {
        checkTextInputLayout(tilName);
    }

    @OnTextChanged(value = R.id.edtSecondName, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void secondChanged(CharSequence text) {
        checkTextInputLayout(tilSecondName);
    }

    @OnTextChanged(value = R.id.edtEmail, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void emailChanged(CharSequence text) {
        checkTextInputLayout(tilEmail);

    }

    private void checkTextInputLayout(TextInputLayout currentTextInputLayout) {
        isDataChanged();
        if (currentTextInputLayout.getError() != null) {
            currentTextInputLayout.setError("");
        }
    }

    private boolean isDataChanged(){
        boolean result = isPhotoUpdate;
        Profile profile = aboutPresenter.getProfile();

        if (!edtName.getText().toString().equals(profile.getFirstName())
                && !edtName.getText().toString().trim().equals("")) result = true;

        if (!edtSecondName.getText().toString().equals(profile.getLastName())
                && !edtSecondName.getText().toString().trim().equals("")) result = true;

        if (!edtEmail.getText().toString().equals(profile.getEmail())
                && !edtEmail.getText().toString().trim().equals("")) result = true;

        personalSave.setEnabled(result);
        if (result) personalSave.setTextColor(ContextCompat.getColor(this, R.color.pumpkin_orange));
        else personalSave.setTextColor(ContextCompat.getColor(this, R.color.search_icon_grey));

        return result;
    }

    @Override
    public void bindFields(Profile profile) {
        if (profile.getFirstName() != null && !profile.getFirstName().equals("")) {
            edtName.setText(profile.getFirstName());
        }
        if (profile.getLastName() != null && !profile.getLastName().equals("default")) {
            edtSecondName.setText(profile.getLastName());
        }
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            edtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        if (profile.getEmail() != null) {
            edtEmail.setText(profile.getEmail());
        }
        if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().equals("default")
            && !TextUtils.isEmpty(profile.getPhotoUrl())) {

            Picasso.get().load(profile.getPhotoUrl()).into(civProfile);
        } else {
            if (profile.isFemale()) {
                Picasso.get().load(R.drawable.female_avatar).into(civProfile);
            } else {
                Picasso.get().load(R.drawable.male_avatar).into(civProfile);
            }
        }
    }

    @OnClick({R.id.ibBack, R.id.ibMakeImage, R.id.personalSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                onBackPressed();
                break;
            case R.id.ibMakeImage:
                callCamera();
                break;
            case R.id.personalSave:
                if (checkValidInputData()) {
                    if (aboutPresenter.calculateAndSave(edtName.getText().toString(),
                            edtSecondName.getText().toString(), edtEmail.getText().toString())) {
                        Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

    private boolean checkValidInputData() {
        boolean result = true;
        Pattern patternEmail = Patterns.EMAIL_ADDRESS;
        if (edtName.getText().toString().equals("") || edtName.getText().toString().replaceAll("\\s+", " ").equals(" ")) {
            tilName.setError(getString(R.string.check_your_name));
            result = false;
        }

        if (!patternEmail.matcher(edtEmail.getText().toString()).matches()) {
            tilEmail.setError(getString(R.string.check_your_email));
            result = false;
        }

        return result;
    }

    private void callCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getString(R.string.load_photo));
            myAlertDialog.setMessage(getString(R.string.where_photo));

            myAlertDialog.setPositiveButton(getString(R.string.phone_memory),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_PICTURE);
                        }
                    });

            myAlertDialog.setNegativeButton(getString(R.string.camera),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            /*if (hasImageCaptureBug()) {
                                i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));
                            } else {
                                i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            }*/
                            startActivityForResult(i, CAMERA_REQUEST);
                        }
                    });
            myAlertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("LOL", "result");
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.e("LOL", "result ok");
            try {
                aboutPresenter.uploadPhoto((Bitmap) data.getExtras().get("data"));
                //Glide.with(this).load((Bitmap) data.getExtras().get("data")).into(civProfile);
                civProfile.setImageBitmap((Bitmap) data.getExtras().get("data"));
                isPhotoUpdate = true;
                isDataChanged();
                //TODO check
                Log.e("LOL", data.getExtras().toString());
                Log.e("LOL", FirebaseAuth.getInstance().getUid());
            } catch (Exception e) {
                Log.e("LOL", e.getMessage());
            }
        } else if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, false);
                int orientation = getOrientation(uri);
                Matrix matrix = new Matrix();
                if (orientation == 90) {
                    matrix.postRotate(90);
                } else if (orientation == 180) {
                    matrix.postRotate(180);
                } else if (orientation == 270) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, matrix, true);
                //Glide.with(this).load(bitmap).into(civProfile);
                civProfile.setImageBitmap(bitmap);
                //TODO check
                aboutPresenter.uploadPhoto(bitmap);
            } catch (Exception ex) {
                Log.e("LOL", ex.getMessage());
            }
        }
    }

    public int getOrientation(Uri photoUri) {
        /* it on the external media. */
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        int result = -1;
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        }

        return result;
    }
}
