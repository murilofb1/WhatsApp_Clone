package com.murilofb.wppclone.settings;


import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.murilofb.wppclone.R;
import com.murilofb.wppclone.helpers.FirebaseH;
import com.murilofb.wppclone.helpers.SecurityH;
import com.murilofb.wppclone.models.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends AppCompatActivity {
    public static final String TAG_SIGNUP = "signupTag";

    private FloatingActionButton fabEditPic;
    private static CircleImageView editUserImage;
    private static EditText edtChangeUserNick;
    private SettingsH settingsH;
    private SecurityH.PermissionsH permissionsH;
    private static boolean isShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fabEditPic = findViewById(R.id.fabEditPic);
        editUserImage = findViewById(R.id.editUserImage);
        edtChangeUserNick = findViewById(R.id.edtChangeUserNick);


        settingsH = new SettingsH(this);
        permissionsH = new SecurityH.PermissionsH(this);

        boolean cameFromSignUp = getIntent().getBooleanExtra(TAG_SIGNUP, false);
        setClickListener();
        if (cameFromSignUp) {
            TextView txtWelcomeTitle = findViewById(R.id.txtWelcomeTitle);
            TextView txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);
            txtWelcomeTitle.setText(R.string.welcome_title);
            txtWelcomeMessage.setText(R.string.welcome_message);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            UserModel currentUser = UserModel.getCurrentUser();
            Log.i("FirebaseH", "profilePic: " + currentUser.getProfileImgLink());
            if (currentUser != null) {
                edtChangeUserNick.setText(currentUser.getName());
                Glide.with(SettingsActivity.this)
                        .load(currentUser.getProfileImgLink())
                        .placeholder(R.drawable.default_user_but_round)
                        .into(editUserImage);
            }
        }
        permissionsH.getUserPermissions();

    }

    @Override
    protected void onStop() {
        super.onStop();
        isShow = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            Bitmap imageBitmap = null;
            Uri imgUri;
            switch (requestCode) {
                case SettingsH.REQUEST_GALLERY_IMG:
                    imgUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SettingsH.REQUEST_CAMERA_IMG:
                    imgUri = Uri.fromFile(new File(SettingsH.getCameraPath()));
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            if (imageBitmap != null) {
                editUserImage.setImageBitmap(imageBitmap);
                FirebaseH firebaseH = new FirebaseH();
                FirebaseH.StorageH storageH = firebaseH.new StorageH();
                storageH.uploadProfileImage(imageBitmap);
            }
            settingsH.dismissDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionsH.REQUEST_ALL_PERMISSIONS) {
            permissionsH.attPermissions(permissions, grantResults);
        }

    }


    private void setClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsH.showPhotoOptionsDialog();
            }
        };
        fabEditPic.setOnClickListener(clickListener);
    }


}
