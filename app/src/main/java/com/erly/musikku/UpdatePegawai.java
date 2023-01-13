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
import android.widget.ArrayAdapter;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class UpdatePegawai extends AppCompatActivity {
    private TextView tanggal;
    EditText NoID;
    EditText Nama, Alamat;
    Spinner Jk;
    RadioButton radioButton;
    CheckBox wni,wna;
    RadioGroup Goldar;
    Button Update, inputTanggal;
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
        setContentView(R.layout.update_pegawai);
        //        Get ID
        progressBar = findViewById(R.id.progres_update);
        progressBar.setVisibility(View.GONE);
        NoID = findViewById(R.id.update_noid);
        Nama = findViewById(R.id.update_nama);
        Alamat = findViewById(R.id.update_alamat);
        Jk = findViewById(R.id.update_agama);
        Goldar = findViewById(R.id.update_goldar);
        Update = findViewById(R.id.button_update);
        Gambar = findViewById(R.id.update_gambar);
        tanggal = findViewById(R.id.output_tanggal);
        wni = findViewById(R.id.update_wni);
        wna = findViewById(R.id.update_wna);

        // pilih Tanggal
        tanggal.setOnClickListener(v -> {
            datePicker();
        });
        pilihTanggal = ((view, tahun, bulan, hari) -> {
            bulan = bulan + 1;
            Log.d(TAG, "onSetDate: "+hari+" / "+bulan+" / "+tahun);

            String tgl = hari+" / "+bulan+" / "+tahun;
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
//        Input Gambar
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoUrl = result.getData().getData();
                    Gambar.setImageURI(fotoUrl);
                    Log.e("Data Foto : ", String.valueOf(fotoUrl));

                    Update.setOnClickListener(new View.OnClickListener() {
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
                                            getNoID = NoID.getText().toString();
                                            getNama = Nama.getText().toString();
                                            getTanggal = tanggal.getText().toString();
                                            getJk = Jk.getSelectedItem().toString();
                                            getAlamat = Alamat.getText().toString();
                                            getKeadaan = cbox;
                                            int selectedRating = Goldar.getCheckedRadioButtonId();
                                            radioButton = Goldar.findViewById(selectedRating);
                                            getGoldar = (String) radioButton.getText();

                                            if (checkInput() == true){
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                                            }else{

//                                             Proses Update
                                                ModelPegawai datapegawai = new ModelPegawai();
                                                datapegawai.setImage(getGambar);
                                                datapegawai.setNama(getNoID);
                                                datapegawai.setNama(getNama);
                                                datapegawai.setKeadaan(getKeadaan);
                                                datapegawai.setTanggal(getTanggal);
                                                datapegawai.setJk(getJk);
                                                datapegawai.setAlamat(getAlamat);
                                                datapegawai.setGoldar(getGoldar);
                                                updateDataPegawai(datapegawai);
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

        //        Tanpa Input Gambar
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getNoID = NoID.getText().toString();
                getNama = Nama.getText().toString();
                getTanggal = tanggal.getText().toString();
                getJk = Jk.getSelectedItem().toString();
                getKeadaan = cbox;
                getAlamat = Alamat.getText().toString();
                int selectedRating = Goldar.getCheckedRadioButtonId();
                radioButton = Goldar.findViewById(selectedRating);
                getGoldar = (String) radioButton.getText();
                if (checkInput() == true){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Data tidak boleh Kosong!", Toast.LENGTH_SHORT).show();
                }else{
//              Proses Update
                    ModelPegawai datapegawai = new ModelPegawai();
                    datapegawai.setImage(getGambar);
                    datapegawai.setNama(getNoID);
                    datapegawai.setNama(getNama);
                    datapegawai.setTanggal(getTanggal);
                    datapegawai.setJk(getJk);
                    datapegawai.setKeadaan(getKeadaan);
                    datapegawai.setAlamat(getAlamat);
                    datapegawai.setGoldar(getGoldar);
                    final String getImage = getIntent().getExtras().getString("dataImage");
                    datapegawai.setImage(getImage);
                    updateDataPegawai(datapegawai);}
            }
        });
    }

    private void updateDataPegawai(ModelPegawai datapegawai) {
        String getKey = getIntent().getExtras().getString("dataKey");
        database.child("Pegawai")
                .child(getKey)
                .setValue(datapegawai)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Gambar.setImageResource(R.mipmap.ic_launcher);
                        NoID.setText("");
                        Nama.setText("");
                        Alamat.setText("");
                        tanggal.setText("");
                        Toast.makeText(UpdatePegawai.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        finish();
                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private boolean checkInput() {
        boolean emtpyInput = false;
        if (isEmpty(getNoID)||isEmpty(getNama)||isEmpty(getTanggal)||isEmpty(getJk)
                ||isEmpty(getAlamat)
                ||isEmpty(getGoldar)){
            emtpyInput = true;
        }
        return emtpyInput;
    }



    private void getData() {
        final String getImage = getIntent().getExtras().getString("dataImage");
        final String getResolusi = getIntent().getExtras().getString("dataKeadaan");
        final String getJudul = getIntent().getExtras().getString("dataNama");
        final String getNoID = getIntent().getExtras().getString("dataNoID");
        final String getTanggal = getIntent().getExtras().getString("dataTanggal");
        final String getDurasi = getIntent().getExtras().getString("dataAlamat");
        final String getGenre = getIntent().getExtras().getString("dataAgama");
        final String getRating = getIntent().getExtras().getString("dataGoldar");

        Picasso.get().load(getImage).into(Gambar);
        NoID.setText(getNoID);
        Nama.setText(getJudul);
        Jk.setSelection(((ArrayAdapter<String>) Jk.getAdapter()).getPosition(getGenre));
        Alamat.setText(getDurasi);
        if (getRating.equalsIgnoreCase("A")) {
            Goldar.check(R.id.opsi_a);
        } else if (getRating.equalsIgnoreCase("AB")) {
            Goldar.check(R.id.opsi_b);
        } else if (getRating.equalsIgnoreCase("O")) {
            Goldar.check(R.id.opsi_c);
        } else if (getRating.equalsIgnoreCase("B")) {
            Goldar.check(R.id.opsi_d);}



    }

    private void datePicker(){
        Calendar kal = Calendar.getInstance();
        int tahun = kal.get(Calendar.YEAR);
        int bulan = kal.get(Calendar.MONTH);
        int hari = kal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(UpdatePegawai.this,pilihTanggal, tahun, bulan, hari);
        dialog.getWindow();
        dialog.show();

    }
    private Context getContext() {

        return null;
    }

}