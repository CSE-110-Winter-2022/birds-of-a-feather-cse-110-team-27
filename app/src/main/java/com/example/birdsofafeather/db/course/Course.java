package com.example.birdsofafeather.db.course;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.birdsofafeather.utils.Utilities;


@Entity(tableName = "courses")
public class Course  extends ICourse {

    @PrimaryKey
    @ColumnInfo(name = "course_id")
    public int courseId;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "quarter")
    public String quarter;

    @ColumnInfo(name = "department")
    public String department;

    @ColumnInfo(name = "course_number")
    public int course_number;

    @ColumnInfo(name = "size")
    public String size;

    public Course(int courseId, int userId, int year, String quarter, String department, int course_number, String size){
        this.courseId = courseId;
        this.userId = userId;
        this.year = year;
        this.quarter = quarter;
        this.department = department;
        this.course_number = course_number;
        this.size = size;
    }

    @Override
    public int getId() {
        return this.courseId;
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public String getQuarter() {
        return this.quarter;
    }

    @Override
    public String getDepartment() {
        return this.department;
    }

    @Override
    public int getCourseNumber() {
        return this.course_number;
    }

    public String getSize() { return size; }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public void setCourseNumber(int courseNumber) {
        this.course_number = courseNumber;
    }

    public void setSize(String size) { this.size = size; }
}
