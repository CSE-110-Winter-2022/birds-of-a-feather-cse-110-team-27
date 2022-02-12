package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.List;

public class DisplayOtherUserInfo extends AppCompatActivity {

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_other_user_info);
        setTitle("Nearby");

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<? extends UserWithCourses> dataList = db.userWithCoursesDao().getAll();

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(dataList);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }

}