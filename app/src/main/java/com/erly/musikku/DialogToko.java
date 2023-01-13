package com.erly.musikku;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogToko extends DialogFragment {
    private String NoId, Nama, Alamat, Pemilik, key, pilih;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogToko(String noId, String nama, String alamat, String pemilik, String key, String pilih) {
        NoId = noId;
        Nama = nama;
        Alamat = alamat;
        Pemilik = pemilik;
        this.key = key;
        this.pilih = pilih;
    }

    TextView tnoid, tnama, talamat, tpemilik;
    Button button_simpan;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_input_toko, container, false);
        progressBar = view.findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        tnoid = view.findViewById(R.id.input_noid);
        tnama = view.findViewById(R.id.input_nama);
        talamat = view.findViewById(R.id.input_alamat);
        tpemilik = view.findViewById(R.id.input_pemilik);
        button_simpan = view.findViewById(R.id.button_simpan);

        tnoid.setText(NoId);
        tnama.setText(Nama);
        talamat.setText(Alamat);
        tpemilik.setText(Pemilik);
        button_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NoId = tnoid.getText().toString();
                String aNama = tnama.getText().toString();
                String bAlamat = talamat.getText().toString();
                String aPemilik = tpemilik.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (pilih.equals("Ubah")){
                    database.child("Toko").child(key).setValue(new ModelToko(NoId, aNama, bAlamat, aPemilik)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(),"Berhasil diubah",Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(),"Maaf gagal diubah",Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
