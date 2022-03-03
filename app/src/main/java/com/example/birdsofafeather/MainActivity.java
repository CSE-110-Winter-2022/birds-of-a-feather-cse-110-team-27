package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.birdsofafeather.db.AppDatabase;
import com.example.birdsofafeather.db.session.Session;
import com.example.birdsofafeather.db.session.SessionWithUsers;
import com.example.birdsofafeather.db.user.User;
import com.example.birdsofafeather.db.user.UserWithCourses;
import com.example.birdsofafeather.utils.Utilities;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserWithCourses user;
    private SignInButton signin;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private static final String TAG = "Main Activity";

    @Override
    protected void onStart() {
        super.onStart();
        Bluetooth.requestEnableBluetooth(this);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        /**
        long session_id = db.sessionWithUsersDao().insertSession(new Session());
        SessionWithUsers sessionWithUsers = db.sessionWithUsersDao().getForId(session_id);
        sessionWithUsers.setSessionName("CSE 110");
        db.sessionWithUsersDao().addUsersToSession(
                session_id,
                Arrays.asList(
                        new User(5, "Anthony Tarbinian", "atarbini@ucsd.edu", "https://google.com")));

         //this session_id2 will be identical to session_id
        long session_id2 = db.sessionWithUsersDao().insertSession(sessionWithUsers.getSession());
        SessionWithUsers sessionWithUsers2 = db.sessionWithUsersDao().getForId(session_id2);
        List<User> users = db.sessionWithUsersDao().getUsersForSessionId(session_id2);
         **/


        setContentView(R.layout.activity_main);

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(this.TAG, "Signing in with google going to Confirm Name Activity");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Intent intent = new Intent(MainActivity.this, ConfirmNameActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void onEnterClassClicked(View view) {
        if(user == null){
            Utilities.showAlert(this,"SIGN IN FIRST");
            return;
        }
        Context context = view.getContext();
        Intent intent = new Intent(context, EnterClassActivity.class);
        intent.putExtra("userId", user.getId());
        context.startActivity(intent);
    }




}