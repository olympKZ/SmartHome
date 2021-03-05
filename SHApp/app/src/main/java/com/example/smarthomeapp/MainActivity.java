package com.example.smarthomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Importing libraries for retrieving data from the database
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;  // the most important part of this app
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView temp, sound, move, buzzer;
    Button btn;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = (TextView) findViewById(R.id.tempResText);
        sound = (TextView) findViewById(R.id.soundResText);
        move = (TextView) findViewById(R.id.moveResText);
        buzzer = (TextView) findViewById(R.id.buzzerResText);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tempHum = snapshot.child("temp_hum").getValue().toString();
                        String snd = snapshot.child("sound").getValue().toString();
                        String mv = snapshot.child("motion").getValue().toString();
                        String bzr = snapshot.child("buzzer").child("status").getValue().toString();

                        temp.setText(tempHum);
                        sound.setText(snd);
                        move.setText(mv);
                        buzzer.setText(bzr);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}