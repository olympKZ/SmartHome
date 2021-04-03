package com.example.smarthomeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

// Importing libraries for retrieving data from the database
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;  // the most important part of this app
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.*;

public class MainActivity extends AppCompatActivity {

    TextView temp, sound, move, buzzer;
    Button btn;
    DatabaseReference ref;

    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        temp = (TextView) findViewById(R.id.tempResText);
        sound = (TextView) findViewById(R.id.soundResText);
        move = (TextView) findViewById(R.id.moveResText);
        buzzer = (TextView) findViewById(R.id.buzzerResText);


        //removed the need to press the "GET" button to start getting readings
//        btn = (Button) findViewById(R.id.get);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
                ref = FirebaseDatabase.getInstance().getReference();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tempHum = snapshot.child("temp_hum").getValue().toString();
                        String snd = snapshot.child("sound").getValue().toString();
                        String mv = snapshot.child("motion").getValue().toString();
                        String bzr = snapshot.child("buzzer").child("status").getValue().toString();

                        //truncating so that it doesn't show milliseconds
//                        temp.setText(tempHum);
                        temp.setText(tempHum.substring(0,19).concat(tempHum.substring(26,47)));
//                        sound.setText(snd);
                        sound.setText(snd.substring(0,37));
//                        move.setText(mv);
                        move.setText(mv.substring(0,38));
                        buzzer.setText(bzr);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
//            }
//        });


//        ////testing notification
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(getString(R.string.notification_title), getString(R.string.notification_title), NotificationManager.IMPORTANCE_HIGH);
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.notification_title));
//        builder.setContentTitle(getString(R.string.app_name));
//        builder.setContentText(getString(R.string.notification_message));
//        builder.setSmallIcon(R.drawable.ic_baseline_warning_24);
//        builder.setAutoCancel(true);
//
//        // Create pending intent, mention the Activity which needs to be
//        //triggered when user clicks on notification(StopScript.class in this case)
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentIntent(contentIntent);
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
//        managerCompat.notify(1, builder.build());
//        ////

        // initiate a toggle
        ToggleButton alarm = (ToggleButton) findViewById(R.id.alarm);

        SharedPreferences sharedPrefs = getSharedPreferences("toggle", MODE_PRIVATE);
        alarm.setChecked(sharedPrefs.getBoolean("toggle", true));

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                DatabaseReference ref;
                ref = FirebaseDatabase.getInstance().getReference();
                if (isChecked) {
                    // The toggle is enabled

                    //save ON toggle state
                    SharedPreferences.Editor toggle = getSharedPreferences("toggle", MODE_PRIVATE).edit();
                    toggle.putBoolean("toggle", true);
                    toggle.commit();

                    ref.child("alarm").setValue("on");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String notification = snapshot.child("notification").getValue().toString();

                            //if(snapshot.child("notification").getValue().toString()=="on"){
                            if(snapshot.child("notification").getValue().toString().equals("on")){
                                Toast.makeText(MainActivity.this, "ALARM is ON", Toast.LENGTH_SHORT).show();

                                ////latest ALARM notification
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel(getString(R.string.notification_title), getString(R.string.notification_title), NotificationManager.IMPORTANCE_HIGH);
                                    NotificationManager manager = getSystemService(NotificationManager.class);
                                    manager.createNotificationChannel(channel);
                                }
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.notification_title));
                                builder.setContentTitle(getString(R.string.app_name));
                                builder.setContentText(getString(R.string.notification_message));
                                builder.setSmallIcon(R.drawable.ic_baseline_warning_24);
                                builder.setAutoCancel(true);
                                // Create pending intent, mention the Activity which needs to be
                                //triggered when user clicks on notification(StopScript.class in this case)
                                PendingIntent contentIntent = PendingIntent.getActivity(buttonView.getContext(), 0,
                                        new Intent(buttonView.getContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                                builder.setContentIntent(contentIntent);

                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                                managerCompat.notify(1, builder.build());
                                ////

//                                ////old not pressing notification with no icon
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    NotificationChannel channel = new NotificationChannel(getString(R.string.notification_title), getString(R.string.notification_title), NotificationManager.IMPORTANCE_HIGH);
//                                    NotificationManager manager = getSystemService(NotificationManager.class);
//                                    manager.createNotificationChannel(channel);
//                                }
//
//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, getString(R.string.notification_title));
//                                builder.setContentTitle(getString(R.string.app_name));
//                                builder.setContentText(getString(R.string.notification_message));
//                                builder.setSmallIcon(R.drawable.ic_launcher_background);
//                                builder.setAutoCancel(true);
//
//                                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
//                                managerCompat.notify(1, builder.build());
//                                ////

                                ref.child("notification").setValue("off");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                    // The toggle is disabled

                    //save OFF toggle state
                    SharedPreferences.Editor toggle = getSharedPreferences("toggle", MODE_PRIVATE).edit();
                    toggle.putBoolean("toggle", false);
                    toggle.commit();

                    ref.child("alarm").setValue("off");

                }
            }
        });

        // check current state of a Switch (true or false).
        //Boolean alarmState = alarm.isChecked();
        // if alarmsState = true then update alarm value on database to on
        // and then buzzer reads that value and if its on the buzzer is on the loop
        //where it constantly checks if the sensors have been updated in database and
        //if they did then buzzer turns on and the app gives notification
        //alarm.setChecked(false);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
     //handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                //Toast.makeText(MainActivity.this, "LOGOUT TEST", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();

                loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                return true;
//                break;

            case R.id.about:
                startActivity(new Intent(MainActivity.this, About.class));
                return true;
//                break;

            default:
                return super.onOptionsItemSelected(item);
        }
//        return false;
    }
}