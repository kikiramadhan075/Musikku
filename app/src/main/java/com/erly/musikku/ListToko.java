package com.erly.musikku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListToko extends AppCompatActivity {
    AdapterToko adapterToko;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelToko> listToko;
    RecyclerView tv_tampil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_toko);
        tv_tampil = findViewById(R.id.tv_tampil);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        tv_tampil.setLayoutManager(mLayout);
        tv_tampil.setItemAnimator(new DefaultItemAnimator());

        tampilData();
    }

    private void tampilData() {
        database.child("Toko").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listToko = new ArrayList<>();
                for (DataSnapshot Item : snapshot.getChildren()){
                    ModelToko toko = Item.getValue(ModelToko.class);
                    toko.setKey(Item.getKey());
                    listToko.add(toko);
                }
                adapterToko = new AdapterToko(listToko,ListToko.this);
                tv_tampil.setAdapter(adapterToko);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}