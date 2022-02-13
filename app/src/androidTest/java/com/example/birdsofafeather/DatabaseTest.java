package com.example.birdsofafeather;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test
    public void checkUserWasAddedToDB() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg");
        db.userWithCoursesDao().insert(newUser);

        UserWithCourses user = db.userWithCoursesDao().getUser(0);
        assertEquals("User Name", user.getName());
        assertEquals("userEmail@ucsd.edu", user.getEmail());
        assertEquals(true, user.getCourses().isEmpty());
    }

    @Test
    public void checkCourseWasAddedToDB() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg");
        db.userWithCoursesDao().insert(newUser);
        Course newCourse = new Course(59, newUser.getId(), 2022, "WINTER", "CSE", 110);
        db.coursesDao().insert(newCourse);

        UserWithCourses user = db.userWithCoursesDao().getUser(0);
        Course course = user.getCourses().get(0);
        assertEquals(false, user.getCourses().isEmpty());
        assertEquals("CSE", course.getDepartment());
        assertEquals(110, course.getCourseNumber());
        assertEquals("WINTER", course.getQuarter());
        assertEquals(2022, course.getYear());
    }
}
