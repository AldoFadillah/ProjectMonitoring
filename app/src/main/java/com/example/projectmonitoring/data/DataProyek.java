package com.example.projectmonitoring.data;

public class DataProyek {
    private String id_proyek, nama_proyek;

    public DataProyek() {
    }

    public DataProyek(String id_proyek, String nama_proyek) {
        this.id_proyek = id_proyek;
        this.nama_proyek = nama_proyek;
    }

    public String getIdProyek() {
        return id_proyek;
    }

    public void setIdProyek(String id_proyek) {
        this.id_proyek = id_proyek;
    }

    public String getNamaProyek() {
        return nama_proyek;
    }

    public void setNamaProyek(String nama_proyek) {
        this.nama_proyek = nama_proyek;
    }
}
