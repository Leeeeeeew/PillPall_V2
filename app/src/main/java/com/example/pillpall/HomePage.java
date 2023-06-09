package com.example.pillpall;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    FloatingActionButton mCreateRem, settingBtn;

    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder;  //Array list to add reminders and display in recyclerview
    DatabaseReference database;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("reminders");
        dataholder = new ArrayList<>();
        adapter = new MyAdapter(this, dataholder);

        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);  //Floating action button to change activity
        settingBtn = (FloatingActionButton) findViewById(R.id.settingBtn);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    dataholder.add(model);
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
                startActivity(intent);  //Starts the new activity to add Reminders
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), settings.class);
                startActivity(intent);
            }
        });

        mRecyclerview.setAdapter(adapter);  //Binds the adapter with recyclerview

        mRecyclerview.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(HomePage.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = mRecyclerview.findChildViewUnder(e.getX(), e.getY());

                    if (childView != null) {
                        int position = mRecyclerview.getChildAdapterPosition(childView);

                        // Fetch the reminder to delete
                        Model reminderToDelete = dataholder.get(position);

                        // Delete the reminder from Firebase
                        database.child(reminderToDelete.getId()).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(HomePage.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();

                                        // Delete the item from the adapter and notify the adapter
                                        adapter.deleteItem(position);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(HomePage.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();  //Makes the user to exit from the app
        super.onBackPressed();
    }
}
