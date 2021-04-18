package com.ahari.gradetracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/*
    InClass09
    Course
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

@Entity(tableName = "courses")
public class Course implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long uuid;
    @ColumnInfo(name = "course_code")
    public String courseCode;
    @ColumnInfo(name = "course_name")
    public String courseName;
    @ColumnInfo(name = "credit_hours")
    public int creditHrs;
    @ColumnInfo(name = "grade")
    public String grade;

    public Course(String courseCode, String courseName, int creditHrs, String grade) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHrs = creditHrs;
        this.grade = grade;
    }

    public Course(long uuid, String courseCode, String courseName, int creditHrs, String grade) {
        this.uuid = uuid;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.creditHrs = creditHrs;
        this.grade = grade;
    }

    public Course() {
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", creditHrs='" + creditHrs + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
