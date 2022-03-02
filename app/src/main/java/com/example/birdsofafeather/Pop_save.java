package com.example.birdsofafeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class Pop_save extends Activity {

    private int test_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Intent intent = getIntent();
        test_user_id = intent.getIntExtra("user_id", -1);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_window_save);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.3));

    }

    public void saveClicked(View view) {
        Intent intent = new Intent(this, FindNearbyActivity.class);
        intent.putExtra("user_id", test_user_id);
        startActivity(intent);
        finish();
    }
}
