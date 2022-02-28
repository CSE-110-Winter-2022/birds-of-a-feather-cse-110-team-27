package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.ProfileCoursesViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
//    private final List<? extends UserWithCourses> persons;
//
//    public ProfileActivity(List<? extends UserWithCourses> persons) {
//        super();
//        this.persons = persons;
//    }

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
        Picasso.get().load(profilePictureUrl).error(R.drawable.default_pfp).into(imageView);

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

    public void onToggleFavorite(View view) {
        int userId = getIntent().getIntExtra("userId", -1);
        AppDatabase db = AppDatabase.singleton(this);

        UserWithCourses user = db.userWithCoursesDao().getUser(userId);
//        user.toggleFavorite();
        user.setName("test");

    }
}