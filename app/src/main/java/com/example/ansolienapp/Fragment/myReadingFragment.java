package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ansolienapp.Adapter.itemAdapter;
import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class myReadingFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseFirestore db;
    String userId;
    RecyclerView rv_patient_readings;
    itemAdapter adapter ;
    List<Relative> relativeList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_my_reading, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        relativeList = new ArrayList<>();
        userId = auth.getUid();
//
        rv_patient_readings = view.findViewById(R.id.rv_patient_readings);
        return view;
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
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        adapter = new itemAdapter(getContext(), relativeList);
                        rv_patient_readings.setHasFixedSize(true);
                        rv_patient_readings.setLayoutManager(new LinearLayoutManager(getContext()));
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