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

import java.util.HashMap;
import java.util.Map;


public class myDoctorFragment extends Fragment {
    EditText et_number, et_email, et_nameDoctor;
    Button btn_add;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String email, userId, idDoctor, userName, mailUser;

    public myDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_doctor, container, false);
        et_email = view.findViewById(R.id.et_email);
        et_number = view.findViewById(R.id.et_number);
        et_nameDoctor = view.findViewById(R.id.et_fullName);
        btn_add = view.findViewById(R.id.btn_add);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();
        mailUser = auth.getCurrentUser().getEmail();
        ViewDatainformatio();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpladeDoctor();
            }
        });
        return view;
    }

    private void UpladeDoctor() {
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("يحب ادخال البريد الالكتروني");
            return;
        }
        if (TextUtils.isEmpty(et_nameDoctor.getText().toString())) {
            et_nameDoctor.setError("يحب ادخال اسم الطبيب");
            return;
        }
        if (TextUtils.isEmpty(et_number.getText().toString())) {
            et_number.setError("يحب ادخال رقم الطبيب ");
            return;
        }

        email = et_email.getText().toString();

        db.collection("As Doctor").whereEqualTo("email", email)
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
                            idDoctor = doc.getId();
                            Map<String, Object> myDoctor = new HashMap<>();
                            myDoctor.put("nameDoctor", et_nameDoctor.getText().toString());
                            myDoctor.put("emailDoctor", et_email.getText().toString());
                            myDoctor.put("phoneDoctor", et_number.getText().toString());
                            myDoctor.put("userId", userId);
                            myDoctor.put("idDoctor", idDoctor);
                            myDoctor.put("email", mailUser);
                            myDoctor.put("userName", userName);
                            db.collection("My Doctor").document(userId).set(myDoctor)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "تم اضافة الطبيب بنجاح", Toast.LENGTH_SHORT).show();
                                                et_email.setText("");
                                                et_nameDoctor.setText("");
                                                et_number.setText("");
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
}