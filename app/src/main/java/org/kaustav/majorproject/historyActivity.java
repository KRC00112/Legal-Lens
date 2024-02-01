package org.kaustav.majorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class historyActivity extends AppCompatActivity {


    private FirebaseUser user;
    private FirebaseAuth auth;

    private TextView userDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        userDetailsTextView = findViewById(R.id.user_details);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
            finish();
        } else {
            userDetailsTextView.setText(user.getEmail());
        }



    }
}