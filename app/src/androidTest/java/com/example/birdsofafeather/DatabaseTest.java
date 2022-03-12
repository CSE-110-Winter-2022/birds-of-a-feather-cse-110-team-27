package com.example.birdsofafeather;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.generator.Generator;
import com.example.birdsofafeather.generator.WaveCSVGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    @Test
    public void checkURLInsertedIntoDB(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User newUser = new User("User Name", "userEmail@ucsd.edu", "https://i.insider.com/602ee9ced3ad27001837f2ac?width=1000&format=jpeg&auto=webp");
        long userId = db.userWithCoursesDao().insert(newUser);

        UserWithCourses userWithCourses = db.userWithCoursesDao().getUser(userId);
        String urlFromDB = userWithCourses.getProfilePictureUrl();

        //assertEquals(user.getEmail(), "userEmail@ucsd.edu");
        assertEquals("https://i.insider.com/602ee9ced3ad27001837f2ac?width=1000&format=jpeg&auto=webp", urlFromDB);
    }



    @Test
    public void testDelete(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User student = new User("X", "******@ucsd.edu", "");
        long studentid = db.userWithCoursesDao().insert(student);

        Course classCancelled = new Course(student.getId(), 2021, "WINTER", "HUM",1, "TINY");
        db.coursesDao().insert(classCancelled);
        db.coursesDao().delete(classCancelled);
        UserWithCourses studentData = db.userWithCoursesDao().getUser(studentid);
        assertEquals(0,studentData.getCourses().size());
    }
}
