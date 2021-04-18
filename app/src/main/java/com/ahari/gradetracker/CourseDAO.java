package com.ahari.gradetracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
    InClass09
    CourseDAO
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Query("SELECT * FROM courses WHERE uuid IN (:uuids)")
    List<Course> getCourseById(long[] uuids);

    @Query("SELECT * FROM courses WHERE course_name LIKE :course_name AND " +
            "course_code LIKE :course_code LIMIT 1")
    Course findByName(String course_name, String course_code);

    @Update
    void update(Course course);

    @Insert
    void insertAll(Course... courses);

    @Delete
    void delete(Course course);
}
