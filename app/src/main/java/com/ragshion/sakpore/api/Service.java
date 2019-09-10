package com.ragshion.sakpore.api;

import com.ragshion.sakpore.objek.Barang;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("api.php")
    Call<List<Barang>> getData(@Query("index") int index,
                               @Query("kategori") String kategori);

    @GET("beli.php")
    Call<ResponseBody> simpan_beli(
            @Query("nama") String nama,
            @Query("alamat") String alamat,
            @Query("no_hp") String no_hp,
            @Query("email") String email,
            @Query("id_product") String id_product,
            @Query("status") String status);

    @GET("search.php")
    Call<List<Barang>> search_barang(
            @Query("key") String key,
            @Query("kategori") String kategori);

}
