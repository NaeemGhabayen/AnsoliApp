package com.example.ansolienapp.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    Button login;
    Intent intent;
    String type;
    EditText et_email, et_password;
    FirebaseAuth auth;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.btn_next);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        auth = FirebaseAuth.getInstance();
        intent = getIntent();
        pref = getApplicationContext().getSharedPreferences("MyFirstPref", MODE_PRIVATE);
        editor = pref.edit();
        type = intent.getStringExtra("type");
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("يحب ادخال البريد الالكتروني");
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())) {
                    et_password.setError("يجب ادخال كلمة المرور");
                    return;
                }

                auth.signInWithEmailAndPassword(et_email.getText().toString()
                        , et_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (!type.equals("As Patient")) {
                                startActivity(new Intent(getApplicationContext(), supHomeActivity.class));

                            } else {
                                startActivity(new Intent(getApplicationContext(), homeActivity.class));
                            }
                            editor.putString("keeplogin", "true");
                            editor.putString("type", type);
                            editor.putString("id", auth.getUid());
                            editor.commit();
                            Toast.makeText(loginActivity.this,
                                    "تم تسجيل دخولك بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }


}