package com.example.projectmonitoring.data;

public class DataKaryawan {
    private String id_karyawan, name;

    public DataKaryawan() {
    }

    public DataKaryawan(String id_karyawan, String name) {
        this.id_karyawan = id_karyawan;
        this.name = name;
    }

    public String getIdKaryawan() {
        return id_karyawan;
    }

    public void setIdKaryawan(String id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getNamaKaryawan() {
        return name;
    }

    public void setNamaKaryawan(String name) {
        this.name = name;
    }
}
