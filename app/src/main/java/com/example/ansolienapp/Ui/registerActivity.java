package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.R;

public class registerActivity extends AppCompatActivity {
    Button btn_AsPatient, btn_AsDoctor, btn_AsRelative;
    TextView tv_show;
    Intent intent;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_show = findViewById(R.id.tv_show);
        btn_AsPatient = findViewById(R.id.btn_AsPatient);
        btn_AsDoctor = findViewById(R.id.btn_AsDoctor);
        btn_AsRelative = findViewById(R.id.btn_AsRelative);

        intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("login")) {
            tv_show.setText("Login");
        }

        btn_AsPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("login")) {
                    startActivity(new Intent(getApplicationContext(), loginActivity.class)
                            .putExtra("type", "As Patient"));
                } else {
                    startActivity(new Intent(getApplicationContext(), signUpActivity.class)
                            .putExtra("type", "As Patient"));
                }
                finish();
            }
        });

        btn_AsDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("login")) {
                    startActivity(new Intent(getApplicationContext(), loginActivity.class)
                            .putExtra("type", "As Doctor"));
                } else {
                    startActivity(new Intent(getApplicationContext(), signUpActivity.class)
                            .putExtra("type", "As Doctor"));
                }
                finish();
            }
        });

        btn_AsRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("login")) {
                    startActivity(new Intent(getApplicationContext(), loginActivity.class)
                            .putExtra("type", "As Relative"));
                } else {
                    startActivity(new Intent(getApplicationContext(), signUpActivity.class)
                            .putExtra("type", "As Relative"));
                }
                finish();
            }
        });

    }


}