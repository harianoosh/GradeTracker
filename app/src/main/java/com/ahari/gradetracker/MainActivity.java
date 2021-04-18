package com.ahari.gradetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
    InClass09
    MainActivity
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class MainActivity extends AppCompatActivity implements AllCoursesFragment.IAllCoursesFragmentListener, NewCourseFragment.INewFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new AllCoursesFragment())
                .commit();


    }

    @Override
    public void launchNewCourseFragment(Course course) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, new NewCourseFragment(course))
                .commit();
    }

    @Override
    public void popFromBackStack() {
        getSupportFragmentManager().popBackStack();
    }
}