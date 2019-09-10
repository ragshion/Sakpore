package com.ragshion.sakpore.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dd.processbutton.FlatButton;
import com.ragshion.sakpore.R;
import com.ragshion.sakpore.api.Client;
import com.ragshion.sakpore.api.Service;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.net.URLEncoder;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class DetailActivity  extends AppCompatActivity {
    TextView dt_nama, dt_kategori, dt_harga, tv_berhasil;
    GifImageView loading;
    ImageView iv_foto;
    Toolbar toolbar;

    MaterialEditText nama, email, alamat, no_hp;

    FlatButton btnBeli, btnWA;

    MaterialDialog materialDialog;

    LinearLayout dt_pembeli;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Bungee.fade(this);
        return  true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Bungee.fade(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("nama"));

        dt_nama = findViewById(R.id.dt_nama);
        dt_kategori = findViewById(R.id.dt_kategori);
        dt_harga = findViewById(R.id.dt_harga);
        iv_foto = findViewById(R.id.iv_foto);

        nama = findViewById(R.id.et_nama);
        email = findViewById(R.id.et_email);
        alamat = findViewById(R.id.et_alamat);
        no_hp = findViewById(R.id.et_no_hp);

        dt_pembeli = findViewById(R.id.dt_pembeli);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);

        tv_berhasil = findViewById(R.id.tv_berhasil);
        tv_berhasil.setVisibility(View.GONE);

        btnBeli = findViewById(R.id.btnBeli);
        btnWA = findViewById(R.id.btnWA);

        btnWA.setVisibility(View.GONE);

        dt_nama.setText(getIntent().getStringExtra("nama"));
        dt_kategori.setText(getIntent().getStringExtra("kategori"));
        dt_harga.setText(getIntent().getStringExtra("harga"));

        Glide
                .with(this)
                .load(getIntent().getStringExtra("url"))
                .into(iv_foto);

        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBeli.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                simpan_beli();
            }
        });

        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    PackageManager packageManager = DetailActivity.this.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=6281225566441&text=" + URLEncoder.encode("Halo", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }else {
                        Toasty.error(DetailActivity.this, "a").show();
                    }
                } catch(Exception e) {
                    Toasty.error(DetailActivity.this, e.getMessage()).show();
                }
            }
        });


    }

    void simpan_beli(){
        if (nama.getText().toString().equals("") |
                alamat.getText().toString().equals("") |
                no_hp.getText().toString().equals("") |
                email.getText().toString().equals("")){
            materialDialog = new MaterialDialog.Builder(this)
                    .title("Peringatan!")
                    .content("Harap isi semua field terlebih dahulu, Terima Kasih.")
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            materialDialog.dismiss();
                            loading.setVisibility(View.GONE);
                            btnBeli.setVisibility(View.VISIBLE);
                        }
                    })
                    .show();
        }else{
            Service service = Client.getClient();
            Call<ResponseBody> simpan_beli = service.simpan_beli(
                    nama.getText().toString(),
                    alamat.getText().toString(),
                    no_hp.getText().toString(),
                    email.getText().toString(),
                    getIntent().getStringExtra("id"),
                    "1");
            simpan_beli.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        Toasty.success(DetailActivity.this, jsonRESULTS.getString("respon")).show();
                        tv_berhasil.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        btnWA.setVisibility(View.VISIBLE);
                        dt_pembeli.setVisibility(View.GONE);
                    }catch (Exception e){
                        Toasty.error(DetailActivity.this, e.getMessage()).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toasty.error(DetailActivity.this, t.getMessage()).show();
                }
            });
        }
    }
}
