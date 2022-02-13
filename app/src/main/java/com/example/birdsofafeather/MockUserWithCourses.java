package com.example.birdsofafeather;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.ArrayList;
import java.util.List;

public class MockUserWithCourses{
    private UserWithCourses student;
    Course[] courses = {
                new Course(0,0,2020, "Fall", "ECE", 100),
                new Course(0,0,2020, "Fall", "BILD", 1),
                new Course(0,0,2020, "Fall", "MATH", 183),
                new Course(0,0,2020, "Fall", "MATH", 18),
                new Course(0,0,2020, "Fall", "HILD", 7),
                new Course(0,0,2020, "Fall", "HUM", 2),
                new Course(0,0,2020, "Fall", "CHEM", 40),
                new Course(0,0,2020, "Fall", "DOC", 1),
            };
    public static String[] studentNames = {"John", "Amy", "Zoey"};
    public static String[] studentEmails = {"john@ucsd.edu", "amy@ucsd.edu", "zoey@ucsd.edu"};


    public MockUserWithCourses(int studentNum){
        User user = new User(studentNum,studentNames[studentNum], studentEmails[studentNum]);
        List<Course> listOfCourses = new ArrayList<Course>();
        for(int i = 0; i < 4; i++){
            Course course = courses[(int)(Math.random()*8)];
            if(!listOfCourses.contains(course)) {
                listOfCourses.add(course);
            }
            else{
                i--;
            }
        }

        student = new UserWithCourses();
        student.user = user;
        student.courses = listOfCourses;
    }

    @Override
    public String toString() {
        String toString = student.getName() + ":|";
        List<Course> studentsCourses = student.courses;
        for(Course course: studentsCourses){
            toString += course.getQuarter() + " " + course.getYear() + " " + course.getDepartment() + course.getCourseNumber() + "|";
        }
        return toString;
    }

    public UserWithCourses getUserWithCourses() {
        return student;
    }
}
