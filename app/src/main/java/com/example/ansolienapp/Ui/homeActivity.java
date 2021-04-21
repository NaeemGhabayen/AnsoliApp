package com.example.ansolienapp.Ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ansolienapp.Fragment.homeFragment;
import com.example.ansolienapp.Fragment.myDoctorFragment;
import com.example.ansolienapp.Fragment.myReadingFragment;
import com.example.ansolienapp.Fragment.profileFragment;
import com.example.ansolienapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class homeActivity extends AppCompatActivity {
    private static final int FM_NOTIFICATION_ID = 10;
    Intent intent;
    String type;
    ImageView signOut;
    FirebaseAuth auth;
String user_id;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private BottomNavigationView.OnNavigationItemSelectedListener nvgition =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.homeFragment:
                            Bundle bundle = new Bundle();
                            bundle.putString("type", pref.getString("type", "t"));
                            homeFragment homeFragment = new homeFragment();
                            homeFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    homeFragment).addToBackStack("").commit();
                            break;
                        case R.id.profileFragment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new profileFragment(type)).addToBackStack("").commit();
                            break;

                        case R.id.myDoctorFragment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new myDoctorFragment()).addToBackStack("").commit();
                            break;
                        case R.id.myReadingFragment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new myReadingFragment()).addToBackStack("").commit();
                            break;
                    }
                    return true;

                }


            };

    @Override
    protected void onStart() {
        super.onStart();
        pref = getApplicationContext().getSharedPreferences("MyFirstPref", MODE_PRIVATE);
        if (!pref.getString("keeplogin", "false").equals("true")) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            type = pref.getString("type", "t");

            //   Toast.makeText(this, "id"+pref.getString("id","")+"\n"+type, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        signOut = findViewById(R.id.signOut);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nvgition);
        intent = getIntent();
        homeFragment homeFragment = new homeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                homeFragment).addToBackStack("").commit();
        auth = FirebaseAuth.getInstance();
        user_id = auth.getUid();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                editor = pref.edit();
                editor.clear();
                editor.apply();
            }
        });
        ViewData();

    }
    private void ViewData() {
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Reding").whereEqualTo("user_id", user_id)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            String reading = doc.get("reading").toString();
                               int x = Integer.parseInt(reading);
                               if (x>=180){
                                   SetNotification("Your sugar value is very high");
                               }else if(x<80){
                                   SetNotification("Your sugar value is very low");

                               }

                        }

                    }
                });

    }

    private void SetNotification(String x) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Warring")
                .setContentText(x)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(x))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());

    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "desc";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);

            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}