package com.example.birdsofafeather.db.course;

import static androidx.room.OnConflictStrategy.ABORT;
import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

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
    @Query("SELECT MAX(course_id) FROM courses")
    int maxId();

    @Query("SELECT * FROM courses WHERE course_id=:courseId")
    Course getForId(int courseId);

    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Insert(onConflict = REPLACE)
    void insert(Course course);

    @Delete
    void delete(Course course);

    //get
}
