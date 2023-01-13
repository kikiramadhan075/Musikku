package com.erly.musikku;

import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InputAlat extends AppCompatActivity {

    EditText NoId, Musik;
    ProgressBar progressBar;
    Button Simpan, btnTest;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_alat);
        progressBar = findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        NoId = findViewById(R.id.idalat);
        Musik = findViewById(R.id.nama_musik);
        Simpan = findViewById(R.id.btnSimpanAlat);
        btnTest = findViewById(R.id.btnTest);

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getNoId = NoId.getText().toString();
                String getMusik = Musik.getText().toString();

                if(getNoId.isEmpty()){
                    NoId.setError("Masukkan No. ID Musik");
                }else if(getMusik.isEmpty()){
                    Musik.setError("Masukkan nama musik");
                }else{
                    database.child("Musik").push().setValue(new ModelMusik(getNoId, getMusik)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(InputAlat.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InputAlat.this, ListMusik.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InputAlat.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ListMusik.class));
            }
        });
    }
}