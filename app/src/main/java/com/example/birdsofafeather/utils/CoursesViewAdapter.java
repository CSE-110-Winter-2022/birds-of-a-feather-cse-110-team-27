package com.example.birdsofafeather.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.R;
import com.example.birdsofafeather.db.course.Course;

import java.util.List;
import java.util.function.Consumer;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {
    private final List<Course> courses;
    private final Consumer<Course> onCourseRemoved;

    public CoursesViewAdapter(List<Course> courses, Consumer<Course> onCourseRemoved){
        super();
        this.courses = courses;
        this.onCourseRemoved = onCourseRemoved;
    }
    @NonNull
    @Override
    public CoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.courses_row, parent, false);
        return new ViewHolder(view, this::removeCourse, onCourseRemoved);
    }
    @Override
    public void onBindViewHolder(@NonNull CoursesViewAdapter.ViewHolder holder, int position){
        holder.setNote(courses.get(position));
    }
    @Override
    public int getItemCount(){
        return this.courses.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseTextView;
        private Course course;
        ViewHolder (View itemView, Consumer<Integer> removeCourse, Consumer<Course> onCourseRemoved){
            super (itemView);
            this.courseTextView = itemView.findViewById(R.id.course_name);
            Button removeButton=itemView.findViewById(R.id.remove_button);
            removeButton.setOnClickListener((view) -> {
                removeCourse.accept(this.getAdapterPosition());
                onCourseRemoved.accept(course);
            });
        }
        public void setNote(Course course) {
            this.course = course;
            //combine 4 cols to 1 string
            String courseInfo = Integer.toString(course.year) + " " + course.quarter + " " + course.department + " " + Integer.toString(course.course_number);
            this.courseTextView.setText(courseInfo);
        }
    }

    public void addCourse(Course course){
        this.courses.add(course);
        this.notifyItemInserted(this.courses.size()-1);
    }
    public void removeCourse(int position){
        this.courses.remove(position);
        this.notifyItemRemoved(position);
    }
}
