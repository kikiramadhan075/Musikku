package com.erly.musikku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPegawai extends RecyclerView.Adapter<AdapterPegawai.MyViewHolder> {
    private List<ModelPegawai> mList;
    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterPegawai(List<ModelPegawai> mList, Activity activity){
        this.mList = mList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.item_pegawai, parent, false);
        return new AdapterPegawai.MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelPegawai data = mList.get(position);
        Picasso.get().load(data.getImage()).into(holder.Gambar);
        holder.NoId.setText(data.getNoid());
        holder.Nama.setText(data.getNama());
        holder.Tanggal.setText(data.getTanggal());
        holder.Jk.setText(data.getJk());
        holder.Alamat.setText(data.getAlamat());
        holder.Keadaan.setText(data.getKeadaan());
        holder.Goldar.setText(data.getGoldar());
        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int iya) {
                        database.child("Pegawai").child(data.getKey()).removeValue().addOnFailureListener(new OnFailureListener() {
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] action = {"Update"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString("dataImage",mList.get(position).getImage());
                        bundle.putString("dataNoid",mList.get(position).getNoid());
                        bundle.putString("dataNama",mList.get(position).getNama());
                        bundle.putString("dataTanggal",mList.get(position).getTanggal());
                        bundle.putString("dataJk",mList.get(position).getJk());
                        bundle.putString("dataAlamat",mList.get(position).getAlamat());
                        bundle.putString("dataGoldar",mList.get(position).getGoldar());
                        bundle.putString("dataKey",mList.get(position).getKey());
                        bundle.putString("dataKeadaan",mList.get(position).getKeadaan());
                        Intent intent = new Intent(v.getContext(), UpdatePegawai.class);
                        intent.putExtras(bundle);
                        activity.startActivity(intent);

                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Gambar;
        TextView NoId;
        TextView Nama;
        TextView Tanggal;
        TextView Jk;
        TextView Alamat;
        TextView Keadaan;
        TextView Goldar;
        CardView card_hasil;
        ImageView btn_hapus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Gambar = itemView.findViewById(R.id.show_gambar);
            NoId = itemView.findViewById(R.id.show_noid);
            Nama = itemView.findViewById(R.id.show_nama);
            Tanggal = itemView.findViewById(R.id.show_tanggal);
            Jk = itemView.findViewById(R.id.show_agama);
            Alamat = itemView.findViewById(R.id.show_alamat);
            Goldar = itemView.findViewById(R.id.show_goldar);
            Keadaan = itemView.findViewById(R.id.show_keadaan);
            card_hasil = itemView.findViewById(R.id.card_hasil);
            btn_hapus = itemView.findViewById(R.id.hapus);
        }
    }
}
