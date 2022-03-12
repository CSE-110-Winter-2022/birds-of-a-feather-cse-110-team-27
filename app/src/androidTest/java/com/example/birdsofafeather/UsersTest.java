package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import org.junit.Test;
import org.junit.runner.RunWith;

public class UsersTest {
    @Test
    public void getUserForEmailTest() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);

        User newUser1 = new User("User Name", "A@ucsd.edu", "");
        long userId1 = db.userWithCoursesDao().insert(newUser1);

        assertNotNull(db.userWithCoursesDao().getUserForEmail("A@ucsd.edu"));

        User newUser2 = new User("User B", "B@ucsd.edu", "");
        long userID2 = db.userWithCoursesDao().insert(newUser2);

        assertNotNull(db.userWithCoursesDao().getUserForEmail("A@ucsd.edu"));
        assertNotNull(db.userWithCoursesDao().getUserForEmail("B@ucsd.edu"));

        assertEquals(db.userWithCoursesDao().getUserForEmail("A@ucsd.edu").getName(), "User Name");
        assertEquals(db.userWithCoursesDao().getUserForEmail("B@ucsd.edu").getName(), "User B");

    }

        @Test
        public void checkUserWasAddedToDB() {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            assertEquals("com.example.birdsofafeather", appContext.getPackageName());

            AppDatabase db = AppDatabase.singleton(appContext);
            User newUser = new User("User Name", "userEmail@ucsd.edu", "");
            long userId = db.userWithCoursesDao().insert(newUser);

            UserWithCourses user = db.userWithCoursesDao().getUser(userId);
            assertEquals("User Name", user.getName());
            assertEquals("userEmail@ucsd.edu", user.getEmail());
            assertEquals(true, user.getCourses().isEmpty());
        }
        @Test
        public void MaxIDTest() {
        // Context of the app under test.
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            assertEquals("com.example.birdsofafeather", appContext.getPackageName());

            AppDatabase db = AppDatabase.singleton(appContext);

            User newUser1 = new User("User Name", "userEmail@ucsd.edu", "");
            long userId1 = db.userWithCoursesDao().insert(newUser1);

            assertEquals(db.userWithCoursesDao().maxId(), userId1);

            User newUser2 = new User("User B", "B@ucsd.edu", "");
            long userID2 = db.userWithCoursesDao().insert(newUser2);

            assertEquals(db.userWithCoursesDao().maxId(), Math.max(userId1, userID2));

        }
        @Test
        public void getUserTest() {
        // Context of the app under test.
            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            assertEquals("com.example.birdsofafeather", appContext.getPackageName());

            AppDatabase db = AppDatabase.singleton(appContext);

            User newUser1 = new User("User Name", "userEmail@ucsd.edu", "");
            long userId1 = db.userWithCoursesDao().insert(newUser1);

            assertNotNull(db.userWithCoursesDao().getUser(userId1));

            User newUser2 = new User("User B", "B@ucsd.edu", "");
            long userID2 = db.userWithCoursesDao().insert(newUser2);

            assertNotNull(db.userWithCoursesDao().getUser(userId1));
            assertNotNull(db.userWithCoursesDao().getUser(userID2));

            assertEquals(db.userWithCoursesDao().getUser(userId1).getName(), "User Name");
            assertEquals(db.userWithCoursesDao().getUser(userID2).getName(), "User B");

        }

}
