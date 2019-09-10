package com.ragshion.sakpore.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ragshion.sakpore.R;
import com.ragshion.sakpore.activities.DetailActivity;
import com.ragshion.sakpore.objek.Barang;
import com.ragshion.sakpore.utilities.SharedPrefManager;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;


public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Barang> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public static MaterialDialog materialDialog;
    public static SharedPrefManager sharedPrefManager;
//    SharedPrefManager sharedPrefManager;

    public DataAdapter(Context context, List<Barang> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new DatazHolder(inflater.inflate(R.layout.recyclerview_item_data,parent,false));

        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((DatazHolder)holder).bindData(movies.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(!movies.get(position).getId().equalsIgnoreCase("")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class DatazHolder extends RecyclerView.ViewHolder{
        TextView tv_nama_barang, tv_harga, tv_kategori;
        ImageView iv_foto;
        String id, url_foto;

        public DatazHolder(final View view){
            super(view);


            tv_nama_barang = view.findViewById(R.id.tv_nama_barang);
            tv_harga= view.findViewById(R.id.tv_harga);
            tv_kategori = view.findViewById(R.id.tv_kategori);
            iv_foto = view.findViewById(R.id.iv_foto);


            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra("nama",tv_nama_barang.getText());
                        intent.putExtra("harga",tv_harga.getText());
                        intent.putExtra("kategori",tv_kategori.getText());
                        intent.putExtra("url",url_foto);
                        intent.putExtra("id",id);
                        v.getContext().startActivity(intent);
                        Bungee.swipeLeft(v.getContext());
                    }
                }
            });

        }

        void bindData(Barang dataz){

            tv_nama_barang.setText(dataz.getNama());
            tv_harga.setText(dataz.getHarga());
            tv_kategori.setText("Kategori : "+dataz.getCategory());
            String url = itemView.getResources().getString(R.string.pathfoto)+dataz.getLinkImage();
            url_foto = url;
            Glide.with(itemView.getContext())
                    .load(url)
                    .apply(new RequestOptions().override(96, 64))
                    .into(iv_foto);
            id = dataz.getId();

        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}
