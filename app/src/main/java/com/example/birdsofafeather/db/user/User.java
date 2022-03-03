package com.example.birdsofafeather.db.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.birdsofafeather.db.course.Course;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "sessionId")
    long sessionId;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "numSameCourses")
    public int numSameCourses;

    @ColumnInfo(name = "lastSameCourseTime")
    int lastSameCourseTime;

    @ColumnInfo(name = "smallestSameCourseSize")
    int smallestSameCourseSize;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "profile_picture_url")
    String profilePictureUrl;

    //TODO: randomly generate id
    public User(int id, String name, String email, String profilePictureUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.numSameCourses = 0;
        this.lastSameCourseTime = 0;
        this.smallestSameCourseSize = 999;
    }
    public int getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getProfilePictureUrl() {
        return this.profilePictureUrl;
    }

    public void setNumSameCourses(int numSameCourses) {
        this.numSameCourses = numSameCourses;
    }

    public void incrementNumSamCourses() {
        this.numSameCourses += 1;
    }

    public int getNumSamCourses() {
        return this.numSameCourses;
    }

    public int getLastSameCourseTime() {
        return lastSameCourseTime;
    }

    public void setLastSameCourseTime(int lastSameCourseTime) {
        this.lastSameCourseTime = lastSameCourseTime;
    }

    public int getSmallestSameCourseSize() {
        return smallestSameCourseSize;
    }

    public void setSmallestSameCourseSize(int smallestSameCourseSize) {
        this.smallestSameCourseSize = smallestSameCourseSize;
    }

    public String getEmail() {
        return this.email;
    }

    public long getSessionId() { return this.sessionId; }

    public void setSessionId(long sessionId) { this.sessionId = sessionId; }
}
