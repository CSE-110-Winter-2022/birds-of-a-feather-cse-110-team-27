package com.example.birdsofafeather;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        public UserWithCourses person;
        private TextView numSameView;
        private TextView favorite;


        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.person_row_name);
            this.numSameView = itemView.findViewById(R.id.num_same_courses_view);
            this.favorite = itemView.findViewById(R.id.favorite);
            itemView.setOnClickListener(this);
        }

        public void setPerson(UserWithCourses person) {
            if (this.person == null) this.person = person;
            else this.person.user = person.user;

            if(person.getNumSamCourses() != 0) {
                this.personNameView.setText(person.getName());
                this.numSameView.setText(String.valueOf(person.getNumSamCourses()));
                if (person.isFavorite()) this.favorite.setText("✩");
                else this.favorite.setText("✩");
            }
        }


        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, ProfileActivity.class);
            List<Course> person_course = this.person.getCourses();
            String name = this.person.getName();
            String profilePictureUrl = person.getProfilePictureUrl();
            int userId = person.getId();
            ArrayList<String> courseArray = courseArrayMaker(person_course, name);
            intent.putStringArrayListExtra("course_names", courseArray);
            intent.putExtra("userId", userId);
            intent.putExtra("name", name);
            intent.putExtra("profile_picture_url", profilePictureUrl);

            context.startActivity(intent);
        }

//        public void updateFavorite() {
//            AppDatabase db = AppDatabase.singleton(this);
//        }
    }

    public static ArrayList<String> courseArrayMaker(List<Course> courses, String name) {
        ArrayList<String> courseArray = new ArrayList<>();
//        courseArray[0] = name;
        for (int i = 0; i < courses.size(); i++) {
            String courseDepartment = courses.get(i).department;
            String courseNumber = Integer.toString(courses.get(i).course_number);
            String courseYear =  Integer.toString(courses.get(i).year);
            String courseQuarter = courses.get(i).quarter;
            String courseInfo = courseDepartment + " " + courseNumber + " " + courseYear + " " + courseQuarter;
//            courseArray[i+1] = courseInfo;
            courseArray.add(courseInfo);
        }
        return courseArray;
    }
}