package com.example.birdsofafeather.db.user;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.birdsofafeather.db.CourseUserRef;
import com.example.birdsofafeather.db.course.Course;

import java.util.ArrayList;
import java.util.List;

//@Entity(tableName = "users_with_courses")
public class UserWithCourses implements IUser {


    @Embedded
    public User user;

    @Relation(parentColumn = "id",
            entityColumn = "user_id",
            entity = Course.class
    )
    public List<Course> courses;

//    public UserWithCourses(int id, String name, String email) {
//        this.user = new User();
//        this.user.id = id;
//        this.user.name = name;
//        this.user.email = email;
////        this.course_ids = new ArrayList<>();
//        this.courses = new ArrayList<>();
//    }

    @Override
    public int getId() {
        return this.user.id;
    }

    @Override
    public String getName() {
        return this.user.name;
    }

    @Override
    public String getEmail() {
        return this.user.email;
    }

    @Override
    public List<Course> getCourses() {
        return this.courses;
    }

    @Override
    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Override
    public void setName(String name) {
        this.user.name = name;
    }

    @Override
    public void setEmail(String email) {
        this.user.email = email;
    }

    @Override
    public String getProfilePictureUrl() {
        return this.user.profilePictureUrl;
    }

    public void setNumSameCourses(int numSameCourses) {
        this.user.setNumSameCourses(numSameCourses);
    }

    public void incrementNumSamCourses() {
        this.user.incrementNumSamCourses();
    }

    public int getNumSamCourses() {
        return this.user.getNumSamCourses();
    }

    public Boolean isFavorite() { return this.user.isFavorite(); }

    public void toggleFavorite() { this.user.toggleFavorite(); }
}
