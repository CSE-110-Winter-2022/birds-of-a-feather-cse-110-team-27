package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Utilities;
import com.squareup.picasso.Picasso;

public class UploadProfilePicture extends AppCompatActivity {
    private String personEmail;
    private String name;
    private String image_url;
    private static final String TAG = "Upload Profile";
    Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        personEmail = intent.getStringExtra("email");
        confirmBtn = findViewById(R.id.button3);


        ImageView display_photo_ImageView = findViewById(R.id.display_photo);
        confirmBtn.setVisibility(View.INVISIBLE);
    }

    public void onUploadClick(View view) {
        TextView image_url_textView = findViewById(R.id.photo_url);
        ImageView display_photo_ImageView = findViewById(R.id.display_photo);
        // URL of the image
        image_url = image_url_textView.getText().toString();

        display_photo_ImageView.setImageResource(R.drawable.title_image);
//        if (image_url.startsWith("https://photos.app.goo.gl/") || image_url.startsWith("photos.app.goo.gl/")) {
        if(!image_url.isEmpty()) {
            Picasso.get()
                    .load(image_url)
                    .error(R.drawable.default_pfp)
                    .into(display_photo_ImageView);
        }
        Log.d(this.TAG, "Previewing Photo");
        confirmBtn.setVisibility(View.VISIBLE);

    }

    public void onConfirmClicked(View view) {
        AppDatabase db = AppDatabase.singleton(this);
//        UserWithCourses userWithCourses = db.userWithCoursesDao().getUser(0);
        UserWithCourses userWithCourses = db.userWithCoursesDao().getUserForEmail(personEmail);
        User user;
        long userId;
        if (userWithCourses == null) {
            user = new User(name, personEmail, image_url);
            userId = db.userWithCoursesDao().insert(user);
        } else {
            user = userWithCourses.user;
            userId = userWithCourses.user.getId();
        }

        Intent enterClassesIntent = new Intent(this, EnterClassActivity.class);
        enterClassesIntent.putExtra("user_id", userId);
        startActivity(enterClassesIntent);
        Log.d(this.TAG, "Confirming Photo and Going to Find Nearby Activity");
    }
}