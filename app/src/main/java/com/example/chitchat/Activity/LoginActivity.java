package com.example.chitchat.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chitchat.R;
import com.example.chitchat.api.UserAPI;
import com.example.chitchat.data.LoginCallback;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;

public class LoginActivity extends AppCompatActivity {
    Button button;
    TextView go_to_signUp;
    EditText username,password;
    String usernamePattern = "^[a-zA-Z0-9_-]{3,16}$";
    String passwordPattern = "^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-Z0-9!@#$%^&*()_+]{8,}$";



    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.login_button);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);

        go_to_signUp = findViewById(R.id.isRegister);
        go_to_signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        //click on Login button
        button.setOnClickListener(v -> {
            String Username = username.getText().toString();
            String Password = password.getText().toString();


            // check if this empty strings
            if((TextUtils.isEmpty(Username))){
                Toast.makeText(LoginActivity.this,"Enter the username",Toast.LENGTH_SHORT).show();
            } else if((TextUtils.isEmpty(Password))){
                Toast.makeText(LoginActivity.this,"Enter the password",Toast.LENGTH_SHORT).show();
            } else if(!Username.matches(usernamePattern)){
                username.setError("Give proper username");
            } else if(!Password.matches(passwordPattern)){
                password.setError("Give proper password");
            } else {
                //login
                UserAPI userAPI = new UserAPI();
                UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
                UserDao userDao = userDatabase.userDao();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //check if the user exists in the local database
                        final UserEntity.UserWithPws userEntity = userDao.login(Username, Password);
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userEntity == null) {
                                    Toast.makeText(LoginActivity.this, "Invalid username or/and password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // User exists in the local database, perform login with UserAPI

                                    userAPI.login(Username, Password, new LoginCallback() {
                                        @Override
                                        public void onLoginSuccess(String token) {
                                            // Handle the successful login and token retrieval
                                            Intent intent = new Intent(LoginActivity.this, AllChatsActivity.class);
                                            intent.putExtra("username",Username); //pass the username
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onLoginFailure(String error) {
                                            // Handle the login failure and error

                                        }
                                    });


                                }
                            }
                        });
                    }
                }).start();


            }

        });

    }
}