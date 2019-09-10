package com.ragshion.sakpore.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Barang {

    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("link_image")
    @Expose
    private String linkImage;
    @SerializedName("category")
    @Expose
    private String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Barang(String id){
        this.id =id;
    }

}
