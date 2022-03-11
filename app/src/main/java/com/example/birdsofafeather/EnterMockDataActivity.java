package com.example.birdsofafeather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EnterMockDataActivity extends AppCompatActivity {
    private String mock_csv_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mock_data);
    }

    public void onSaveCSVClicked(View view) {
        EditText csvDataInput = findViewById(R.id.csvDataInput);
        mock_csv_data = csvDataInput.getText().toString();
        FindNearbyActivity.nearbyUsersMessage = mock_csv_data;
//        FindNearbyActivity.messageListener = new FakedMessageListener(realListener, 3, FindNearbyActivity.nearbyMessage);
        finish();
    }
}