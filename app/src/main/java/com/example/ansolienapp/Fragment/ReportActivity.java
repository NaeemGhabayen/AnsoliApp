package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ansolienapp.Adapter.itemAdapter;
import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    TextView tv_age, tv_type, tv_name;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String userId;
    RecyclerView rv_patient_readings;
    itemAdapter adapter ;
    List<Relative> relativeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        rv_patient_readings = findViewById(R.id.rv_patient_readings);
        tv_age = findViewById(R.id.tv_age);
        tv_type = findViewById(R.id.tv_type);
        tv_name = findViewById(R.id.tv_name);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        relativeList = new ArrayList<>();
        userId = auth.getUid();
        db.collection("As Patient").document(userId).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String fullName = task.getResult().get("fullName").toString();
                            String age = task.getResult().get("age").toString();
                            String type = task.getResult().get("type").toString();
                            tv_name.setText("The Patient name is "+fullName + "");
                            tv_age.setText("The Patient age is "+age + "");
                            tv_type.setText("The Patient type is "+type + "");

                        }
                    }
                });
    }

    private void ViewData() {
        relativeList.clear();
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Reding").whereEqualTo("user_id", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            String reading = doc.get("reading").toString();
                            String time = doc.get("time").toString();
                            int x = Integer.parseInt(reading);
                            if (x>=140){
                                reading = x+" High value";
                                relativeList.add(new Relative(time, reading , "report"));
                            }else if(x<80){
                                reading = x+" Low value";
                                relativeList.add(new Relative(time, reading , "report"));
                            }else {
                                reading = x+" Normal value";
                                relativeList.add(new Relative(time, reading , "report"));
                            }



                        }
                        adapter = new itemAdapter(getApplicationContext(), relativeList);
                        rv_patient_readings.setHasFixedSize(true);
                        rv_patient_readings.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_patient_readings.setAdapter(adapter);
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        relativeList = new ArrayList<>();
        ViewData();
    }
}