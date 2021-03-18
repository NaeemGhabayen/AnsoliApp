package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.Fragment.homeFragment;
import com.example.ansolienapp.Fragment.myDoctorFragment;
import com.example.ansolienapp.Fragment.myReadingFragment;
import com.example.ansolienapp.Fragment.profileFragment;
import com.example.ansolienapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity {
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
                        case R.id.homeFragment:
                            Bundle bundle=new Bundle();
                            bundle.putString("type", pref.getString("type","t"));
                            homeFragment homeFragment =new homeFragment();
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
        if (!pref.getString("keeplogin","false").equals("true")){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }else {
            type = pref.getString("type","t");

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
        homeFragment homeFragment =new homeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                 homeFragment).addToBackStack("").commit();
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                editor = pref.edit();
                editor.clear();
                editor.apply();
            }
        });
    }
}