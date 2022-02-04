package com.example.birdsofafeather.db;

import androidx.room.Entity;

@Entity(primaryKeys = {"user_id", "course_id"})
public class CourseUserRef {
    public int user_id;
    public int course_id;
}
