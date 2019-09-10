package com.ragshion.sakpore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ragshion.sakpore.R;
import com.ragshion.sakpore.activities.DetailActivity;
import com.ragshion.sakpore.objek.Barang;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<Barang> contacts;
    private Context context;

    public SearchAdapter(List<Barang> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        String alamat = contacts.get(position).getAlamat()+" Rt "+contacts.get(position).getRt()+" Rw "+contacts.get(position).getRw()
//                +" Desa "+contacts.get(position).getNama_desa().substring(0, 1).toUpperCase()+contacts.get(position).getNama_desa().substring(1).toLowerCase()
//                +" Kecamatan "+contacts.get(position).getNama_kec().substring(0, 1).toUpperCase()+contacts.get(position).getNama_kec().substring(1).toLowerCase();

        holder.id = contacts.get(position).getId();
        holder.tv_nama_barang.setText(contacts.get(position).getNama());
        holder.tv_harga.setText(contacts.get(position).getHarga());
        holder.tv_kategori.setText(contacts.get(position).getCategory());

        String url = holder.itemView.getResources().getString(R.string.pathfoto)+contacts.get(position).getLinkImage();
        Glide.with(holder.itemView.getContext())
                .load(url)
                .apply(new RequestOptions().override(96, 64))
                .into(holder.iv_foto);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_barang, tv_harga, tv_kategori;
        ImageView iv_foto;
        String id, url_foto;

        public MyViewHolder(View view) {
            super(view);

            tv_nama_barang = view.findViewById(R.id.tv_nama_barang);
            tv_harga= view.findViewById(R.id.tv_harga);
            tv_kategori = view.findViewById(R.id.tv_kategori);
            iv_foto = view.findViewById(R.id.iv_foto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("nama",tv_nama_barang.getText());
                    intent.putExtra("harga",tv_harga.getText());
                    intent.putExtra("kategori",tv_kategori.getText());
                    intent.putExtra("url",url_foto);
                    intent.putExtra("id",id);
                    v.getContext().startActivity(intent);
                    Bungee.swipeLeft(v.getContext());
                }
            });
        }
    }
}
