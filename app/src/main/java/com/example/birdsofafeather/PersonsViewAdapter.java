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
            Intent intent = new Intent(context, ProfileActivity.class);
            List<Course> person_course = this.person.getCourses();
            String name = this.person.getName();
            ArrayList<String> courseArray = courseArrayMaker(person_course, name);
            intent.putStringArrayListExtra("course_names", courseArray);

            intent.putExtra("name", person.getName());
            intent.putExtra("profile_picture_url", "https://lh3.googleusercontent.com/c44HXj2-lQgykjwSvTt4MboXj51qMieDWk8ePaYDCQa5-Hsd9HmQyyB-1j17xfyaFTe9AV_B2kt0vdxl33-_PVt8Hl9OCTN2Ajah-Q-UN7vNi-rFofQeGtcaN4dfWtwogctirbt1X400W62e0Hwb27ezEvrUGEuiCdcWtemY6I-DXin7jA8uUHfpn39m7mHZxZLTJGv6agZk27Qu6vYLb2xKRNy7oPjopO5VqQ4q24nrzXhAftM42P9rDv9PqQ4uk-4b3LPCo8MUOWpPyljNsfJZwiuKkwE_QHbmT28FEhLWp2Hj55fqj_NK5bz0w-ubWl5TjZJWrAxNYVz0Ci41-KqDO8ziefTBpbeSA-B1eN-w6PDQvn34I4bnV_NeihGBLZRNap4rpkk4_k4iD2wZEWOiexzCHE0KPrp8wzTzbWbauIPkIzqvKWCjGMswrqE5zjLvmlqGHI7FsItPPLylm6iLx6hlvPWlJOgzoNCwBiNcgg4LPwsKNByTPprLF9Gw3BeZHVRcZisPiXzzMHIVqZHcTYNWAoQyD_tXz8F5rJIisXv27wrD3QeLxgediWDt5d4LRuQdYRePyGe04if-9GZDlgLiggThm-Bxj03oW8Tvw5nGb2EyAE9MlNKzo3SZosY9KQJ7ZwAuXyfQK1U2EDdG1qDTC9vWZzdZmc5_AZqYQVw177KtaXUZ6q6pAxpajh_8p4ninlnFWMlOK1p0B_gqHznk5qu4PleJWgWWBX6rRnuuYSEiwrUkPjo-CuCYT3Dnp0yf2YvufGHg=w96-h72-no");

            context.startActivity(intent);
        }
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