package com.example.projectmonitoring.data;

public class DataBarang {
    private String id_barang, nama_barang, harga_satuanbrg;

    public DataBarang() {
    }

    public DataBarang(String id_barang, String nama_barang, String harga_satuanbrg) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.harga_satuanbrg = harga_satuanbrg;
    }

    public String getIdBarang() {
        return id_barang;
    }

    public void setIdBarang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getNamaBarang() {
        return nama_barang;
    }

    public void setNamaBarang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHargaSatuanBrg() {
        return harga_satuanbrg;
    }

    public void setHargaSatuanBrg(String harga_satuanbrg) {
        this.harga_satuanbrg = harga_satuanbrg;
    }
}
