package com.example.ansolienapp.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ansolienapp.Model.Patient;
import com.example.ansolienapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class editProfileFragment extends Fragment {
    public static final int IMAGE_REQUEST = 1;
    String type, image, fullName, phone, password, year, weight,
            typePatient, patientEmail, clinicPhone, userId;
    EditText et_fullName, et_password, et_age, et_weight, et_type, et_phone, et_joker;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Patient patient;
    ImageView image_profile;
    TextView tv_typePantint, tv_weight;
    Button btn_save;
    FirebaseUser user;
    String muri = " ";
    private Uri imageUri;
    private StorageTask uplodeTask;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;

    public editProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            type = getArguments().getString("type");
            image = getArguments().getString("image");
            fullName = getArguments().getString("fullName");
            password = getArguments().getString("password");
            year = getArguments().getString("year");
            weight = getArguments().getString("weight");
            typePatient = getArguments().getString("typePatient");
            phone = getArguments().getString("phone");
            patientEmail = getArguments().getString("patientEmail");
            clinicPhone = getArguments().getString("clinicPhone");
            showData(type);
            Toast.makeText(getContext(), type, Toast.LENGTH_SHORT).show();
        }
        if (!type.equals("As Patient")) {
            et_age.setVisibility(View.GONE);
            et_weight.setVisibility(View.GONE);
            et_type.setVisibility(View.GONE);
            tv_typePantint.setVisibility(View.GONE);
            tv_weight.setVisibility(View.GONE);
            et_joker.setVisibility(View.VISIBLE);
            if (type.equals("As Relative")) {
                et_joker.setHint("email Patient");
            } else if (type.equals("As Doctor")) {
                et_joker.setHint("number Clinic");
            }
        }
    }

    private void showData(String type) {
        if (image.equals("default")) {
            Glide.with(getContext()).load(R.drawable.ic_baseline_person_pin_24).into(image_profile);
        } else {
            Glide.with(getContext()).load(image).into(image_profile);
        }
        et_fullName.setText(fullName);
        et_phone.setText(phone);
        et_password.setText(password);
        switch (type) {
            case "As Patient":
                et_age.setText(year);
                et_type.setText(typePatient);
                break;
            case "As Relative":
                et_joker.setText(patientEmail);
                break;
            case "As Doctor":
                et_joker.setText(clinicPhone);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        et_fullName = view.findViewById(R.id.et_fullName);
        et_password = view.findViewById(R.id.et_password);
        et_age = view.findViewById(R.id.et_age);
        et_weight = view.findViewById(R.id.et_weight);
        et_type = view.findViewById(R.id.et_type);
        et_phone = view.findViewById(R.id.et_phone);
        et_joker = view.findViewById(R.id.et_joker);
        image_profile = view.findViewById(R.id.image_profile);
        tv_typePantint = view.findViewById(R.id.tv_typePantint);
        tv_weight = view.findViewById(R.id.tv_weight);
        btn_save = view.findViewById(R.id.btn_save);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        patient = new Patient();
        userId = auth.getUid();
        user = auth.getCurrentUser();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        return view;
    }

    private void UpdateData() {
        Map<String, Object> users = new HashMap<>();
        users.put("fullName", et_fullName.getText().toString());
        users.put("phone", et_phone.getText().toString());
        users.put("password", et_password.getText().toString());
        if (muri.equals(" ")) {
            users.put("image", "default");
        } else {
            users.put("image", muri);
        }
        switch (type) {
            case "As Patient":
                users.put("year", et_age.getText().toString());
                users.put("weight", et_weight.getText().toString());
                users.put("type", et_type.getText().toString());
                break;
            case "As Relative":
                users.put("patientEmail", et_joker.getText().toString());

                break;
            case "As Doctor":
                users.put("clinicPhone", et_joker.getText().toString());
                break;
        }
        user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "pass is update", Toast.LENGTH_SHORT).show();
            }
        });

        db.collection(type).document(userId).update(users).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                homeFragment homeFragment = new homeFragment();
                homeFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                        homeFragment).addToBackStack("").commit();
            }
        });

    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtionion(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void uplodeImage() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if (imageUri != null) {
            final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtionion(imageUri));

            uplodeTask = storageReference.putFile(imageUri);
            uplodeTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();


                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        muri = uri.toString();
                        Glide.with(getContext()).load(muri).into(image_profile);


                    } else {
                        Toast.makeText(getContext(), "Fields", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Error", "" + e.getMessage());
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (uplodeTask != null && uplodeTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload Is Progress", Toast.LENGTH_SHORT).show();
            } else {
                uplodeImage();
            }
        }
    }
}