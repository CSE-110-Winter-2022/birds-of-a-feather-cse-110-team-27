package com.example.birdsofafeather.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.Room;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.course.CoursesDao;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsersDao;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCoursesDao;

@Database(entities = {User.class, Course.class, Session.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase singletonInstance;

    public static AppDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, AppDatabase.class, "bof.db")
//                    .createFromAsset("starter-persons.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return singletonInstance;
    }

    public abstract CoursesDao coursesDao();
    public abstract UserWithCoursesDao userWithCoursesDao();
    public abstract SessionWithUsersDao sessionWithUsersDao();
}
