package com.wsoteam.diet.presentation.profile.about;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.wsoteam.diet.POJOProfile.Profile;
import com.wsoteam.diet.POJOSExercises.Ex;
import com.wsoteam.diet.R;

import java.io.File;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends MvpAppCompatActivity implements AboutView {
  @Inject
  @InjectPresenter
  AboutPresenter aboutPresenter;
  private static final int CAMERA_REQUEST = 0;
  private static final int GALLERY_PICTURE = 1;
  @BindView(R.id.civProfile) CircleImageView civProfile;
  @BindView(R.id.edtName) EditText edtName;
  @BindView(R.id.edtSecondName) EditText edtSecondName;
  @BindView(R.id.edtEmail) EditText edtEmail;
  @BindView(R.id.edtAge) EditText edtAge;
  @BindView(R.id.edtWeight) EditText edtWeight;
  @BindView(R.id.edtHeight) EditText edtHeight;
  @BindView(R.id.tilName) TextInputLayout tilName;
  @BindView(R.id.tilSecondName) TextInputLayout tilSecondName;
  @BindView(R.id.tilEmail) TextInputLayout tilEmail;
  @BindView(R.id.tilAge) TextInputLayout tilAge;
  @BindView(R.id.tilWeight) TextInputLayout tilWeight;
  @BindView(R.id.tilHeight) TextInputLayout tilHeight;

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

  @OnTextChanged(value = R.id.edtAge, callback = OnTextChanged.Callback.TEXT_CHANGED)
  public void ageChanged(CharSequence text) {
    checkTextInputLayout(tilAge);
  }

  @OnTextChanged(value = R.id.edtWeight, callback = OnTextChanged.Callback.TEXT_CHANGED)
  public void weightChanged(CharSequence text) {
    checkTextInputLayout(tilWeight);
  }

  @OnTextChanged(value = R.id.edtHeight, callback = OnTextChanged.Callback.TEXT_CHANGED)
  public void heightChanged(CharSequence text) {
    checkTextInputLayout(tilHeight);
  }

  private void checkTextInputLayout(TextInputLayout currentTextInputLayout) {
    if (currentTextInputLayout.getError() != null) {
      currentTextInputLayout.setError("");
    }
  }

  @Override
  public void bindFields(Profile profile) {
    if (profile.getFirstName() != null && !profile.getFirstName().equals("default")) {
      edtName.setText(profile.getFirstName());
    }
    if (profile.getLastName() != null && !profile.getLastName().equals("default")) {
      edtSecondName.setText(profile.getLastName());
    }
    if (profile.getEmail() != null) {
      edtEmail.setText(profile.getEmail());
    }
    edtAge.setText(String.valueOf(profile.getAge()));
    edtWeight.setText(String.valueOf(profile.getWeight()));
    edtHeight.setText(String.valueOf(profile.getHeight()));
    if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
      edtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
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

  @OnClick({ R.id.ibBack, R.id.ibMakeImage, R.id.tvSave })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.ibBack:
        onBackPressed();
        break;
      case R.id.ibMakeImage:
        callCamera();
        break;
      case R.id.tvSave:
        if (checkInputData()) {
          if (aboutPresenter.calculateAndSave(edtName.getText().toString(),
              edtSecondName.getText().toString(),
              edtHeight.getText().toString(), edtWeight.getText().toString(),
              edtAge.getText().toString(), edtEmail.getText().toString())) {
            Toast.makeText(this, R.string.profile_saved, Toast.LENGTH_SHORT).show();
            finish();
          }
        }
        break;
    }
  }

  private boolean checkInputData() {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    if (!edtName.getText().toString().equals("")) {
      if (!edtSecondName.getText().toString().equals("")) {
        if (!edtAge.getText().toString().equals("")
            && Integer.parseInt(edtAge.getText().toString()) >= 9
            && Integer.parseInt(edtAge.getText().toString()) <= 200) {
          if (!edtHeight.getText().toString().equals("")
              && Integer.parseInt(edtHeight.getText().toString()) >= 100
              && Integer.parseInt(edtHeight.getText().toString()) <= 300) {
            if (!edtWeight.getText().toString().equals("")
                && Double.parseDouble(edtWeight.getText().toString()) >= 30
                && Double.parseDouble(edtWeight.getText().toString()) <= 300) {
              if (edtEmail.getText().toString().matches(emailPattern)) {
                return true;
              } else {
                tilEmail.setError(getString(R.string.check_your_email));
                return false;
              }
            } else {
              tilWeight.setError(getString(R.string.spk_check_weight));
              return false;
            }
          } else {
            tilHeight.setError(getString(R.string.spk_check_your_height));
            return false;
          }
        } else {
          tilAge.setError(getString(R.string.spk_check_your_age));
          return false;
        }
      } else {
        tilSecondName.setError(getString(R.string.spk_check_your_second_name));
        return false;
      }
    } else {
      tilName.setError(getString(R.string.check_your_name));
      return false;
    }
  }

  private void callCamera() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.CAMERA }, 1);
    } else {
      AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
      myAlertDialog.setTitle("Загрузка фото");
      myAlertDialog.setMessage("Откуда берем фото?");

      myAlertDialog.setPositiveButton("Галерея",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
              Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                  MediaStore.Images.Media.INTERNAL_CONTENT_URI);
              startActivityForResult(galleryIntent, GALLERY_PICTURE);
            }
          });

      myAlertDialog.setNegativeButton("Камера",
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
              Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
          });
      myAlertDialog.show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
      aboutPresenter.uploadPhoto((Bitmap) data.getExtras().get("data"));
      Glide.with(this).load((Bitmap) data.getExtras().get("data")).into(civProfile);
    } else if (requestCode == GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {
      try {
        Uri selectedImage = data.getData();
        String path = selectedImage.getPath();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Glide.with(this).load(bitmap).into(civProfile);
        aboutPresenter.uploadPhoto(bitmap);
      }catch (Exception ex){

      }
    }
  }
}
