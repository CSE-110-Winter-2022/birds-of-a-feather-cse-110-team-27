package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfirmNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_confirm);

        //Got intent from previous activity
        Intent intent =getIntent();
        String name = intent.getStringExtra("name");

        //If the name got was nontrivial, put it into the name box.
        if(name != null && !name.equals("")){
            TextView name_input_textView = findViewById(R.id.name_input_textView);
            name_input_textView.setText(name);
        }
    }


    public void nameConfirmOnClick(View view) {

        //Get the name from the text box
        TextView name_input_textView = findViewById(R.id.name_input_textView);
        String name = name_input_textView.getText().toString();

        //If user didn't provide a name, we show an alert.
        if(name.equals("")){
            Utility.showAlert(this, "You need a name to continue!");
            return;
        }


        //Save the name into SharedPreferences when user hits "Confirm"
        SharedPreferences sp =getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("name", name);
        editor.apply();

        //TODO: Update intent to next activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}