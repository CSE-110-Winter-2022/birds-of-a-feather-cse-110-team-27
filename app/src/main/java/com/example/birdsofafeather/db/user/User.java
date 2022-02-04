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
    @ColumnInfo(name = "user_id")
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "email")
    String email;

    //TODO: randomly generate id
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

}
