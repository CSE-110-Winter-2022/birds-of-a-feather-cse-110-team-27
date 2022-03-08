package com.example.birdsofafeather;

import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.CheckUserLastSameCourse;

import org.junit.Assert;
import org.junit.Test;

public class CheckUserLastSameCourseTest {

    @Test
    public void checkUserLastSameCourse() {
        User user1 = new User("Byron Chan", "bschan@ucsd.edu", "https://ucsd.edu");
        User user2 = new User("Emil Sharkov", "esharkov@ucsd.edu", "https://ucsd.edu");
        UserWithCourses userWithCourses1 = new UserWithCourses();
        userWithCourses1.user = user1;
        UserWithCourses userWithCourses2 = new UserWithCourses();
        userWithCourses2.user = user2;
        CheckUserLastSameCourse.updateUserLastSameCourseTime(userWithCourses1, 2021, "FALL");
        CheckUserLastSameCourse.updateUserLastSameCourseTime(userWithCourses2, 2021, "SPRING");

        Assert.assertEquals(true, userWithCourses1.getLastSameCourseTime() > userWithCourses2.getLastSameCourseTime());

    }

}
