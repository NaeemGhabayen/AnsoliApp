package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ansolienapp.Adapter.itemAdapter;
import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class mySupervisionFragment extends Fragment {
    EditText et_email;
    Button btn_add;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String email, userId, idRelative;
    RecyclerView rv_relative;
    List<Relative> relativeList;
    itemAdapter adapter;
    String isAccess, mail;
    String pantint_id;
    RecyclerView rv_patient_readings;
    itemAdapter adapter1;
    List<Relative> relativeList1;

    public mySupervisionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_supervision, container, false);

        rv_patient_readings = view.findViewById(R.id.rv_patient_readings);
        et_email = view.findViewById(R.id.et_email);
        btn_add = view.findViewById(R.id.btn_add);
        rv_relative = view.findViewById(R.id.rv_relative);
        relativeList = new ArrayList<>();
        relativeList1 = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("?????? ?????????? ???????????? ????????????????????");
                    return;
                }
                GiveAceses();
            }
        });
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
        db.collection("My Relative").whereEqualTo("idRelative", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Toast.makeText(getContext(), "?????? ?????? ???? ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.getId();
                            String isAc = doc.get("isAccess").toString();
                            Toast.makeText(getContext(), "" + isAc, Toast.LENGTH_SHORT).show();
                            String email = doc.get("email").toString();
                            getIdInformation(email);
                            String userName = doc.get("userName").toString();
                            if (isAc.equals("true"))
                                relativeList.add(new Relative(email, userName, "s"));

                        }
                        adapter = new itemAdapter(getContext(), relativeList);
                        rv_relative.setHasFixedSize(true);
                        rv_relative.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_relative.setAdapter(adapter);
                    }
                });

    }

    private void getIdInformation(String email) {
        db.collection("As Patient").whereEqualTo("email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            pantint_id = doc.getId();
                            Toast.makeText(getContext(), "sadsa" + pantint_id, Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "onEvent: " + pantint_id);
                            getInormationRading(pantint_id);
                        }

                    }
                });
    }

    private void getInormationRading(String pantint_id) {
        relativeList1.clear();
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Reding").whereEqualTo("user_id", pantint_id)
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
                                relativeList1.add(new Relative(time, reading , "report"));
                            }else if(x<80){
                                reading = x+" Low value";
                                relativeList1.add(new Relative(time, reading , "report"));
                            }else {
                                reading = x+" Normal value";
                                relativeList1.add(new Relative(time, reading , "report"));
                            }



                        }
                        adapter1 = new itemAdapter(getContext(), relativeList1);
                        rv_patient_readings.setHasFixedSize(true);
                        rv_patient_readings.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_patient_readings.setAdapter(adapter1);
                    }
                });

    }

    private void GiveAceses() {
        relativeList.clear();
        relativeList = new ArrayList<>();
        email = et_email.getText().toString();
        String emaill = auth.getCurrentUser().getEmail();
        db.collection("My Relative").whereEqualTo("idRelative", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Toast.makeText(getContext(), "???????????? ???????????? ???? ?????????????? ????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.get("idRelative").toString();
                            userId = doc.get("userId").toString();
                            // String isAccess = doc.get("isAccess").toString();
                            mail = doc.get("email").toString();
                            if (mail.equals(et_email.getText().toString())) {
                                isAccess = "true";
                            }
                            Map<String, Object> myRelative = new HashMap<>();
                            myRelative.put("emailRelative", emaill);
                            myRelative.put("userId", userId);
                            myRelative.put("idRelative", idRelative);
                            myRelative.put("email", mail);
                            myRelative.put("isAccess", isAccess);
                            db.collection("My Relative").document(idRelative + userId)
                                    .update(myRelative)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(getContext(), "???? ?????????? ???????????? ??????????", Toast.LENGTH_SHORT).show();
                                                et_email.setText("");


                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "onFailure: " + e.getMessage());
                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                    }
                });


    }
}