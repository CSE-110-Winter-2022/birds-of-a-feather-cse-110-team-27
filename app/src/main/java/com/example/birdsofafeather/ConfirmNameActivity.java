package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Utilities;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ConfirmNameActivity extends AppCompatActivity {
    ImageView photo;
    TextView name, email, id;
    Button signOut;
    GoogleSignInClient mGoogleSignInClient;
    String personName;
    String personEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_confirm);

        //Got intent from previous activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");


//        signOut = findViewById(R.id.sign_out_button);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) {
//                    // ...
//                    case R.id.sign_out_button:
//                        signOut();
//                        break;
//                    // ...
//                }
//            }
//        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            //If the name got was nontrivial, put it into the name box.
            if(personName != null && !personName.equals("")){
                TextView name_input_textView = findViewById(R.id.name_input_textView);
                name_input_textView.setText(personName);
            }
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
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("name", name);
        editor.apply();

        AppDatabase db = AppDatabase.singleton(this);
//        Utilities.showAlert(this, personEmail);
//        Utilities.showAlert(this, name);
        int userId = personEmail.hashCode();
        UserWithCourses userWithCourses = db.userWithCoursesDao().getUser(userId);
        User user;
        if (userWithCourses == null) {
            user = new User(userId, name, personEmail);
            db.userWithCoursesDao().insert(user);
        } else {
            user = userWithCourses.user;
        }

        if(user == null){
            Utilities.showAlert(this,"SIGN IN FIRST");
            return;
        }

//        Utilities.showAlert(this, user.getName());
        Intent enterClassesIntent = new Intent(this, EnterClassActivity.class);
        enterClassesIntent.putExtra("user_id", user.getId());
        startActivity(enterClassesIntent);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}