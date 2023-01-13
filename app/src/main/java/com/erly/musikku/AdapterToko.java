package com.erly.musikku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterToko extends RecyclerView.Adapter<AdapterToko.MyViewHolder> {
    private List<ModelToko> mList;
    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterToko(List<ModelToko>mList, Activity activity){
        this.mList = mList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.item_toko, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelToko data = mList.get(position);
        holder.tv_id.setText("NoId : " + data.getNoId());
        holder.tv_nama.setText("Nama : " + data.getNama());
        holder.tv_alamat.setText("Alamat : " + data.getAlamat());
        holder.tv_pemilik.setText("Pemilik : " + data.getPemilik());
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int iya) {
                        database.child("Toko").child(data.getKey()).removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Data gagal dihapus", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("Apakah yakin mau menghapus? " + data.getNama());
                builder.show();
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            FragmentManager manager = ((AppCompatActivity)activity).getSupportFragmentManager();
            DialogToko dialog = new DialogToko(
                    data.getNoId(),
                    data.getNama(),
                    data.getAlamat(),
                    data.getPemilik(),
                    data.getKey(),
                    "Ubah"
            );
            dialog.show(manager, "form");
            return true;
        });



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id, tv_nama, tv_alamat, tv_pemilik;
        ImageView btn_hapus;
        CardView card_hasil;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_alamat = itemView.findViewById(R.id.tv_alamat);
            tv_pemilik = itemView.findViewById(R.id.tv_pemilik);
            card_hasil = itemView.findViewById(R.id.card_hasil);
            btn_hapus = itemView.findViewById(R.id.hapus);
        }
    }
}
