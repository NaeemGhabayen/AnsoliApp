package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.Model.Doctor;
import com.example.ansolienapp.Model.Patient;
import com.example.ansolienapp.Model.Relative;
import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class signUpActivity extends AppCompatActivity {
    Intent intent;
    String type, userId, name, email, password, age, year, weight, typePantent, phone, joker;
    EditText et_fullName, et_email, et_password, et_age, et_year, et_weight, et_type, et_phone, et_joker;
    Button btn_next;
    Patient patient;
    FirebaseAuth auth;
    FirebaseFirestore db;
    DocumentReference reference;
    Doctor doctor;
    Relative relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btn_next = findViewById(R.id.btn_next);
        et_fullName = findViewById(R.id.et_fullName);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_age = findViewById(R.id.et_age);
        et_year = findViewById(R.id.et_year);
        et_weight = findViewById(R.id.et_weight);
        et_type = findViewById(R.id.et_type);
        et_phone = findViewById(R.id.et_phone);
        et_joker = findViewById(R.id.et_joker);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        patient = new Patient();
        intent = getIntent();
        type = intent.getStringExtra("type");
        Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
        if (type.equals("As Doctor")) {
            et_age.setVisibility(View.GONE);
            et_year.setVisibility(View.GONE);
            et_weight.setVisibility(View.GONE);
            et_type.setVisibility(View.GONE);
            et_joker.setVisibility(View.VISIBLE);
            et_joker.setHint("number Clinic");

        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validite()) {
                    auth.createUserWithEmailAndPassword(et_email.getText().toString()
                            , et_password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userId = auth.getUid();

                                        name = et_fullName.getText().toString();
                                        email = et_email.getText().toString();
                                        password = et_password.getText().toString();
                                        age = et_age.getText().toString();
                                        year = et_year.getText().toString();
                                        weight = et_weight.getText().toString();
                                        typePantent = et_type.getText().toString();
                                        phone = et_phone.getText().toString();
                                        //Toast.makeText(signUpActivity.this, "r"+type, Toast.LENGTH_SHORT).show();

                                        switch (type) {
                                            case "As Patient":
                                                patient = new Patient(name, email, password, age, year
                                                        , weight, typePantent, phone, "default");
                                                uploadeDataBase(patient);
                                                break;
                                            case "As Doctor":
                                                Toast.makeText(signUpActivity.this, "ttt", Toast.LENGTH_SHORT).show();
                                                doctor = new Doctor(name, email, password, phone,
                                                        et_joker.getText().toString(), "default");
                                                uploadeDataBase(doctor);
                                                break;
                                            case "As Relative":
                                                relative = new Relative(name, email, password, phone,
                                                        et_joker.getText().toString(), "default");
                                                uploadeDataBase(relative);

                                                break;
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(signUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private boolean CheckSign() {
        db.collection("As Patient").whereEqualTo("idRelative", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    }
                });
        return true;
    }


    private void uploadeDataBase(Object object) {
        reference = db.collection(type).document(userId);
        reference.set(object).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "تم تسجيل الاشتراك بنجاح", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), alertActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUpActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean Validite() {
        if (TextUtils.isEmpty(et_fullName.getText().toString())) {
            et_fullName.setError("يحب ادخال اسم المستخدم");
            return false;
        }
        if (TextUtils.isEmpty(et_email.getText().toString())) {
            et_email.setError("يجب ادخال البريد الالكتروني");
            return false;
        }
        if (TextUtils.isEmpty(et_password.getText().toString())) {
            et_password.setError("يجب ادخال كلمة المرور");
            return false;
        }
        if (TextUtils.isEmpty(et_phone.getText().toString())) {
            et_phone.setError("يجب رقم الهاتف");
            return false;
        }
        return true;
    }
}