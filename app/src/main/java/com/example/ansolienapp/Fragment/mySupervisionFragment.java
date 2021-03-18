package com.example.ansolienapp.Fragment;

import android.graphics.Color;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


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

    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
            "Oct", "Nov", "Dec"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};
    List yAxisValues = new ArrayList();
    List axisValues = new ArrayList();

    public mySupervisionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_supervision, container, false);


        LineChartView lineChartView = view.findViewById(R.id.chart);
        Line line = new Line(yAxisValues);

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        Axis axis = new Axis();
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);
        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);
        line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

        et_email = view.findViewById(R.id.et_email);
        btn_add = view.findViewById(R.id.btn_add);
        rv_relative = view.findViewById(R.id.rv_relative);
        relativeList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = auth.getUid();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_email.getText().toString())) {
                    et_email.setError("يحب ادخال البريد الالكتروني");
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
                            Toast.makeText(getContext(), "حدث خطا ما ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (DocumentSnapshot doc : value) {
                            idRelative = doc.getId();
                            String isAc = doc.get("isAccess").toString();
                            Toast.makeText(getContext(), "" + isAc, Toast.LENGTH_SHORT).show();
                            String email = doc.get("email").toString();
                            String userName = doc.get("userName").toString();
                            if (isAc.equals("true"))
                                relativeList.add(new Relative(email, userName));

                        }
                        adapter = new itemAdapter(getContext(), relativeList);
                        rv_relative.setHasFixedSize(true);
                        rv_relative.setLayoutManager(new LinearLayoutManager(getContext()));
                        rv_relative.setAdapter(adapter);
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
                            Toast.makeText(getContext(), "الرجاء التحقق من الايميل المدخل", Toast.LENGTH_SHORT).show();
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

                                                Toast.makeText(getContext(), "تم اضافة المشرف بنجاح", Toast.LENGTH_SHORT).show();
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