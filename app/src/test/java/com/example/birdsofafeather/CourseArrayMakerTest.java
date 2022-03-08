package com.example.birdsofafeather;

import static com.example.birdsofafeather.PersonsViewAdapter.courseArrayMaker;

import static org.junit.Assert.assertEquals;

import com.example.birdsofafeather.db.course.Course;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CourseArrayMakerTest {
    @Test
    public void courseArrayMakerTest() {
        List<Course> listOfCourses = new ArrayList<Course>();

        // Initializes 3 different courses
        Course course0 = new Course(0, 2021, "Fall", "ECE", 101,"Medium (75-150)");
        Course course1 = new Course(0, 2022, "Winter", "CSE", 110, "Large (150-250)");
        Course course2 = new Course(0, 2022, "Winter", "CSE", 101, "Medium (75-150)");

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
