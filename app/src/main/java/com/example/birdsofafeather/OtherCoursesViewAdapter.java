package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.birdsofafeather.db.course.Course;

import java.util.List;
import java.util.function.Consumer;

public class OtherCoursesViewAdapter extends RecyclerView.Adapter<OtherCoursesViewAdapter.ViewHolder> {

    private final String[] courses;

    public OtherCoursesViewAdapter(String[] courseArray) {
        super();
        this.courses = courseArray;
    }



    @NonNull
    @Override
    public OtherCoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_courses_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OtherCoursesViewAdapter.ViewHolder holder, int position) {
        holder.setNote(courses[position]);
    }

    @Override
    public int getItemCount() {
        return this.courses.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameTextView;
        private String course;

        ViewHolder(View itemView) {
            super(itemView);
            this.courseNameTextView = itemView.findViewById(R.id.other_courses_row_text);
        }

        public void setNote(String course) {
            this.course = course;
            this.courseNameTextView.setText(course);
        }
    }

}