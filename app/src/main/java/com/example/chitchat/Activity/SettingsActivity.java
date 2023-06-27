package com.example.chitchat.Activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chitchat.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText editServer;
    private RadioGroup radioGroup;
    private RadioButton radioLight, radioDark;
    private Button btnSave;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editServer = findViewById(R.id.editServer);
        radioGroup = findViewById(R.id.radioGroup);
        radioLight = findViewById(R.id.radioLight);
        radioDark = findViewById(R.id.radioDark);
        btnSave = findViewById(R.id.btnSave);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Load saved settings
        String savedServer = sharedPreferences.getString("server", "");
        int savedTheme = sharedPreferences.getInt("theme", R.id.radioLight);

        editServer.setText(savedServer);
        radioGroup.check(savedTheme);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the settings
                String server = editServer.getText().toString();
                int selectedTheme = radioGroup.getCheckedRadioButtonId();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("server", server);
                editor.putInt("theme", selectedTheme);
                editor.apply();

                applyTheme(selectedTheme); // Apply the selected theme

                // Update the server address
                updateServer(server);

                finish();
            }
        });
    }

    private void applyTheme(int selectedTheme) {
        if (selectedTheme == R.id.radioDark) {
            // Set dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            // Set light theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        // Restart the activity to apply the theme changes
        recreate();
    }

    private void updateServer(String server) {
        // Update the server address
        String baseUrl = getString(R.string.BaseUrl);
        String updatedBaseUrl = baseUrl.replaceFirst("http://.*?/", "http://" + server + "/");
        // Update the value of BaseUrl in strings.xml
        Resources res = getResources();
        int resId = res.getIdentifier("BaseUrl", "string", getPackageName());
        if (resId != 0) {
            res.getString(resId, updatedBaseUrl);
        }
    }
}