package com.example.birdsofafeather.db.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.birdsofafeather.db.course.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

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
    public String profilePictureUrl;

    @ColumnInfo(name = "favorite")
    public Boolean favorite;

    //TODO: randomly generate id
    public User(String name, String email, String profilePictureUrl) {
//        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.numSameCourses = 0;
        this.favorite = false;
        this.lastSameCourseTime = 0;
        this.smallestSameCourseSize = 999;
    }

    public long getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Boolean isFavorite() { return this.favorite; }

    public void toggleFavorite() { this.favorite ^= true; }

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
