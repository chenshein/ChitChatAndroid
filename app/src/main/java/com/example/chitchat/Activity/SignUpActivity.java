package com.example.chitchat.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.R;
import com.example.chitchat.api.UserAPI;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int DEFAULT_PROFILE_PHOTO_RES_ID = R.drawable.default_avatar;

    private UserDatabase db;
    TextView go_to_login;
    Button sign_up_button;
    EditText username, password, confirm_password, displayName;
    CircleImageView photo_upload;

    //regex
    String usernamePattern = "^[a-zA-Z0-9_-]{3,16}$";
    String passwordPattern = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9!@#$%^&*()_+]{8,}$";
    String displayPattern = "^[a-zA-Z0-9_\\-][a-zA-Z0-9_\\- ]{1,14}[a-zA-Z0-9_\\-]$";
    private String base64Pic = "";
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        UserDatabase userDatabase = UserDatabase.getUserDatabase(this);
        userDao = userDatabase.userDao();

        sign_up_button = findViewById(R.id.signUp_button);
        username = findViewById(R.id.signUp_username);
        password = findViewById(R.id.signUp_password);
        confirm_password = findViewById(R.id.signUp_confirm_password);
        displayName = findViewById(R.id.signUp_displayName);
        photo_upload = findViewById(R.id.profileImage);

        photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        //if the user clicked on login
        go_to_login = findViewById(R.id.isLogin);
        go_to_login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        //clicked on sign up button
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String Confirm_Password = confirm_password.getText().toString();
                String DisplayName = displayName.getText().toString();

                if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Password) ||
                        TextUtils.isEmpty(Confirm_Password) || TextUtils.isEmpty(DisplayName)) {
                    Toast.makeText(SignUpActivity.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                } else if (!Password.matches(passwordPattern)) {
                    password.setError("Type valid password");
                } else if (!Username.matches(usernamePattern)) {
                    username.setError("Type valid username");
                } else if (!DisplayName.matches(displayPattern)) {
                    displayName.setError("Type valid username");
                } else if (!Password.equals(Confirm_Password)) {
                    password.setError("The password doesn't match");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean userExist = check_if_user_exist(Username);
                            Handler handler = new Handler(getMainLooper()); // Create a handler for the main thread
                            if (userExist) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                if(base64Pic.equals("")){
                                    base64Pic = getDefaultAvatarBase64(); // Set base64Pic to the default avatar
                                }
                                UserEntity new_user = new UserEntity(Username, Password, DisplayName,base64Pic);
                                UserAPI userAPI = new UserAPI();
                                userAPI.registerUser(new_user); //add to database
                                userDao.insert(new_user); // add to local database
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "User registered!", Toast.LENGTH_SHORT).show();

                                        // Go to LoginActivity after successful registration
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private boolean check_if_user_exist(String username) {
        UserEntity userEntity = userDao.get(username);
        return userEntity != null;
    }


    private String getDefaultAvatarBase64() {
        Bitmap defaultAvatarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        defaultAvatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] defaultAvatarBytes = baos.toByteArray();
        return Base64.encodeToString(defaultAvatarBytes, Base64.DEFAULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            base64Pic = getBase64FromImageUri(selectedImageUri, this);

            if (base64Pic != null) {
                Picasso.get().load(selectedImageUri).into(photo_upload);
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static String getBase64FromImageUri(Uri imageUri, Context context) {
        byte[] imageBytes = getStreamByteFromImage(imageUri, context);
        if (imageBytes != null) {
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
        return null;
    }
    public static byte[] getStreamByteFromImage(final Uri imageUri, final Context context) {
        Bitmap photoBitmap = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                photoBitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (photoBitmap != null) {

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        }

        return stream.toByteArray();
    }
}
