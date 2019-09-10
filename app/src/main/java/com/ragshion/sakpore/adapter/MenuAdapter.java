package com.ragshion.sakpore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ragshion.sakpore.R;
import com.ragshion.sakpore.activities.DataActivity;
import com.ragshion.sakpore.activities.DetailActivity;
import com.ragshion.sakpore.activities.TentangActivity;
import com.ragshion.sakpore.objek.MenuData;
import com.ragshion.sakpore.utilities.SharedPrefManager;

import java.net.URLEncoder;
import java.util.List;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private Context mContext;
    private List<MenuData> mFlowerList;

    public MenuAdapter(Context mContext, List<MenuData> mFlowerList) {
        this.mContext = mContext;
        this.mFlowerList = mFlowerList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_menu, parent, false);
        return new MenuViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder holder, int position) {
        holder.mImage.setImageResource(mFlowerList.get(position).getMenuImage());
        holder.mTitle.setText(mFlowerList.get(position).getMenuName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFlowerList.get(holder.getAdapterPosition()).getMenuName().equalsIgnoreCase("jejamu")){
                    Intent load = new Intent(mContext, DataActivity.class);
                    load.putExtra("judul", "Jejamu");
                    load.putExtra("kategori", "2");
                    mContext.startActivity(load);

                }else if (mFlowerList.get(holder.getAdapterPosition()).getMenuName().equalsIgnoreCase("kelontong")){
                    Intent load = new Intent(mContext, DataActivity.class);
                    load.putExtra("judul", "Kelontong");
                    load.putExtra("kategori", "3");
                    mContext.startActivity(load);
                }else if (mFlowerList.get(holder.getAdapterPosition()).getMenuName().equalsIgnoreCase("whatsapp")){
                    try{
                        PackageManager packageManager = mContext.getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String url = "https://api.whatsapp.com/send?phone=6281225566441&text=" + URLEncoder.encode("Halo", "UTF-8");
                        i.setPackage("com.whatsapp");
                        i.setData(Uri.parse(url));
                        if (i.resolveActivity(packageManager) != null) {
                            mContext.startActivity(i);
                        }else {
                            Toasty.error(mContext, "a").show();
                        }
                    } catch(Exception e) {
                        Toasty.error(mContext, e.getMessage()).show();
                    }

                }else if (mFlowerList.get(holder.getAdapterPosition()).getMenuName().equalsIgnoreCase("kuliner")) {
                    Intent load = new Intent(mContext, DataActivity.class);
                    load.putExtra("judul", "Kuliner");
                    load.putExtra("kategori", "1");
                    mContext.startActivity(load);
                }else if (mFlowerList.get(holder.getAdapterPosition()).getMenuName().equalsIgnoreCase("Tentang App")) {
                    Intent load = new Intent(mContext, TentangActivity.class);
                    mContext.startActivity(load);
                }
                Bungee.swipeLeft(mContext);


            }
        });
    }

    @Override
    public int getItemCount() {
        return mFlowerList.size();
    }
}

class MenuViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    CardView mCardView;

    MenuViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.cardview);
    }
}
