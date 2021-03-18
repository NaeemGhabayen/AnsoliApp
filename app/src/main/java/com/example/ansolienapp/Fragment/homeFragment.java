package com.example.ansolienapp.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


public class homeFragment extends Fragment {
    Button btn_giveAnAccess, btn_generateReport, btn_carb, btn_showProfile;
    String type = "x", userId;
    FirebaseAuth auth;
    FirebaseFirestore db;
    ImageView image_profile;
    TextView tv_fullName;
    private SharedPreferences pref;

    @Override
    public void onStart() {
        super.onStart();
        pref = getContext().getSharedPreferences("MyFirstPref", MODE_PRIVATE);
        type = pref.getString("type", "t");
        userId = pref.getString("id", "");
        showData(type);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn_showProfile = view.findViewById(R.id.btn_showProfile);
        btn_giveAnAccess = view.findViewById(R.id.btn_giveAnAccess);
        btn_generateReport = view.findViewById(R.id.btn_generateReport);
        btn_carb = view.findViewById(R.id.btn_carb);
        image_profile = view.findViewById(R.id.image_profile);
        tv_fullName = view.findViewById(R.id.tv_fullName);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        btn_giveAnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveAnAccessFragment accessFragment = new giveAnAccessFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        accessFragment).addToBackStack("").commit();

            }
        });
        btn_generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReportFragment generateReportFragment = new generateReportFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        generateReportFragment).addToBackStack("").commit();

            }
        });
        btn_carb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carbohydratesFragment carbohydratesFragment = new carbohydratesFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        carbohydratesFragment).addToBackStack("").commit();

            }
        });
        btn_showProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment profileFragment = new profileFragment(type);
                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        profileFragment).addToBackStack("").commit();
            }
        });
        return view;
    }

    private void showData(String type) {

        // Toast.makeText(getContext(), "ss"+type + "\n"+userId, Toast.LENGTH_SHORT).show();
        db.collection(type).document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String image = task.getResult().get("image").toString();
                            String name = task.getResult().get("fullName").toString();
                            tv_fullName.setText(name);
                            if (image.equals("default")) {
                                Glide.with(getContext()).load(R.drawable.ic_baseline_person_pin_24).into(image_profile);
                            } else {
                                Glide.with(getContext()).load(image).into(image_profile);
                            }
                        }
                    }
                });
    }
}