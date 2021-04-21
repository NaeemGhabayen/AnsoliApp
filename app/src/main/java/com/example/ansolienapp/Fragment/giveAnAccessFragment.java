package com.example.ansolienapp.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class giveAnAccessFragment extends Fragment {
    EditText et_fullName, et_email;
    Button btn_add;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String email, userId, idRelative;
    RecyclerView rv_relative;
    List<Relative> relativeList;
    itemAdapter adapter;
    FirebaseUser user;
    String userName;

    public giveAnAccessFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_give_an_access, container, false);
        // Inflate the layout for this fragment
        et_email = view.findViewById(R.id.et_email);
        et_fullName = view.findViewById(R.id.et_fullName);
        btn_add = view.findViewById(R.id.btn_add);
        rv_relative = view.findViewById(R.id.rv_relative);
        relativeList = new ArrayList<>();
        ViewData();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        ViewDatainformatio();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("يحب ادخال البريد الالكتروني");
                    return;
                }
                if (TextUtils.isEmpty(et_fullName.getText().toString())) {
                    et_fullName.setError("يحب ادخال اسم المساعد");
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
        db.collection("My Relative").whereEqualTo("userId", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.getId();
                            String email = doc.get("emailRelative").toString();
                            String name = doc.get("nameRelative").toString();
                            relativeList.add(new Relative(email, name,"s"));
                        }
                        adapter = new itemAdapter(getContext(), relativeList);
                        rv_relative.setHasFixedSize(true);
                        rv_relative.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_relative.setAdapter(adapter);
                    }
                });

    }

    private void ViewDatainformatio() {
        db.collection("As Patient").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            userName = task.getResult().get("fullName").toString();
                        }
                    }
                });
    }

    private void GiveAceses() {
        relativeList.clear();
        relativeList = new ArrayList<>();
        email = et_email.getText().toString();
        String emaill = auth.getCurrentUser().getEmail();
        db.collection("As Relative").whereEqualTo("email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Toast.makeText(getContext(), "الرجاء التحقق من الايميل المدخل", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.getId();

                            Map<String, Object> myRelative = new HashMap<>();
                            myRelative.put("nameRelative", et_fullName.getText().toString());
                            myRelative.put("emailRelative", et_email.getText().toString());
                            myRelative.put("userId", userId);
                            myRelative.put("idRelative", idRelative);
                            myRelative.put("email", emaill);
                            myRelative.put("isAccess", "false");
                            myRelative.put("userName", userName);

                            db.collection("My Relative").document(idRelative + userId)
                                    .set(myRelative)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                et_email.setText("");
                                                et_fullName.setText("");
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                    }
                });


    }
}