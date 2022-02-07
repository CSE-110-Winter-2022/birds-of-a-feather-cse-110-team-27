package com.example.birdsofafeather.db.course;

public abstract class ICourse {
    abstract int getId();
    abstract int getYear();
    abstract String getQuarter();
    abstract String getDepartment();
    abstract int getCourseNumber();

    abstract void setYear(int year);
    abstract void setQuarter(String quarter);
    abstract void setDepartment(String department);
    abstract void setCourseNumber(int courseNumber);
}
