package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReportActivity extends AppCompatActivity {
    TextView tv_age, tv_type , tv_name;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tv_age = findViewById(R.id.tv_age);
        tv_type = findViewById(R.id.tv_type);
        tv_name = findViewById(R.id.tv_name);

         db = FirebaseFirestore.getInstance();
         auth = FirebaseAuth.getInstance();


        userId = auth.getUid();

        db.collection("As Patient").document(userId).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String fullName = task.getResult().get("fullName").toString();
                    String age = task.getResult().get("age").toString();
                    String type = task.getResult().get("type").toString();

                    tv_name.setText(fullName+"");
                    tv_age.setText(age+"");
                    tv_type.setText(type+"");

                }
            }
        });
    }
}