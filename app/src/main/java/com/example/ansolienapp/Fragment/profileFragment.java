package com.example.ansolienapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ansolienapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;


public class profileFragment extends Fragment {
    TextView tv_typePantint, tv_fullName, tv_phone, tv_email, tv_year, tv_weight;
    ImageView imageProfile;
    Button btn_editProfile;
    String type= "x"
            , userId;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String image, fullName, phone, email, year, weight, typePatient, password, patientEmail, clinicPhone;

    public profileFragment(String type) {
        this.type = type;
        Log.d("TAG", "profileFragment: "+ type);
    }
    @Override
    public void onStart() {
        super.onStart();
         SharedPreferences pref;
         SharedPreferences.Editor editor;
        pref = getContext().getSharedPreferences("MyFirstPref", MODE_PRIVATE);
        type = pref.getString("type", "t");
        Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        showData(type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btn_editProfile = view.findViewById(R.id.btn_editProfile);

        tv_typePantint = view.findViewById(R.id.tv_typePantint);
        tv_fullName = view.findViewById(R.id.tv_fullName);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        tv_year = view.findViewById(R.id.tv_year);
        tv_weight = view.findViewById(R.id.tv_weight);
        imageProfile = view.findViewById(R.id.imageProfile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = auth.getUid();
      //  Toast.makeText(getContext(), type + "\n"+userId, Toast.LENGTH_SHORT).show();
        btn_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("image", image);
                bundle.putString("fullName", fullName);
                bundle.putString("phone", phone);
                bundle.putString("patientEmail", patientEmail);
                bundle.putString("year", year);
                bundle.putString("weight", weight);
                bundle.putString("typePatient", typePatient);
                bundle.putString("password", password);
                bundle.putString("clinicPhone", clinicPhone);
                editProfileFragment editProfileFragment = new editProfileFragment();
                editProfileFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        editProfileFragment).addToBackStack("").commit();
            }
        });
        return view;
    }

    private void showData(String type) {
        db.collection(type).document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            image = task.getResult().get("image").toString();
                            fullName = task.getResult().get("fullName").toString();
                            phone = task.getResult().get("phone").toString();
                            email = task.getResult().get("email").toString();
                            password = task.getResult().get("password").toString();
                            switch (type) {
                                case "As Patient":
                                    year = task.getResult().get("year").toString();
                                    weight = task.getResult().get("weight").toString();
                                    typePatient = task.getResult().get("type").toString();
                                    tv_typePantint.setText(typePatient);
                                    tv_weight.setText(weight);
                                    tv_year.setText(year);
                                    break;
                                case "As Relative":
                                    patientEmail = task.getResult().get("patientEmail").toString();
                                    tv_weight.setVisibility(View.GONE);
                                    tv_typePantint.setVisibility(View.GONE);
                                    tv_year.setVisibility(View.GONE);
                                    break;
                                case "As Doctor":
                                    clinicPhone = task.getResult().get("clinicPhone").toString();
                                    tv_weight.setVisibility(View.GONE);
                                    tv_typePantint.setVisibility(View.GONE);
                                    tv_year.setText(clinicPhone);
                                    break;
                            }

                            tv_email.setText(email);
                            tv_phone.setText(phone);
                            tv_fullName.setText(fullName);
                            if (image.equals("default")) {
                                Glide.with(getContext()).load(R.drawable.ic_baseline_person_pin_24).into(imageProfile);
                            } else {
                                Glide.with(getContext()).load(image).into(imageProfile);
                            }
                        }
                    }
                });
    }
}