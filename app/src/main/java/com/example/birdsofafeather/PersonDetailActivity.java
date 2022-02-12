package com.example.birdsofafeather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.IUser;

import java.util.List;


public class PersonDetailActivity extends AppCompatActivity {
    private AppDatabase db;


    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager notesLayoutManager;
    private OtherCoursesViewAdapter otherCoursesViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Intent intent = getIntent();
        String[] courses_array = intent.getStringArrayExtra("courses_array");

        setTitle("Courses");

        notesRecyclerView = findViewById(R.id.notes_view);
        notesLayoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setLayoutManager(notesLayoutManager);

        otherCoursesViewAdapter = new OtherCoursesViewAdapter(courses_array);
        notesRecyclerView.setAdapter(otherCoursesViewAdapter);
    }

}