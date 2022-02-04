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
    @Transaction
    @Query("SELECT * FROM users where user_id=:userId")
    public abstract UserWithCourses getUser(int userId);

    @Transaction
    @Query("SELECT * FROM users")
    public abstract List<UserWithCourses> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insert(User user);

    @Insert()
    public abstract void insertCourse(Course course);

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
