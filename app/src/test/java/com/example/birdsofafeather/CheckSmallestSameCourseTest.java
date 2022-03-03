package com.example.birdsofafeather;

import static org.junit.Assert.assertEquals;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.CheckUserSmallestSameCourse;
import com.example.birdsofafeather.utils.CourseComparison;

import org.junit.Test;

public class CheckSmallestSameCourseTest {

    @Test
    public void sameSizeCourseComparison() {
        User user = new User(0,"Emil", "emil@ucsd.edu","mockimage.jpg");
        UserWithCourses emil = new UserWithCourses();
        emil.user = user;
        user.setSmallestSameCourseSize(150);

        CheckUserSmallestSameCourse.updateUserSmallestSameCourse(emil, "Large (150-250)");
        assertEquals(emil.getSmallestSameCourseSize(), 150);
    }

    @Test
    public void smallerSizeCourseComparison() {
        User user = new User(0,"Emil", "emil@ucsd.edu","mockimage.jpg");
        UserWithCourses emil = new UserWithCourses();
        emil.user = user;
        user.setSmallestSameCourseSize(40);

        CheckUserSmallestSameCourse.updateUserSmallestSameCourse(emil, "Large (150-250)");
        assertEquals(emil.getSmallestSameCourseSize(), 40);
    }

    @Test
    public void biggerSizeCourseComparison() {
        User user = new User(0,"Emil", "emil@ucsd.edu","mockimage.jpg");
        UserWithCourses emil = new UserWithCourses();
        emil.user = user;
        user.setSmallestSameCourseSize(400);

        CheckUserSmallestSameCourse.updateUserSmallestSameCourse(emil, "Large (150-250)");
        assertEquals(emil.getSmallestSameCourseSize(), 150);
    }
}
