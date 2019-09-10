package com.ragshion.sakpore.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.ragshion.sakpore.R;
import com.ragshion.sakpore.adapter.DataAdapter;
import com.ragshion.sakpore.api.Client;
import com.ragshion.sakpore.api.Service;
import com.ragshion.sakpore.objek.Barang;
import com.ragshion.sakpore.utilities.SharedPrefManager;
import com.ragshion.sakpore.utilities.VerticalLineDecorator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;


public class DataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Barang> movies;
    DataAdapter adapter;
    Service api;
    String TAG = "Data Activity - ";
    Context context;
    Toolbar dataztoolbar;
    ImageView ic_livesearch;
    GifImageView loading;

    SharedPrefManager sharedPrefManager;

    int requestSimpan = 0;
    String kategori;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        Bungee.fade(this);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.fade(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        context = this;
        recyclerView = findViewById(R.id.recyclerview_list_data);
        movies = new ArrayList<>();
        loading = findViewById(R.id.loading);

        sharedPrefManager = new SharedPrefManager(this);

        dataztoolbar = findViewById(R.id.dataztoolbar);
        setSupportActionBar(dataztoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String judul = getIntent().getStringExtra("judul");
        kategori = getIntent().getStringExtra("kategori");
        getSupportActionBar().setTitle(judul);

        ic_livesearch = findViewById(R.id.ic_livesearch);
        ic_livesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent livesearch = new Intent(DataActivity.this, LiveSearchActivity.class);
                livesearch.putExtra("kategori",kategori);
                startActivity(livesearch);
                Bungee.slideUp(DataActivity.this);
            }
        });

        adapter = new DataAdapter(this, movies);
        adapter.setLoadMoreListener(new DataAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);
        api = Client.getClient();
        load(0);
    }


    private void load(int index){
        Call<List<Barang>> call = api.getData(index,kategori);
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                loading.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    List<Barang> result = response.body();
                    if(result.size()<=0){
                        Toasty.error(DataActivity.this,"Maaf, Saat Ini Belum ada Data pada Kategori Tersebut");
                    }else{
                        movies.addAll(response.body());
                        adapter.notifyDataChanged();
                    }
                }else{
                    Log.e(TAG," Response Error "+ String.valueOf(response.code()));
                    Toasty.error(DataActivity.this,"Maaf, Tidak ada Data pada Kategori ini");
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toasty.error(DataActivity.this,"Maaf, Tidak ada Data pada Kategori ini");
            }
        });
    }

    private void loadMore(int index){
        movies.add(new Barang("load"));
        adapter.notifyItemInserted(movies.size()-1);

        Call<List<Barang>> call = api.getData(index,kategori);
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                if(response.isSuccessful()){

                    //remove loading view
                    movies.remove(movies.size()-1);

                    /*if(cek_load==1){
                        movies.remove(movies.size()-1);
                        cek_load = cek_load+1;
                    }*/


                    List<Barang> result = response.body();
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    if(result.size()>1){
                        //add loaded data

                        /*if(cek_load==1){
                            movies.remove(movies.size()-1);
                            cek_load = cek_load+1;
                        }*/
                        //movies.remove(movies.size()-1);
                        movies.remove(movies.size()-1);
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server

                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        //Toast.makeText(context,"Semua Data Sudah Dimuat",Toast.LENGTH_LONG).show();
                    }
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    Log.e(TAG," Load More Response Error "+ String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == requestSimpan){
            this.recreate();
        }
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}
