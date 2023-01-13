package com.erly.musikku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputToko extends AppCompatActivity {
    EditText NoId;
    EditText Nama, Alamat, Pemilik;
    ProgressBar progressBar;
    Button Simpan;
    ImageView Gambar;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_toko);
        progressBar = findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        NoId = findViewById(R.id.input_noid);
        Nama = findViewById(R.id.input_nama);
        Alamat = findViewById(R.id.input_alamat);
        Pemilik = findViewById(R.id.input_pemilik);
        Simpan = findViewById(R.id.button_simpan);

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getNoId = NoId.getText().toString();
                String getNama = Nama.getText().toString();
                String getAlamat = Alamat.getText().toString();
                String getPemilik = Pemilik.getText().toString();

                if(getNoId.isEmpty()){
                    NoId.setError("Masukkan No. ID Toko");
                }else if(getNama.isEmpty()){
                    Nama.setError("Masukkan nama toko");
                }else{
                    database.child("Toko").push().setValue(new ModelToko(getNoId,getNama,getAlamat,getPemilik)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(InputToko.this, "Data berhasil disimpan",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InputToko.this, ListToko.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InputToko.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}