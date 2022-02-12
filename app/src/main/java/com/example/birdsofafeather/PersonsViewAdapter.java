package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.util.List;


public class PersonsViewAdapter extends RecyclerView.Adapter<PersonsViewAdapter.ViewHolder> {
    private final List<? extends UserWithCourses> persons;

    public PersonsViewAdapter(List<? extends UserWithCourses> persons) {
        super();
        this.persons = persons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.person_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setPerson(persons.get(position));
    }

    @Override
    public int getItemCount() {
        return this.persons.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView personNameView;
        private UserWithCourses person;


        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.person_row_name);
            itemView.setOnClickListener(this);
        }

        public void setPerson(UserWithCourses person) {
            this.person = person;
            this.personNameView.setText(person.getName());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, PersonDetailActivity.class);
            List<Course> person_course = this.person.getCourses();
            String name = this.person.getName();
            String[] courseArray = courseArrayMaker(person_course, name);
            intent.putExtra("courses_array", courseArray);
            context.startActivity(intent);
        }
    }

    public static String[] courseArrayMaker(List<Course> courses, String name) {
        String[] courseArray = new String[courses.size()+1];
        courseArray[0] = name;
        for (int i = 0; i < courses.size(); i++) {
            String courseDepartment = courses.get(i).department;
            String courseNumber = Integer.toString(courses.get(i).course_number);
            String courseYear =  Integer.toString(courses.get(i).year);
            String courseQuarter = courses.get(i).quarter;
            String courseInfo = courseDepartment + " " + courseNumber + " " + courseYear + " " + courseQuarter;
            courseArray[i+1] = courseInfo;
        }
        return courseArray;
    }
}