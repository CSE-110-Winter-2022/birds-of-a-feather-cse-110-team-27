package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.IUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
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
        long personId = intent.getLongExtra("user_id", 0);
        db = AppDatabase.singleton(this);
        List<SessionWithUsers> sessions = db.sessionWithUsersDao().getAllSessions();
        List<SessionWithUsers> sessions_to_display = new LinkedList<>();
        Date maxDate = new Date(0);
        SessionWithUsers maxDateSession = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        for(SessionWithUsers session : sessions) {
            if(session.getSession().hasName()) {
                sessions_to_display.add(session);
            } else {
                try {
                    Date session_date = sdf.parse(session.getSession().getDate());
                    if(session_date.after(maxDate)) {
                        maxDate = session_date;
                        maxDateSession = session;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if(maxDateSession != null) {
            sessions_to_display.add(0, maxDateSession);
        }

        sessionRecyclerView = findViewById(R.id.session_recycler);
        sessionLayoutManager = new LinearLayoutManager(this);
        sessionRecyclerView.setLayoutManager(sessionLayoutManager);

        sessionViewAdapter = new SessionViewAdapter(sessions_to_display, personId);
        sessionRecyclerView.setAdapter(sessionViewAdapter);
    }
}