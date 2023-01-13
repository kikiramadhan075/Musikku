package com.erly.musikku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ListPegawai extends AppCompatActivity {
    AdapterPegawai adapterPegawai;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelPegawai> listPegawai;
    RecyclerView tv_tampil;
    CardView card_hasil;

    //    Search
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pegawai);

        card_hasil = findViewById(R.id.card_hasil);
        tv_tampil = findViewById(R.id.tv_tampil);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        tv_tampil.setLayoutManager(mLayout);
        tv_tampil.setItemAnimator(new DefaultItemAnimator());
        searchView = findViewById(R.id.etSearch);

        tampilData();

        if (searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (TextUtils.isEmpty(newText)) {
                        tampilData();
                    } else {
                        tampilData(newText);
                    }
                    return true;
                }
            });
        }

    }

    private void tampilData(String newText) {
        database.child("Pegawai").orderByChild("nama").startAt(newText.toUpperCase(Locale.ROOT)).
                endAt(newText.toLowerCase(Locale.ROOT) + "\uf8ff").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listPegawai = new ArrayList<>();
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot Item : snapshot.getChildren()) {
                                ModelPegawai pegawai = Item.getValue(ModelPegawai.class);
                                pegawai.setKey(Item.getKey());
                                listPegawai.add(pegawai);
                            }
                        }
                        adapterPegawai = new AdapterPegawai(listPegawai,ListPegawai.this);
                        tv_tampil.setAdapter(adapterPegawai);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void tampilData() {
        database.child("Pegawai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPegawai = new ArrayList<>();
                for (DataSnapshot Item : snapshot.getChildren()){
                    ModelPegawai pegawai = Item.getValue(ModelPegawai.class);
                    pegawai.setKey(Item.getKey());
                    listPegawai.add(pegawai);
                }
                adapterPegawai = new AdapterPegawai(listPegawai,ListPegawai.this);
                tv_tampil.setAdapter(adapterPegawai);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}