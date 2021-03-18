package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ansolienapp.Adapter.itemAdapter;
import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class myPatientFragment extends Fragment {
    BarChart barChart;
    Random random;
    ArrayList<BarEntry> barEntries;
    ArrayList<String> date;
    EditText et_email;
    Button btn_add;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String email, userId, idRelative;
    RecyclerView rv_relative;
    List<Relative> relativeList;
    itemAdapter adapter;

    public myPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_patient, container, false);
        barChart = (BarChart) view.findViewById(R.id.bargraph);
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(66f, 2));
        barEntries.add(new BarEntry(44f, 3));
        barEntries.add(new BarEntry(50f, 4));
        barEntries.add(new BarEntry(91f, 5));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        date = new ArrayList<>();
        date.add("a");
        date.add("b");
        date.add("c");
        date.add("d");
        date.add("e");
        date.add("f");
        BarData barData = new BarData(date, barDataSet);
        barChart.setData(barData);
        barChart.setTouchEnabled(true);
        et_email = view.findViewById(R.id.et_email);
        btn_add = view.findViewById(R.id.btn_add);
        rv_relative = view.findViewById(R.id.rv_relative);
        relativeList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        relativeList = new ArrayList<>();
        ViewData();
    }


    private void ViewData() {
        relativeList.clear();
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("My Doctor").whereEqualTo("idDoctor", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Toast.makeText(getContext(), "حدث خطا ما ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.getId();
                            String email = doc.get("email").toString();
                            String userName = doc.get("userName").toString();
                            relativeList.add(new Relative(email, userName));
                        }
                        adapter = new itemAdapter(getContext(), relativeList);
                        rv_relative.setHasFixedSize(true);
                        rv_relative.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_relative.setAdapter(adapter);
                    }
                });

    }

}