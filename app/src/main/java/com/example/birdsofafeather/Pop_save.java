package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pop_save extends Activity {

    private long test_user_id;
    private long[] user_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Intent intent = getIntent();
        test_user_id = intent.getLongExtra("user_id", -1);
        user_ids = intent.getLongArrayExtra("user_ids");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_window_save);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.3));

    }

    public void saveClicked(View view) {
        TextView sessionNameTextView = findViewById(R.id.current_enrolled_class);
        String sessionName = sessionNameTextView.getText().toString();
        AppDatabase db = AppDatabase.singleton(this);

        long session_id = db.sessionWithUsersDao().insertSession(new Session());
        SessionWithUsers sessionWithUsers = db.sessionWithUsersDao().getForId(session_id);
        sessionWithUsers.setSessionName(sessionName);
//        List<User> users = new ArrayList<>();
        for (long id : user_ids) {
            UserWithCourses user = db.userWithCoursesDao().getUser(id);
            db.sessionWithUsersDao().addUsersToSession(session_id, Arrays.asList(user));
//            users.add(user.user);
        }
//        sessionWithUsers.addUsers(users);
        List<SessionWithUsers> s = db.sessionWithUsersDao().getAllSessions();
        Log.d("Pop_save", "Creating session id: " + session_id + " with name: " + sessionName + " and " + user_ids.length + " users");
        db.sessionWithUsersDao().insertSession(sessionWithUsers.getSession());

        SessionWithUsers testS = db.sessionWithUsersDao().getForId(session_id);
        List<User> u = testS.getUsers();

        System.out.println();
        finish();
    }
}
