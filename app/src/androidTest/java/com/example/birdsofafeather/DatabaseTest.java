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
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "");
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
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "");
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

    @Test
    public void checkCourseRemovedFromDB(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "");
        db.userWithCoursesDao().insert(newUser);
        Course newCourse = new Course(59, newUser.getId(), 2022, "WINTER", "CSE", 110);
        db.coursesDao().insert(newCourse);
        db.coursesDao().delete(newCourse);

        UserWithCourses user = db.userWithCoursesDao().getUser(0);

        assertEquals(true, user.getCourses().isEmpty());
    }

    @Test
    public void checkURLInsertedIntoDB(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User newUser = new User(0, "User Name", "userEmail@ucsd.edu", "https://i.insider.com/602ee9ced3ad27001837f2ac?width=1000&format=jpeg&auto=webp");
        db.userWithCoursesDao().insert(newUser);

        UserWithCourses userWithCourses = db.userWithCoursesDao().getUser(0);
        String urlFromDB = userWithCourses.getProfilePictureUrl();

        //assertEquals(user.getEmail(), "userEmail@ucsd.edu");
        assertEquals("https://i.insider.com/602ee9ced3ad27001837f2ac?width=1000&format=jpeg&auto=webp", urlFromDB);
    }

    @Test
    public void checkUserWithCourse(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User Rick = new User(6121,"Rick", "RickRick@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        db.userWithCoursesDao().insert(Rick);
        User Morty = new User(56467,"Morty", "Mooorty@gmail.com", "https://images.app.goo.gl/g8byPRgsPjgD2LGx5");
        db.userWithCoursesDao().insert(Morty);
        Course RickCourse1 = new Course(666, Rick.getId(), 1985, "FALL", "MATH",130);
        db.coursesDao().insert(RickCourse1);
        Course RickCourse2 = new Course(667, Rick.getId(), 1985, "FALL", "CHEM",7);
        db.coursesDao().insert(RickCourse2);
        Course MortyCourse1 = new Course(9527, Morty.getId(), 2018, "WINTER", "MMW",3);
        db.coursesDao().insert(MortyCourse1);
        List<Course> courses= db.coursesDao().getAll();
        System.out.println(courses.toString());

        UserWithCourses RickData = db.userWithCoursesDao().getUser(Rick.getId());
        UserWithCourses MortyData = db.userWithCoursesDao().getUser(Morty.getId());

        assertEquals(1,MortyData.getCourses().size());
        assertEquals(2,RickData.getCourses().size());
    }

    @Test
    public void testDelete(){
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birdsofafeather", appContext.getPackageName());

        AppDatabase db = AppDatabase.singleton(appContext);
        User student = new User(65414584,"X", "******@ucsd.edu", "");
        db.userWithCoursesDao().insert(student);

        Course classCancelled = new Course(93217, student.getId(), 2021, "WINTER", "HUM",1);
        db.coursesDao().insert(classCancelled);
        db.coursesDao().delete(classCancelled);
        UserWithCourses studentData = db.userWithCoursesDao().getUser(student.getId());
        assertEquals(0,studentData.getCourses().size());
    }
}
