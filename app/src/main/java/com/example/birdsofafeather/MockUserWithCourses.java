package com.example.birdsofafeather;

import androidx.annotation.NonNull;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.ArrayList;
import java.util.List;

public class MockUserWithCourses{
    public UserWithCourses student;
    Course testCourse_1 = new Course(0,0,2021, "FALL", "ECE", 100);
    Course testCourse_2 = new Course(0,0,2021, "FALL", "ECE", 65);
    Course testCourse_3 = new Course(0,0,2021, "FALL", "MATH", 109);
    Course testCourse_4 = new Course(0,0,2022, "WINTER", "CSE", 110);
    Course[] courses = {
                new Course(0,0,2021, "FALL", "ECE", 101),
                new Course(0,0,2020, "FALL", "BILD", 1),
                new Course(0,0,2020, "FALL", "MATH", 183),
                new Course(0,0,2020, "FALL", "MATH", 18),
                new Course(0,0,2020, "FALL", "HILD", 7),
                new Course(0,0,2020, "FALL", "HUM", 2),
                new Course(0,0,2020, "FALL", "CHEM", 40),
                new Course(0,0,2020, "FALL", "DOC", 1),
            };
    public static String[] studentNames = {"John", "Amy", "Zoey", "Matt"};
    public static String[] studentEmails = {"john@ucsd.edu", "amy@ucsd.edu", "zoey@ucsd.edu", "matt@ucsd.edu"};
    public static String[] studentProfilePictureUrls = {
            "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/220px-President_Barack_Obama.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Donald_Trump_official_portrait.jpg/220px-Donald_Trump_official_portrait.jpg",
            "https://resizing.flixster.com/9Tn-N5F9wPj2VvlJWDNT1iw7kNo=/206x305/v2/https://flxt.tmsimg.com/assets/p186137_b_v8_aa.jpg",
            "https://static.wikia.nocookie.net/w__/images/5/52/HEYimHeroic_3DS_FACE-024_Matt-Wii.JPG/revision/latest?cb=20200705094326&path-prefix=wiisports"

    };


    public MockUserWithCourses(int studentNum){
        User user = new User(studentNum,studentNames[studentNum], studentEmails[studentNum], studentProfilePictureUrls[studentNum]);

        List<Course> listOfCourses = new ArrayList<Course>();
        if(studentNum != 3){
            for(int i = 0; i < 4; i++){
                Course course = courses[(int)(Math.random()*8)];
                if(!listOfCourses.contains(course)) {
                    listOfCourses.add(course);
                }
                else{
                    i--;
                }
            }
        }
        else {
            listOfCourses.add(testCourse_4);
        }
        if(studentNum == 2) {
            listOfCourses.add(testCourse_1);
            listOfCourses.add(testCourse_2);
            listOfCourses.add(testCourse_3);
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
