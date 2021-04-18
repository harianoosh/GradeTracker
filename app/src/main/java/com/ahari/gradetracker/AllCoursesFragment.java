package com.ahari.gradetracker;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/*
    InClass09
    AllCoursesFragment
    Anoosh Hari, Dayakar Ravuri - Group 29
 */

public class AllCoursesFragment extends Fragment {

    RecyclerView recyclerView;
    TextView gpa, creditHrs;
    ImageButton addCourse;

    LinearLayoutManager linearLayoutManager;
    CoursesViewAdapter adapter;

    IAllCoursesFragmentListener listener;

    ArrayList<Course> courses = new ArrayList<>();

    CourseDatabase database;

    public AllCoursesFragment() {

    }

    public static AllCoursesFragment newInstance() {
        AllCoursesFragment fragment = new AllCoursesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addCourse:
                listener.launchNewCourseFragment(null);
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_courses, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        gpa = view.findViewById(R.id.gpa);
        creditHrs = view.findViewById(R.id.totalCreditHours);
        addCourse = view.findViewById(R.id.addCourse);

        setHasOptionsMenu(true);

        getActivity().setTitle(getString(R.string.grades_title));

        Toolbar toolbar = view.findViewById(R.id.appbarAllCourses);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        database = CourseDatabase.getInstance(getContext());

        linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new CoursesViewAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getCourses();

        updateGrades();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateGrades() {
        int cHrs = courses.stream().mapToInt(course -> course.creditHrs).sum();
        creditHrs.setText("Hours: " + cHrs);
        int totalGradePoints = courses.stream().mapToInt(course -> {
            int gradePoints = 0;
            switch (course.grade) {
                case "A":
                    gradePoints = 4;
                    break;
                case "B":
                    gradePoints = 3;
                    break;
                case "C":
                    gradePoints = 2;
                    break;
                case "D":
                    gradePoints = 1;
                    break;
                case "F":
                    gradePoints = 0;
                    break;
            }
            return gradePoints * course.creditHrs;
        }).sum();
        String gpaVal;
        if (cHrs != 0) {
            gpaVal = String.format("%.1f", (totalGradePoints / new Float(cHrs)));
        } else {
            gpaVal = "4.0";
        }
        gpa.setText("GPA: " + gpaVal);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getCourses() {
        courses.clear();
        courses.addAll(database.courseDAO().getAll());
        adapter.notifyDataSetChanged();
        updateGrades();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IAllCoursesFragmentListener) {
            listener = (IAllCoursesFragmentListener) context;
        }
    }

    interface IAllCoursesFragmentListener {
        void launchNewCourseFragment(Course course);
    }

    class CoursesViewAdapter extends RecyclerView.Adapter<CourseViewHolder> {

        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
            CourseViewHolder holder = new CourseViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            Course course = courses.get(position);
            holder.itemGrade.setText(course.grade);
            holder.itemCourseCode.setText(course.courseCode);
            holder.itemCourseName.setText(course.courseName);
            holder.itemCreditHours.setText(course.creditHrs + " "+getString(R.string.credit_hours_label));
            holder.course = course;
        }

        @Override
        public int getItemCount() {
            return courses.size();
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView itemGrade, itemCourseCode, itemCourseName, itemCreditHours;
        ImageView bin;
        Course course;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            itemGrade = itemView.findViewById(R.id.itemGrade);
            itemCourseCode = itemView.findViewById(R.id.itemCourseCode);
            itemCourseName = itemView.findViewById(R.id.itemCourseName);
            itemCreditHours = itemView.findViewById(R.id.itemCreditHours);
            bin = itemView.findViewById(R.id.itemDeleteIcon);

            bin.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    database.courseDAO().delete(course);
                    getCourses();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.launchNewCourseFragment(course);
                }
            });
        }
    }
}