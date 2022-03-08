package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.utils.CourseComparison;

import org.junit.Test;

public class CourseComparisonTest {

    @Test
    public void compareSameCourses() {
        Course cse110 = new Course(8, 2022, "WINTER", "CSE", 110, "Large (150-250)");
        Course cse110Again = new Course(22, 2022, "WINTER", "CSE", 110, "Large (150-250)");
        assertEquals(true, CourseComparison.compareCourses(cse110, cse110Again));
    }

    @Test
    public void compareDifferentCourses() {
        Course cse110 = new Course(8, 2022, "WINTER", "CSE", 101, "Medium (75-150)");
        Course cse101 = new Course(22, 2022, "WINTER", "CSE", 110, "Large (150-250)");
        assertEquals(false, CourseComparison.compareCourses(cse110, cse101));
    }
}
