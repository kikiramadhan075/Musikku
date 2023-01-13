package com.erly.musikku;

public class ModelPegawai {
    private String noid;
    private String nama;
    private String tanggal;
    private String jk;
    private String alamat;
    private String goldar;
    private String keadaan;
    private String image;
    private String key;

    public ModelPegawai(){

    }

    public ModelPegawai(String noid, String nama, String tanggal, String jk, String alamat, String goldar, String keadaan, String image) {
        this.noid = noid;
        this.nama = nama;
        this.tanggal = tanggal;
        this.jk = jk;
        this.alamat = alamat;
        this.goldar = goldar;
        this.keadaan = keadaan;
        this.image = image;
    }

    public String getNoid() {
        return noid;
    }

    public void setNoid(String noid) {
        this.noid = noid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGoldar() {
        return goldar;
    }

    public void setGoldar(String goldar) {
        this.goldar = goldar;
    }

    public String getKeadaan() {
        return keadaan;
    }

    public void setKeadaan(String keadaan) {
        this.keadaan = keadaan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
