package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import org.junit.Test;

import java.util.List;

public class CoursesTest {
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
}
