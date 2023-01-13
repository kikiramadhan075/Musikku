package com.erly.musikku;

public class ModelToko {
    private String NoId;
    private String Nama;
    private String Alamat;
    private String Pemilik;
    private String key;

    public  ModelToko(){

    }

    public ModelToko(String noId, String nama, String alamat, String pemilik) {
        NoId = noId;
        Nama = nama;
        Alamat = alamat;
        Pemilik = pemilik;
    }

    public String getNoId() {
        return NoId;
    }

    public void setNoId(String noId) {
        NoId = noId;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getPemilik() {
        return Pemilik;
    }

    public void setPemilik(String pemilik) {
        Pemilik = pemilik;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
