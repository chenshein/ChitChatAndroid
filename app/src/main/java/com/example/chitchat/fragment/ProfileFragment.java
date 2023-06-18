package com.example.chitchat.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.chitchat.Activity.MainActivity;
import com.example.chitchat.Activity.SearchUserActivity;
import com.example.chitchat.R;
import com.example.chitchat.data.User.UserDao;
import com.example.chitchat.data.User.UserDatabase;
import com.example.chitchat.data.User.UserEntity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private EditText usernameEditText;
    private EditText displayNameEditText;
    private ImageButton logout_btn;

    private CircleImageView profilePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        profilePic = view.findViewById(R.id.profile_profilePic);
        usernameEditText = view.findViewById(R.id.profile_username);
        displayNameEditText = view.findViewById(R.id.profile_displayName);
        logout_btn = view.findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Set the current user's username and display name
        String currentUserUsername = getCurrentUserUsername();
        new DatabaseAsyncTask().execute(currentUserUsername);

        return view;
    }


    private class DatabaseAsyncTask extends AsyncTask<String, Void, UserEntity> {

        @Override
        protected UserEntity doInBackground(String... params) {
            // Access the database and retrieve the current user
            String currentUserUsername = params[0];
            UserDatabase userDatabase = UserDatabase.getUserDatabase(requireContext());
            UserDao userDao = userDatabase.userDao();
            return userDao.get(currentUserUsername);
        }

        @Override
        protected void onPostExecute(UserEntity curr_user) {
            // Update the UI with the retrieved user data
            if (curr_user != null) {
                usernameEditText.setText(curr_user.getUsername());
                displayNameEditText.setText(curr_user.getDisplayName());
                String userImageUrl = curr_user.getProfilePic();
                byte[] decodedImage = Base64.decode(userImageUrl, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
                profilePic.setImageBitmap(bitmap);

            }
        }
    }

    private String getCurrentUserUsername() {
        Bundle args = getArguments();
        if (args != null) {
            return args.getString("username");
        } else {
            return "";
        }
    }
}
