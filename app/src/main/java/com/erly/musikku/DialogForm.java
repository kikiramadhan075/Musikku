package com.erly.musikku;

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

public class DialogForm extends DialogFragment {
    String NoId, Musik, key, pilih;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm(String noId, String musik, String key, String pilih) {
        NoId = noId;
        Musik = musik;
        this.key = key;
        this.pilih = pilih;
    }

    TextView tnoid, tmusik;
    Button btnSimpanAlat;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_input_alat, container, false);
        progressBar = view.findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        tnoid = view.findViewById(R.id.idalat);
        tmusik = view.findViewById(R.id.nama_musik);
        btnSimpanAlat = view.findViewById(R.id.btnSimpanAlat);

        tnoid.setText(NoId);
        tmusik.setText(Musik);
        btnSimpanAlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NoId = tnoid.getText().toString();
                String Musik = tmusik.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (pilih.equals("Ubah")) {
                    database.child("Musik").child(key).setValue(new ModelMusik(NoId, Musik)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), "Berhasil diubah", Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), "Maaf gagal diubah", Toast.LENGTH_SHORT);
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
