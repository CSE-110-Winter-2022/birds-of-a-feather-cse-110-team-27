package com.example.birdsofafeather.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.R;
import com.example.birdsofafeather.db.course.Course;

import java.util.List;

public class ProfileCoursesViewAdapter extends RecyclerView.Adapter<ProfileCoursesViewAdapter.ViewHolder> {
    private final List<String> courses;

    public ProfileCoursesViewAdapter(List<String> course_strings){
        super();
        this.courses = course_strings;
    }

    @NonNull
    @Override
    public ProfileCoursesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.profile_courses_row, parent, false);
        return new ProfileCoursesViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setNote(courses.get(position));
    }

    @Override
    public int getItemCount(){
        return this.courses.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView courseTextView;
        ViewHolder (View itemView){
            super (itemView);
            this.courseTextView = itemView.findViewById(R.id.profile_details_course_name);
        }
        public void setNote(String course_string) {
            this.courseTextView.setText(course_string);
        }
    }

    public void addCourse(String course_string){
        this.courses.add(course_string);
        this.notifyItemInserted(this.courses.size()-1);
    }
}
