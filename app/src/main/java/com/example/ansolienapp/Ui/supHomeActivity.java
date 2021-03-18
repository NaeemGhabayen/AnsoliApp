package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.Fragment.myPatientFragment;
import com.example.ansolienapp.Fragment.mySupervisionFragment;
import com.example.ansolienapp.Fragment.profileFragment;
import com.example.ansolienapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class supHomeActivity extends AppCompatActivity {
    Intent intent;
    String type;
    ImageView signOut;
    FirebaseAuth auth;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private BottomNavigationView.OnNavigationItemSelectedListener nvgition =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.profileFragment:
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                    new profileFragment(type)).addToBackStack("").commit();
                            break;
                        case R.id.mySupervisionFragment:
                            if (type.equals("As Doctor")) {
                                menuItem.setTitle("My Patient");
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                        new myPatientFragment()).addToBackStack("").commit();
                            } else {
                                menuItem.setTitle("supervision");
                                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                                        new mySupervisionFragment()).addToBackStack("").commit();

                            }
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_home);
        signOut = findViewById(R.id.signOut);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(nvgition);
        intent = getIntent();
        type = intent.getStringExtra("type");
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        profileFragment profileFragment = new profileFragment(type);
        profileFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                profileFragment).addToBackStack("").commit();
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
    }
}