package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);
        confirmButton = findViewById(R.id.button3);
        confirmButton.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        personEmail = intent.getStringExtra("email");


        ImageView display_photo_ImageView = findViewById(R.id.display_photo);
        // The user's profile picture should be here:
//        Picasso.get().load().into(display_photo_ImageView);
//        display_photo_ImageView.setImageDrawable(R.drawable.default_pfp);
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
        confirmButton.setVisibility(View.VISIBLE);
    }
//        else {
//            Picasso.get()
//                    .load(R.drawable.default_pfp)
//                    .into(display_photo_ImageView);
//        }
//    }

    public void onConfirmClicked(View view) {
        AppDatabase db = AppDatabase.singleton(this);
        int userId = personEmail.hashCode();
        UserWithCourses userWithCourses = db.userWithCoursesDao().getUser(userId);
        User user;
        if (userWithCourses == null) {
            user = new User(userId, name, personEmail, image_url);
            db.userWithCoursesDao().insert(user);
        } else {
            user = userWithCourses.user;
        }

        Intent enterClassesIntent = new Intent(this, EnterClassActivity.class);
        enterClassesIntent.putExtra("user_id", user.getId());
        startActivity(enterClassesIntent);
    }
}