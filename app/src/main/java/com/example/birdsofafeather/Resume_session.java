package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.IUser;

import java.util.List;

public class Resume_session extends AppCompatActivity {
    private AppDatabase db;
    private IUser user;
    private RecyclerView sessionRecyclerView;
    private RecyclerView.LayoutManager sessionLayoutManager;
    private SessionViewAdapter sessionViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_session);

        Intent intent = getIntent();
        int personId = intent.getIntExtra("user_id", 0);
        db = AppDatabase.singleton(this);
        List<SessionWithUsers> sessions = db.sessionWithUsersDao().getAllSessions();

        sessionRecyclerView = findViewById(R.id.session_recycler);
        sessionLayoutManager = new LinearLayoutManager(this);
        sessionRecyclerView.setLayoutManager(sessionLayoutManager);

        sessionViewAdapter = new SessionViewAdapter(sessions, personId);
        sessionRecyclerView.setAdapter(sessionViewAdapter);
    }
}