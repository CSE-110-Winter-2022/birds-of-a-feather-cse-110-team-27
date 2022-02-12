package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.IUser;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.ArrayList;
import java.util.List;

public class DisplayOtherUserActivity extends AppCompatActivity {

    protected RecyclerView personsRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected PersonsViewAdapter personsViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_other_user_info);
        setTitle("Nearby");

        MockUserWithCourses mockUser0 = new MockUserWithCourses(0);
        MockUserWithCourses mockUser1 = new MockUserWithCourses(1);
        MockUserWithCourses mockUser2 = new MockUserWithCourses(2);

        List<UserWithCourses> dataList = new ArrayList<UserWithCourses>();
        dataList.add(mockUser0.getUserWithCourses());
        dataList.add(mockUser1.getUserWithCourses());
        dataList.add(mockUser2.getUserWithCourses());

        personsRecyclerView = findViewById(R.id.persons_view);

        personsLayoutManager = new LinearLayoutManager(this);
        personsRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new PersonsViewAdapter(dataList);
        personsRecyclerView.setAdapter(personsViewAdapter);
    }

}