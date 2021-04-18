package com.ahari.gradetracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/*
    InClass09
    CourseDatabase
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

@Database(entities = {Course.class}, version = 1)
public abstract class CourseDatabase extends RoomDatabase {

    static CourseDatabase database;

    public abstract CourseDAO courseDAO();

    public static CourseDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, CourseDatabase.class, "courses.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}
