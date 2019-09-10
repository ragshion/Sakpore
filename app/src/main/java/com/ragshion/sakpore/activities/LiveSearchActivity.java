package com.ragshion.sakpore.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ragshion.sakpore.R;
import com.ragshion.sakpore.adapter.SearchAdapter;
import com.ragshion.sakpore.api.Client;
import com.ragshion.sakpore.api.Service;
import com.ragshion.sakpore.objek.Barang;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class LiveSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Service api;

    private List<Barang> barang_list;
    private SearchAdapter barang_adapter;

    GifImageView loading;

    Call<List<Barang>> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        loading.bringToFront();

        recyclerView = findViewById(R.id.recyclerview_livesearch);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void fetchContact(String key){
        loading.setVisibility(View.VISIBLE);

        api = Client.getClient();

        call = api.search_barang(key, getIntent().getStringExtra("kategori"));
        call.enqueue(new Callback<List<Barang>>() {
            @Override
            public void onResponse(Call<List<Barang>> call, Response<List<Barang>> response) {
                loading.setVisibility(View.GONE);
                barang_list = response.body();
                barang_adapter = new SearchAdapter(barang_list, LiveSearchActivity.this);
                recyclerView.setAdapter(barang_adapter);
                barang_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Barang>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(LiveSearchActivity.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.slideDown(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_atas, menu);
        MenuItem cari = menu.findItem(R.id.btnSearch);
        cari.setVisible(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        search.expandActionView();
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                View view = LiveSearchActivity.this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(LiveSearchActivity.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                Bungee.slideDown(LiveSearchActivity.this);
                return true;
            }
        });

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchContact(newText);
                return false;
            }
        });

        return true;
    }

}
