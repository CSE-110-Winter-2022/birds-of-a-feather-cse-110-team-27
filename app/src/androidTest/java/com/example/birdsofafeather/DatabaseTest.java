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
import com.example.birdsofafeather.generator.Generator;
import com.example.birdsofafeather.generator.WaveCSVGenerator;

import java.util.List;

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
        User newUser = new User("User Name", "userEmail@ucsd.edu", "");
        long userId = db.userWithCoursesDao().insert(newUser);

        UserWithCourses user = db.userWithCoursesDao().getUser(userId);
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
        User newUser = new User( "User Name", "userEmail@ucsd.edu", "");
        long userId = db.userWithCoursesDao().insert(newUser);
        Course newCourse = new Course(userId, 2022, "WINTER", "CSE", 110, "Tiny");
        db.coursesDao().insert(newCourse);

        UserWithCourses user = db.userWithCoursesDao().getUser(userId);
        Course course = user.getCourses().get(0);
        assertEquals(false, user.getCourses().isEmpty());
        assertEquals("CSE", course.getDepartment());
        assertEquals(110, course.getCourseNumber());
        assertEquals("WINTER", course.getQuarter());
        assertEquals(2022, course.getYear());
    }

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
    public void checkUserWithCourse(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User Rick = new User("Rick", "RickRick@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        long rickid = db.userWithCoursesDao().insert(Rick);
        User Morty = new User("Morty", "Mooorty@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        long mortyid = db.userWithCoursesDao().insert(Morty);
        Course RickCourse1 = new Course(rickid, 1985, "FALL", "MATH",130, "TINY");
        db.coursesDao().insert(RickCourse1);
        Course RickCourse2 = new Course(rickid, 1985, "FALL", "CHEM",7, "TINY");
        db.coursesDao().insert(RickCourse2);
        Course MortyCourse1 = new Course(mortyid, 2018, "WINTER", "MMW",3, "TINY");
        db.coursesDao().insert(MortyCourse1);
        List<Course> courses= db.coursesDao().getAll();
        System.out.println(courses.toString());

        UserWithCourses RickData = db.userWithCoursesDao().getUser(rickid);
        UserWithCourses MortyData = db.userWithCoursesDao().getUser(mortyid);

        assertEquals(1,MortyData.getCourses().size());
        assertEquals(2,RickData.getCourses().size());
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

    @Test
    public void testWaveCSV() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User Rick = new User("Rick", "RickRick@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        long rickid = db.userWithCoursesDao().insert(Rick);
        User Morty = new User("Morty", "Mooorty@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        long mortyid = db.userWithCoursesDao().insert(Morty);
        Course RickCourse1 = new Course(rickid, 1985, "FALL", "MATH",130, "TINY");
        db.coursesDao().insert(RickCourse1);
        Course RickCourse2 = new Course(rickid, 1985, "FALL", "CHEM",7, "TINY");
        db.coursesDao().insert(RickCourse2);
        Course MortyCourse1 = new Course(mortyid, 2018, "WINTER", "MMW",3, "TINY");
        db.coursesDao().insert(MortyCourse1);
        List<Course> courses= db.coursesDao().getAll();
        System.out.println(courses.toString());

        UserWithCourses RickData = db.userWithCoursesDao().getUser(rickid);
        UserWithCourses MortyData = db.userWithCoursesDao().getUser(mortyid);
        Generator generator = new WaveCSVGenerator();
        String resultCSV = generator.generateCSV(appContext, RickData.getId(), MortyData.getId());
        System.out.println(resultCSV);
        assertEquals(1, 0);
    }
}
