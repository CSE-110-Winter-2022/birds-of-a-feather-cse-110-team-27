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

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "email")
    String email;

    @ColumnInfo(name = "profile_picture_url")
    String profilePictureUrl;

    //TODO: randomly generate id
    public User(int id, String name, String email, String profilePictureUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
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

    public String getEmail() {
        return this.email;
    }
}
