package com.example.birdsofafeather;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.generator.Generator;
import com.example.birdsofafeather.generator.WaveCSVGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RunWith(AndroidJUnit4.class)
public class WaveCSVGeneratorTest {
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
        AppDatabase.useTestSingleton(context);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testWaveCSVGenerator() {
        User meUser = new User("Rick Astley", "rick@gmail.com", "");
        User targetUser = new User("Yeltsa Kcir", "yeltsa@gmail.com", "");
        Course meTestCourse1 = new Course(meUser.getId(), 2021, "FALL", "CSE", 210, "Small (40-75)");
        Course meTestCourse2 = new Course(meUser.getId(), 2022, "WINTER", "CSE", 110, "Large (150-250)");
        List<Course> myListOfCourses = new ArrayList<Course>();
        myListOfCourses.add(meTestCourse1);
        myListOfCourses.add(meTestCourse2);
        UserWithCourses meTest = new UserWithCourses();
        UserWithCourses targetTest = new UserWithCourses();
        meTest.user = meUser;
        meTest.courses = myListOfCourses;
        targetTest.user = targetUser;

        db.userWithCoursesDao().insert(meUser);
        db.coursesDao().insert(meTestCourse1);
        db.userWithCoursesDao().insert(targetUser);

        Generator generator = new WaveCSVGenerator();
//        Context context = ApplicationProvider.getApplicationContext();
//        String resultCSV = generator.generateCSV(context, meTest.getId(), targetTest.getId());
//        System.out.println(resultCSV);
    }
}
