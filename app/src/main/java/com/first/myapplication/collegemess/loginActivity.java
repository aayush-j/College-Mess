package com.first.myapplication.collegemess;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static android.app.Notification.DEFAULT_SOUND;
import static com.first.myapplication.collegemess.App.CHANNEL_1_ID;


public class loginActivity extends AppCompatActivity {

    Spinner mySpinner, timeSpinner;
    BottomNavigationView bottomNavigationView;
    DatabaseReference databaseMess;
    TextView menuItem;
    Spinner day;
    Spinner meal;
    Button update;
    ProgressBar progressBar;





    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        timeSpinner = findViewById(R.id.spinner2);
        mySpinner = findViewById(R.id.spinner1);
        progressBar = findViewById(R.id.pbar);



        databaseMess = FirebaseDatabase.getInstance().getReference("MessMenu");
        menuItem = findViewById(R.id.menuDisplay);
//        day = (Spinner)findViewById(R.id.DayRetrieve);
//        meal = (Spinner)findViewById(R.id.mealRetrieve);
        update = findViewById(R.id.submit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                getMenu();
                progressBar.setVisibility(View.GONE);

            }
        });



        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(loginActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(loginActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.time));
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        bottomNavigationView = findViewById(R.id.navigation);




            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_signout:
                            FirebaseAuth.getInstance().signOut();

                            Intent i = new Intent(loginActivity.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                            return true;
                        case R.id.nav_complaint:
                            Intent intent2 = new Intent(loginActivity.this, instructions.class);
                            startActivity(intent2);

                            return true;
                        case R.id.nav_feedback:
                            Intent intent = new Intent(loginActivity.this,Feedback.class);
                            startActivity(intent);
                            return true;
//                    case R.id.nav_notifications:
//                        return true;
                    }
                    return false;
                }

            });


        //code for scheduling notification


        Intent notificationIntent = new Intent("nikhil.action.DISPLAY_NOTIFICATION");
        PendingIntent contentIntent = PendingIntent.getBroadcast(loginActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), contentIntent );



    }






    private void getMenu(){
        String weekDay = mySpinner.getSelectedItem().toString();
        String mealType = timeSpinner.getSelectedItem().toString();
        Log.v("check","getMenu is called");

//        final AlertDialog.Builder a_builder = new AlertDialog.Builder(loginActivity.this);


        databaseMess.child(weekDay).child(mealType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                    messmenu changedPost = dataSnapshot.getValue(messmenu.class);

                    String item = changedPost.getMenuItem();
                    menuItem.setText(item);
                }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }










}