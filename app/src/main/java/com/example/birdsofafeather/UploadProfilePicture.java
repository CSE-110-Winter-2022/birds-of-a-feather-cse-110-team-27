package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class UploadProfilePicture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        ImageView display_photo_ImageView = findViewById(R.id.display_photo);
        // The user's profile picture should be here:
        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8d/President_Barack_Obama.jpg/1200px-President_Barack_Obama.jpg").into(display_photo_ImageView);
    }

    public void onUploadClick(View view) {
        TextView image_url_textView = findViewById(R.id.photo_url);
        ImageView display_photo_ImageView = findViewById(R.id.display_photo);
        // URL of the image
        String image_url = image_url_textView.getText().toString();

        display_photo_ImageView.setImageResource(R.drawable.title_image);
//        if (image_url.startsWith("https://photos.app.goo.gl/") || image_url.startsWith("photos.app.goo.gl/")) {
            Picasso.get()
                    .load(image_url)
                    .error(R.drawable.default_pfp)
                    .into(display_photo_ImageView);
        }
//        else {
//            Picasso.get()
//                    .load(R.drawable.default_pfp)
//                    .into(display_photo_ImageView);
//        }
//    }

    public void closeActivity(View view) {
        finish();

    }
}