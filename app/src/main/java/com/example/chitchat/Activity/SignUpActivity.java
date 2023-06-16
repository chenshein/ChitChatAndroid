package com.example.chitchat.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 1;

    private UserDatabase db;
    TextView go_to_login;
    Button sign_up_button;
    EditText username, password, confirm_password, displayName;
    CircleImageView photo_upload;

    //regex
    String usernamePattern = "^[a-zA-Z0-9_-]{3,16}$";
    String passwordPattern = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9!@#$%^&*()_+]{8,}$";
    String displayPattern = "^[a-zA-Z0-9_\\-][a-zA-Z0-9_\\- ]{1,14}[a-zA-Z0-9_\\-]$";

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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
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
                                UserEntity new_user = new UserEntity(Username, Password, DisplayName,"test");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Handle the selected image and update the profile image
            // You can use a library like Picasso or Glide to load the image into the CircleImageView
            Picasso.get().load(selectedImageUri).into(photo_upload);
        }
    }
    private boolean check_if_user_exist(String username) {
        UserEntity userEntity = userDao.get(username);
        return userEntity != null;
    }
}
