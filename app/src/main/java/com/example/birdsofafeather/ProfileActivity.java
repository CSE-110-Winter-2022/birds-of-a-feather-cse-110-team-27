package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.utils.CoursesViewAdapter;
import com.example.birdsofafeather.utils.ProfileCoursesViewAdapter;
import com.example.birdsofafeather.utils.Utilities;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileActivity extends AppCompatActivity {

    @SuppressLint("WrongThread")
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

        ImageView imageView = findViewById(R.id.profile_details_picture);
        Picasso.get().load(profilePictureUrl).into(imageView);

        System.out.println(courseNames);
        RecyclerView coursesRecyclerView = findViewById(R.id.profile_details_courses);
        RecyclerView.LayoutManager coursesLayoutManager = new LinearLayoutManager( this) ;
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);
        ProfileCoursesViewAdapter coursesViewAdapter = new ProfileCoursesViewAdapter(courseNames);
        coursesRecyclerView.setAdapter(coursesViewAdapter);


    }

    public void onGoBackFromProfileClicked(View view) {
        this.finish();
    }
}