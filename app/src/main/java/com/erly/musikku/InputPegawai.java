package com.erly.musikku;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class InputPegawai extends AppCompatActivity {
    private TextView tanggal;
    CheckBox wni, wna;
    EditText NoId;
    EditText Nama, Alamat, NoHP, Email;
    Spinner jk;
    RadioButton radioButton;
    RadioGroup goldar;
    Button Simpan, btnLogout, inputTanggal;
    ImageView Gambar;
    ProgressBar progressBar;

    String getNoID, getNama, getTanggal, getAlamat, getNoHP, getEmail, getJk, getGoldar, getGambar, getKeadaan, cbox;

    //    Input Foto
    ActivityResultLauncher<Intent> activityResultLauncher;
    Uri fotoUrl;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    // tanggal
    private DatePickerDialog.OnDateSetListener pilihTanggal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_pegawai);
        progressBar = findViewById(R.id.progres_simpan);
        progressBar.setVisibility(View.GONE);
        NoId = findViewById(R.id.input_noid);
        Nama = findViewById(R.id.input_nama);
        Alamat = findViewById(R.id.input_alamat);
        jk = findViewById(R.id.input_agama);
        goldar = findViewById(R.id.opsi_goldar);
        Simpan = findViewById(R.id.button_simpan);
        Gambar = findViewById(R.id.input_foto);
        wni = findViewById(R.id.wni);
        wna = findViewById(R.id.wna);
        tanggal = findViewById(R.id.output_tanggal);

        // pilih Tanggal
        tanggal.setOnClickListener(v -> {
            datePicker();
        });
        pilihTanggal = ((view, tahun, bulan, hari) -> {
            bulan = bulan + 1;
            Log.d(TAG, "onSetDate: " + hari + " / " + bulan + " / " + tahun);

            String tgl = hari + " / " + bulan + " / " + tahun;
            tanggal.setText(tgl);
        });

        wni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "WNI";
            }
        });

        wna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbox = "WNA";
            }
        });

        //      Mendapatkan Data dari Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference getReference = database.getReference();

        //        Ketika Gambar Di Klik
        Gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        //      Get Foto
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoUrl = result.getData().getData();
                    Gambar.setImageURI(fotoUrl);
                    Log.e("Data Lokasi Foto : ", String.valueOf(fotoUrl));

                    Simpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressBar.setVisibility(View.VISIBLE);
                            StorageReference filePath = storage.getReference().child("ImagePost").child(fotoUrl.getLastPathSegment());
                            filePath.putFile(fotoUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> dowload = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            getGambar = task.getResult().toString();
                                            getNoID = NoId.getText().toString();
                                            getNama = Nama.getText().toString();
                                            getTanggal = tanggal.getText().toString();
                                            getJk = jk.getSelectedItem().toString();
                                            getAlamat = Alamat.getText().toString();
                                            getKeadaan = cbox;
                                            int selectedRating = goldar.getCheckedRadioButtonId();
                                            radioButton = goldar.findViewById(selectedRating);
                                            getGoldar = (String) radioButton.getText();

                                            if (checkInput() == true) {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                checkUser();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

        Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput() == true) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    checkUser();
                }
            }
        });


    }

    private void datePicker() {
        Calendar kal = Calendar.getInstance();
        int tahun = kal.get(Calendar.YEAR);
        int bulan = kal.get(Calendar.MONTH);
        int hari = kal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(InputPegawai.this, pilihTanggal, tahun, bulan, hari);
        dialog.getWindow();
        dialog.show();

    }

    private Context getContext() {

        return null;
    }


    //
    private boolean checkInput() {
        boolean emtpyInput = false;
        if (isEmpty(getGambar) || isEmpty(getNoID) || isEmpty(getNama) || isEmpty(getTanggal) || isEmpty(getJk)
                || isEmpty(getAlamat)
                || isEmpty(getGoldar)) {
            emtpyInput = true;
        }
        return emtpyInput;
    }

    private void checkUser() {
        if (isEmpty(getGambar) || isEmpty(getNoID) || isEmpty(getNama) || isEmpty(getTanggal) || isEmpty(getJk)
                || isEmpty(getAlamat)
                || isEmpty(getGoldar)) {
            Toast.makeText(getContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
        } else {
            database.child("Pegawai").push()
                    .setValue(new ModelPegawai(getNoID, getNama, getTanggal, getJk, getAlamat,
                            getGoldar, getKeadaan, getGambar)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(InputPegawai.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Gambar.setImageResource(R.mipmap.ic_launcher);
                    NoId.setText("");
                    Nama.setText("");
                    tanggal.setText("");
                    Alamat.setText("");
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}