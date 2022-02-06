package com.example.birdsofafeather.db.course;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CoursesDao {
//    @Transaction
//    @Query("SELECT * FROM courses where course_id=:userId")
//    List<Course> getForUser(int userId);
    @Query("SELECT MAX(id) FROM users")
    int maxId();

    @Query("SELECT * FROM courses WHERE course_id=:courseId")
    Course getForId(int courseId);

    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Delete
    void delete(Course course);

    //get
}
