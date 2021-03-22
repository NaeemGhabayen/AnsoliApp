package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class alertActivity extends AppCompatActivity {
    Button btn_next;
    Intent intent;
    String type, userId;
    RadioButton rg_eventType;
    RadioGroup rg_type;
    String typeAlarm;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        btn_next = findViewById(R.id.btn_next);
        rg_type = findViewById(R.id.rg_type);
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        type = intent.getStringExtra("type");
        userId = intent.getStringExtra("userId");
        int selectedId = rg_type.getCheckedRadioButtonId();
        rg_eventType = findViewById(selectedId);
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rg_eventType = findViewById(checkedId);
                typeAlarm = rg_eventType.getText().toString();
           //     Toast.makeText(alertActivity.this, "" + typeAlarm, Toast.LENGTH_SHORT).show();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            db.collection(type).document(userId).update("type alarm",typeAlarm)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(alertActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            }
        });
    }
}