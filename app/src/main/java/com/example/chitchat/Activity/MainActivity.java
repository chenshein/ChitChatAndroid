package com.example.chitchat.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chitchat.R;
import com.example.chitchat.data.User.UserDatabase;

public class MainActivity extends AppCompatActivity {

    private UserDatabase db;
    private Object userDao;

    private Button btnSettings;
    private SharedPreferences sharedPreferences;
    private String server;
    private int themeId;

    @Override
    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        btnSettings = findViewById(R.id.btnSettings);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        server = sharedPreferences.getString("server", "");
        themeId = sharedPreferences.getInt("theme", R.id.radioLight);

        applyTheme(themeId);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        Button login_button = findViewById(R.id.login_button_main);
        login_button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Button sign_up_button = findViewById(R.id.signUp_button_main);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void applyTheme(int themeId) {
        // Apply the selected theme
        if (themeId == R.id.radioDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the latest settings from SharedPreferences
        String updatedServer = sharedPreferences.getString("server", "");
        int updatedThemeId = sharedPreferences.getInt("theme", R.id.radioLight);

        // Check if the settings have changed
        if (!server.equals(updatedServer) || themeId != updatedThemeId) {
            server = updatedServer;
            themeId = updatedThemeId;

            applyTheme(themeId);
            recreate(); // Recreate the activity to apply the new theme
        }
    }
}
