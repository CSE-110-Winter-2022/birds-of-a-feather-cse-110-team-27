package com.example.birdsofafeather;

import static com.example.birdsofafeather.PersonsViewAdapter.courseArrayMaker;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.course.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void courseArrayMakerTest() {
        List<Course> listOfCourses = new ArrayList<Course>();

        // Initializes 3 different courses
        Course course0 = new Course(0, 0, 2021, "Fall", "ECE", 101);
        Course course1 = new Course(0, 0, 2022, "Winter", "CSE", 110);
        Course course2 = new Course(0, 0, 2022, "Winter", "CSE", 101);

        listOfCourses.add(course0);
        listOfCourses.add(course1);
        listOfCourses.add(course2);

        ArrayList<String> user = courseArrayMaker(listOfCourses, "Matt");

        String courseIndex0 = "ECE 101 2021 Fall";
        String courseIndex1 = "CSE 110 2022 Winter";
        String courseIndex2 = "CSE 101 2022 Winter";

        assertEquals(courseIndex0, user.get(0));
        assertEquals(courseIndex1, user.get(1));
        assertEquals(courseIndex2, user.get(2));

    }
}

