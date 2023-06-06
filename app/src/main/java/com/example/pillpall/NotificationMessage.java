package com.example.pillpall;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillpall.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//this class creates the Reminder Notification Message

public class NotificationMessage extends AppCompatActivity {
    TextView textView;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);

        textView = findViewById(R.id.tv_message);
        Bundle bundle = getIntent().getExtras();                                                    //call the data which is passed by another intent


        database = FirebaseDatabase.getInstance().getReference("reminders").child("title");

        String title = database.toString();
        textView.setText(bundle.getString(title));
    }
}
