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

public class ListMusik extends AppCompatActivity {
    AdapterMusik adapterMusik;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelMusik> listMusik;
    RecyclerView tv_tampil;
    CardView card_hasil;

    //    Search
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_musik);

        tv_tampil = findViewById(R.id.tampil);
        card_hasil = findViewById(R.id.card_hasil);
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
        database.child("Musik").orderByChild("noId").startAt(newText.toUpperCase(Locale.ROOT)).
                endAt(newText.toLowerCase(Locale.ROOT) + "\uf8ff").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listMusik = new ArrayList<>();
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot Item : snapshot.getChildren()) {
                                ModelMusik musik = Item.getValue(ModelMusik.class);
                                musik.setKey(Item.getKey());
                                listMusik.add(musik);
                            }
                        }
                        adapterMusik = new AdapterMusik(listMusik,ListMusik.this);
                        tv_tampil.setAdapter(adapterMusik);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void tampilData() {
        database.child("Musik").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMusik = new ArrayList<>();
                for (DataSnapshot Item : snapshot.getChildren()){
                    ModelMusik musik = Item.getValue(ModelMusik.class);
                    musik.setKey(Item.getKey());
                    listMusik.add(musik);
                }
                adapterMusik = new AdapterMusik(listMusik,ListMusik.this);
                tv_tampil.setAdapter(adapterMusik);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}