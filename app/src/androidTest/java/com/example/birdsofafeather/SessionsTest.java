package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SessionsTest {
    @Test
    public void checkUserWSession(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User Gary = new User("Gary", "GG@ucsd.edu", "");
        Gary.setSessionId(9527);
        assertEquals(9527, Gary.getSessionId());
    }
    @Test

    public void checkSessionWUsers() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User A = new User("A", "a@ucsd.edu", "");
        long Aid = db.userWithCoursesDao().insert(A);
        User B = new User("B", "b@ucsd.edu", "");
        long Bid = db.userWithCoursesDao().insert(B);
        User C = new User("C", "b@ucsd.edu", "");
        long Cid = db.userWithCoursesDao().insert(C);

        UserWithCourses Adata = db.userWithCoursesDao().getUser(Aid);
        UserWithCourses Bdata = db.userWithCoursesDao().getUser(Bid);
        UserWithCourses Cdata = db.userWithCoursesDao().getUser(Cid);

        Session session = new Session("session");
        long sessionid = db.sessionWithUsersDao().insertSession(session);
        List<UserWithCourses> listOfUsers = new ArrayList<UserWithCourses>();
        listOfUsers.add(Adata);
        listOfUsers.add(Bdata);
        listOfUsers.add(Cdata);

        db.sessionWithUsersDao().addUsersToSession(sessionid, listOfUsers);
        List<User> users = db.sessionWithUsersDao().getUsersForSessionId(sessionid);
        System.out.println(users.toString());
        assertEquals("A", users.get(0).getName());
        assertEquals("B", users.get(1).getName());
        assertEquals("C", users.get(2).getName());
    }
}
