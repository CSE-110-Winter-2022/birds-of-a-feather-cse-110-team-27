package com.example.birdsofafeather;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing%22%3ETesting documentation</a>
 */
public class MockUserTests {
    @Test
    public void correctNumTotalCourses() {
        MockUserWithCourses MockJohn = new MockUserWithCourses(0);
        UserWithCourses John = MockJohn.student;
        assertEquals(John.getCourses().size(), 4);
    }

    @Test
    public void correctMockUserData() {
        MockUserWithCourses MockJohn = new MockUserWithCourses(0);
        User John = MockJohn.student.user;
        assertEquals(John.getId(), 0);
        assertEquals(John.getName(), "John");
        assertEquals(John.getEmail(), "john@ucsd.edu");
        assertEquals(John.getProfilePictureUrl(), "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg");
    }
}