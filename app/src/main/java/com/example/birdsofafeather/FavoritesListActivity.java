package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.ArrayList;

public class FavoritesListActivity extends AppCompatActivity {

    private AppDatabase db;

    private RecyclerView favoritesRecyclerView;
    private RecyclerView.LayoutManager favoritesLayoutManager;
    private PersonsViewAdapter favoriteViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_list);

        db = AppDatabase.singleton(this);

        Intent intent = getIntent();
        long[] favoriteUserIds = intent.getLongArrayExtra("favoritesList");
        ArrayList<UserWithCourses> favorites = new ArrayList<UserWithCourses>();
        for(long userID: favoriteUserIds){
            favorites.add(db.userWithCoursesDao().getUser(userID));
        }

        favoritesRecyclerView = findViewById(R.id.favorites_view);
        favoritesLayoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(favoritesLayoutManager);

        favoriteViewAdapter = new PersonsViewAdapter(favorites, 1, null);
        favoritesRecyclerView.setAdapter(favoriteViewAdapter);
    }
}