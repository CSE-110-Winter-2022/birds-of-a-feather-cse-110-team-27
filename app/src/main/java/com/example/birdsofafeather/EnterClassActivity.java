package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.IUser;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.utils.Constants;
import com.example.birdsofafeather.utils.CoursesViewAdapter;
import com.example.birdsofafeather.utils.Utilities;

import java.sql.Array;
import java.util.List;


public class EnterClassActivity extends AppCompatActivity {

    private AppDatabase db;
    private IUser user;
    private RecyclerView courseRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter courseViewAdapter; //working on it

    public void onAddCourseClicked(View view) {
       EditText year_input = this.findViewById(R.id.year_input);
       String year = year_input.getText().toString();

       Spinner quarter_dropdown = this.findViewById(R.id.quarter_dropdown);
       String quarter = quarter_dropdown.getSelectedItem().toString();

       Spinner department_dropdown = this.findViewById(R.id.department_dropdown);
       String department = department_dropdown.getSelectedItem().toString();

       EditText course_number_input = this.findViewById(R.id.course_number_input);
       String course_number = course_number_input.getText().toString();

       if (year.isEmpty()) {
            Utilities.showAlert(this, "Missing year field");
            return;
        }
        if (quarter.isEmpty()) {
            Utilities.showAlert(this, "Missing quarter field");
            return;
        }
        if (department.isEmpty()) {
            Utilities.showAlert(this, "Missing department field");
            return;
        }
        if (course_number.isEmpty()) {
            Utilities.showAlert(this, "Missing course number field");
            return;
        }

       Integer year_int;
       try {
           year_int = Integer.parseInt(year);
       } catch (final NumberFormatException e) {
           Utilities.showAlert(this, String.format("Invalid Year %s", year));
           return;
       }

      Integer course_number_int;
       try {
           course_number_int = Integer.parseInt(course_number);
       } catch (final NumberFormatException e) {
           Utilities.showAlert(this, String.format("Invalid Course Number %s", course_number));
           return;
       }

        System.out.println("User" + user.getId());
       Course newCourse = new Course(db.coursesDao().maxId() + 1, user.getId(), year_int.intValue(), quarter, department, course_number_int.intValue());
        db.coursesDao().insert(newCourse);
        System.out.println(db.coursesDao().maxId());
        courseViewAdapter.addCourse(newCourse);
        Utilities.showAlert(this, String.format("Added %s %s: %s %s", department, course_number, quarter, year));

    }

    private class DropdownAdapter extends ArrayAdapter {

        public DropdownAdapter(@NonNull Context context, String[] items) {
            super(context, android.R.layout.simple_spinner_dropdown_item, items);
        }

        @Override
        public boolean isEnabled(int position) {
            if (position == 0) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return false;
            } else {
                return true;
            }
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if (position == 0) {
                // Set the hint text color gray
                tv.setTextColor(Color.GRAY);
            } else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_class);

        Spinner quarterDropdown = findViewById(R.id.quarter_dropdown);
        DropdownAdapter quarterSelectionAdapter = new DropdownAdapter(this, Constants.quarters);
        quarterDropdown.setAdapter(quarterSelectionAdapter);

        Spinner departmentDropdown = findViewById(R.id.department_dropdown);
        DropdownAdapter departmentSelectionAdapter = new DropdownAdapter(this, Constants.departments);
        departmentDropdown.setAdapter(departmentSelectionAdapter);

        Intent intent = getIntent();
        int userId = intent.getIntExtra( "user_id", 0); //for now default is 0. Maybe last activity pass some value???

        db = AppDatabase.singleton(this);
        user = db.userWithCoursesDao().getUser(userId);


        //List<Course> courses = db.coursesDao().getForId(userId);   //couseId or userId
        List<Course> courses = user.getCourses();


        // Set the title with the person.
        setTitle (user.getName ());
        // Set up the recycler view to show our database contents.
        courseRecyclerView=findViewById(R.id.courses_view);
        coursesLayoutManager = new LinearLayoutManager( this) ;
        courseRecyclerView.setLayoutManager(coursesLayoutManager);
        courseViewAdapter = new CoursesViewAdapter (courses, (course)->{
            db.coursesDao().delete(course);
        });
        courseRecyclerView.setAdapter(courseViewAdapter);


    }

    public void doneClicked(View view) {
        Intent intent = new Intent(this, StartFindNearby.class);
        intent.putExtra("user_id", user.getId());
        startActivity(intent);
    }
}