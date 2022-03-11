package com.example.birdsofafeather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.course.Course;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.generator.Generator;
import com.example.birdsofafeather.generator.WaveCSVGenerator;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class PersonsViewAdapter extends RecyclerView.Adapter<PersonsViewAdapter.ViewHolder> {
    private final List<? extends UserWithCourses> persons;
    public final long testUserID;
    private FindNearbyService currFindNearbyService;

    public PersonsViewAdapter(List<? extends UserWithCourses> persons, long testUserID, FindNearbyService findNearbyService) {
        super();
        this.persons = persons;
        this.testUserID = testUserID;
        this.currFindNearbyService = findNearbyService;
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
        holder.setUserID(testUserID);
        holder.setCurrFindNearbyService(currFindNearbyService);
    }

    @Override
    public int getItemCount() {
        return this.persons.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView personNameView;
        private Button wave;
        public UserWithCourses person;
        private TextView numSameView;
        private TextView favorite;
        private long userID = -1;
        private FindNearbyService currFindNearbyService;


        ViewHolder(View itemView) {
            super(itemView);
            this.personNameView = itemView.findViewById(R.id.person_row_name);
            this.numSameView = itemView.findViewById(R.id.num_same_courses_view);
            this.favorite = itemView.findViewById(R.id.favorite);
            this.wave = itemView.findViewById(R.id.wave);

            itemView.setOnClickListener(this);

            itemView.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long userId = person.getId();
                    AppDatabase db = AppDatabase.singleton(v.getContext());
                    UserWithCourses user = db.userWithCoursesDao().getUser(userId);
                    user.toggleFavorite();
                    db.userWithCoursesDao().insert(user.user);
                    setPerson(user);
                    favoriteClicked(v, user.isFavorite());
                }

            });

            itemView.findViewById(R.id.wave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    waveClicked(v);
                }
            });
        }

        public void waveClicked(View view){
            Context context = view.getContext();
            wave.setEnabled(false);
            if (this.userID == -1) {
                Toast.makeText(context, "Can't find current user in db", Toast.LENGTH_SHORT).show();
                return;
            }
            Generator generator = new WaveCSVGenerator();
            String resultCSV = generator.generateCSV(context, this.userID, this.person.getId());
            Toast.makeText(context, resultCSV, Toast.LENGTH_SHORT).show();

            if(currFindNearbyService != null) {
                currFindNearbyService.updateCurMessage(resultCSV);
            }
        }

        public void setCurrFindNearbyService(FindNearbyService findNearbyService) {
           this.currFindNearbyService = findNearbyService;
        }

        public void setPerson(UserWithCourses person) {
            if (this.person == null) this.person = person;
            else this.person.user = person.user;

            if(person.getNumSamCourses() != 0) {
                this.personNameView.setText(person.getName());
                if (person.user.wavedToMe) {
                    this.wave.setText("Wave back");
                    this.wave.setBackgroundColor(Color.RED);
                }
                this.numSameView.setText(String.valueOf(person.getNumSamCourses()));
                if (person.isFavorite()) this.favorite.setText("⭐");
                else this.favorite.setText("✩");
            }
        }

        public void setUserID(long userID) {
            this.userID = userID;
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, ProfileActivity.class);
            List<Course> person_course = this.person.getCourses();
            String name = this.person.getName();
            String profilePictureUrl = person.getProfilePictureUrl();
            Boolean isFavorite = person.isFavorite();
            long userId = person.getId();
            ArrayList<String> courseArray = courseArrayMaker(person_course, name);
            intent.putStringArrayListExtra("course_names", courseArray);
            intent.putExtra("userId", userId);
            intent.putExtra("name", name);
            intent.putExtra("profile_picture_url", profilePictureUrl);
            intent.putExtra("favorite", isFavorite);

            context.startActivity(intent);
        }

        public void favoriteClicked(View view, Boolean isFavorite){
            Context context = view.getContext();
            if (isFavorite) Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            else Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
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