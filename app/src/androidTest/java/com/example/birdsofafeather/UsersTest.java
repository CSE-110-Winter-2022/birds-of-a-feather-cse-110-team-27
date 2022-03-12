package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

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

    }
