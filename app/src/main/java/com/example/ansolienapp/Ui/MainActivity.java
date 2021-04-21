package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.R;

public class MainActivity extends AppCompatActivity {
    Button btn_register, btn_login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();
        pref = getApplicationContext().getSharedPreferences("MyFirstPref", MODE_PRIVATE);
        if (GetDataFromPref("keeplogin", "false").equals("true") &&
                !GetDataFromPref("type","t").equals("As Patient")) {
            startActivity(new Intent(getApplicationContext(), supHomeActivity.class));
        }else if (GetDataFromPref("keeplogin", "false").equals("true") &&
                GetDataFromPref("type","t").equals("As Patient")){
            startActivity(new Intent(getApplicationContext(), homeActivity.class));

        }
    }

    public String GetDataFromPref(String key, String defultvalue) {
        return pref.getString(key, defultvalue);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerActivity.class)
                        .putExtra("type", "register"));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), registerActivity.class)
                        .putExtra("type", "login"));
            }
        });
    }
}