package com.example.birdsofafeather.db.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.birdsofafeather.db.course.Course;

import java.util.List;

@Dao
public abstract class UserWithCoursesDao {

    @Query("SELECT MAX(id) FROM users")
    public abstract int maxId();

    @Transaction
    @Query("SELECT * FROM users where id=:userId")
    public abstract UserWithCourses getUser(int userId);

    @Transaction
    @Query("SELECT * FROM courses where user_id=:userId")
    public abstract List<Course> getCoursesForUserId(int userId);

//    @Transaction
//    @Query("SELECT * FROM users where email=:email")
//    public abstract UserWithCourses getUserForEmail(String email);

    @Transaction
    @Query("SELECT * FROM users")
    public abstract List<UserWithCourses> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertCourse(Course course);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(User user);

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    public abstract void insertCourse(Course course);

//    @Update
//    void update(UserWithCourses user);
//
//    @Delete
//    void delete(UserWithCourses user);

    public void insertUserWithCourse(User user){
//        List<Course> courses = use
        this.insert(user);
    }
}
