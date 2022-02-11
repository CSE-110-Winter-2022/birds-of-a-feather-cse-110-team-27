package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        List<String> courseNames = intent.getStringArrayListExtra("course_names");
        String profilePictureUrl = intent.getStringExtra("profile_picture_url");

        TextView nameTextView = findViewById(R.id.profile_details_name);
        nameTextView.setText(name);



    }
}