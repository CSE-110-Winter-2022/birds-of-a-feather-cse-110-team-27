package com.example.birdsofafeather.db.course;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "courses")
public class Course  extends ICourse {

    @PrimaryKey
    @ColumnInfo(name = "course_id")
    public int id;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "quarter")
    public String quarter;

    @ColumnInfo(name = "department")
    public String department;

    @ColumnInfo(name = "course_number")
    public int course_number;

    //TODO: randomly generate ID
    public Course(int id, int year, String quarter, String department, int course_number){
        this.id = id;
        this.year = year;
        this.quarter = quarter;
        this.department = department;
        this.course_number = course_number;
    }

    @Override
    public int getId() {
        return this.id;
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
}