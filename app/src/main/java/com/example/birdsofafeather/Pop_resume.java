package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsers;

public class Pop_resume extends Activity {
    private int test_user_id;

    protected void onCreate(Bundle savedInstanceState){

        Intent intent = getIntent();
        test_user_id = intent.getIntExtra("user_id", -1);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_window_resume);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.3));

    }

    public void newClicked(View view) {
        AppDatabase db = AppDatabase.singleton(this);
        long session_id = db.sessionWithUsersDao().insertSession(new Session());
        SessionWithUsers sessionWithUsers = db.sessionWithUsersDao().getForId(session_id);
        sessionWithUsers.setSessionName("CSE 110");

        Intent intent = new Intent(this, FindNearbyActivity.class);
        intent.putExtra("user_id", test_user_id);
        startActivity(intent);
    }

    public void resumeClicked(View view) {
        Intent intent = new Intent(this, Resume_session.class);
        intent.putExtra("user_id", test_user_id);
        startActivity(intent);
        finish();
    }
}
