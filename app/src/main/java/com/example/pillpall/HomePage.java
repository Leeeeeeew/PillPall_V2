package com.example.pillpall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {


    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Reminder> dataholder = new ArrayList<Reminder>();                                               //Array list to add reminders and display in recyclerview
    myAdapter adapter;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);                     //Floating action button to change activity
        database = FirebaseDatabase.getInstance().getReference("reminders");

        dataholder = new ArrayList<Reminder>();

        adapter = new myAdapter(this, dataholder);
        mRecyclerview.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Reminder reminder = dataSnapshot.getValue(Reminder.class);
                    dataholder.add(reminder);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateNewReminder.class);
                startActivity(intent);                                                              //Starts the new activity to add Reminders
            }
        });

//        Cursor cursor = new dbManager(getApplicationContext()).readallreminders();                  //Cursor To Load data From the database
//        while (cursor.moveToNext()) {
//            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
//            dataholder.add(model);
//        }



        adapter = new myAdapter(this, dataholder);
        mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview

    }

    @Override
    public void onBackPressed() {
        finish();                                                                                   //Makes the user to exit form the app
        super.onBackPressed();

    }
}