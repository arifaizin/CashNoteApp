package com.imastudio.cashnoteapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.imastudio.cashnoteapp.model.DataItem;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {
    public static final String DATA_KETERANGAN = "ketarangan";
    public static final String DATA_PENGELUARAN = "pengeluaran";
    public static final String DATA_TANGGAL = "tanggal";
    public static final String DATA_IDE = "id";

    Context context;
    List<DataItem> data = new ArrayList<>();

    //constructor->
    //klik kanan > generate > constructor

    public NoteAdapter(Context context, List<DataItem> data) {
        this.context = context;
        this.data = data;
    }

    //sambungkan ke layout item
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    //alt+ctrl +v

    //set data
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int posisi) {
        final String keterangan = data.get(posisi).getKeterangan();
        myViewHolder.tvKeterangan.setText(keterangan);
        final String pengeluaran = data.get(posisi).getPengeluaran();
        myViewHolder.tvPengeluaran.setText("Rp "+ pengeluaran);
        final String tanggal = data.get(posisi).getTanggal();
        myViewHolder.tvtanggal.setText(tanggal);
        Glide.with(context).load("https://cdn.pixabay.com/photo/2017/10/10/00/15/flat-2835466_960_720.png").into(myViewHolder.ivGambar);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(context, UpdateActivity.class);
                //kirim data
                pindah.putExtra(DATA_IDE, data.get(posisi).getId());
                pindah.putExtra(DATA_KETERANGAN, keterangan);
                pindah.putExtra(DATA_PENGELUARAN, pengeluaran);
                pindah.putExtra(DATA_TANGGAL, tanggal);
                context.startActivity(pindah);
            }
        });
    }

    //hitung jml data
    @Override
    public int getItemCount() {
        return data.size();
    }


    //kenalin komponenn dalam item
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGambar;
        TextView tvKeterangan, tvPengeluaran, tvtanggal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGambar = itemView.findViewById(R.id.ivItemGambar);
            tvKeterangan = itemView.findViewById(R.id.tvItemKeterangan);
            tvPengeluaran = itemView.findViewById(R.id.tvItemPengeluaran);
            tvtanggal = itemView.findViewById(R.id.tvItemTanggal);
        }
    }

    //extend class ke RecyelerView.Adapter
}
