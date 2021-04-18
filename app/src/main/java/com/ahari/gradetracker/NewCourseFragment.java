package com.ahari.gradetracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

/*
    InClass09
    NewCourseFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class NewCourseFragment extends Fragment {

    private static final String COURSE = "COURSE";

    private Course course;

    EditText courseNumber, courseName, creditHrs;
    RadioGroup radioGroup;
    Button submit, cancel;

    CourseDatabase database;

    public NewCourseFragment(Course course) {
        this.course = course;
    }

    public static NewCourseFragment newInstance(Course course) {
        NewCourseFragment fragment = new NewCourseFragment(course);
        Bundle args = new Bundle();
        args.putSerializable(COURSE, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (Course) getArguments().getSerializable(COURSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_course, container, false);

        courseNumber = view.findViewById(R.id.courseNumber);
        courseName = view.findViewById(R.id.courseName);
        creditHrs = view.findViewById(R.id.creditHrs);
        radioGroup = view.findViewById(R.id.radioGroup);
        submit = view.findViewById(R.id.submit);
        cancel = view.findViewById(R.id.cancel);

        if (course != null){
            getActivity().setTitle(getString(R.string.update_course_title));
        } else {
            getActivity().setTitle(getString(R.string.add_course_title));
        }

        Toolbar toolbar = view.findViewById(R.id.appbarNewCourse);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        database = CourseDatabase.getInstance(getContext());

        if (course != null) {
            courseNumber.setText(course.courseCode);
            courseName.setText(course.courseName);
            creditHrs.setText(course.creditHrs + "");
            int radioButtonId = -1;
            switch (course.grade.toLowerCase()) {
                case "a":
                    radioButtonId = R.id.gradeA;
                    break;
                case "b":
                    radioButtonId = R.id.gradeB;
                    break;
                case "c":
                    radioButtonId = R.id.gradeC;
                    break;
                case "d":
                    radioButtonId = R.id.gradeD;
                    break;
                case "f":
                    radioButtonId = R.id.gradeF;
                    break;
            }
            radioGroup.check(radioButtonId);
        }

        if (course != null) {
            submit.setText("Update");
        } else {
            submit.setText("Submit");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cName = courseName.getText().toString();
                String cNumber = courseNumber.getText().toString();
                int cHrs = 0;
                try {
                    if (cName.isEmpty()) {
                        throw new Exception("Enter a valid Course Name");
                    }
                    if (cNumber.isEmpty()) {
                        throw new Exception("Enter a valid Course Number");
                    }
                    try {
                        cHrs = Integer.parseInt(creditHrs.getText().toString());
                        if (cHrs == 0) {
                            throw new Exception("Enter valid number of Credit Hours");
                        }
                    } catch (Exception e) {
                        throw new Exception("Enter valid number of Credit Hours");
                    }
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        throw new Exception("Select any grade");
                    }
                    if (course != null) {
                        course.courseCode = cNumber;
                        course.creditHrs = cHrs;
                        course.grade = getGradeFromRadioGroup();
                        course.courseName = cName;
                        database.courseDAO().update(course);
                        Toast.makeText(getContext(), "Course updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        course = new Course(cNumber, cName, cHrs, getGradeFromRadioGroup());
                        Toast.makeText(getContext(), "Course added successfully", Toast.LENGTH_SHORT).show();
                        database.courseDAO().insertAll(course);
                    }
                    listener.popFromBackStack();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.popFromBackStack();
            }
        });

        return view;
    }

    private String getGradeFromRadioGroup() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.gradeA:
                return "A";
            case R.id.gradeB:
                return "B";
            case R.id.gradeC:
                return "C";
            case R.id.gradeD:
                return "D";
            case R.id.gradeF:
                return "F";
        }

        return null;
    }

    INewFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof INewFragmentListener) {
            listener = (INewFragmentListener) context;
        }
    }

    interface INewFragmentListener {
        void popFromBackStack();
    }
}