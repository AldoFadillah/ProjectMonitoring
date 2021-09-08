package com.example.projectmonitoring.data;

public class DataModelKwitansi {
    private String id_proyek, nama_proyek, nama_client, tgl_mulaiproyek, tgl_selesaiproyek;

    public DataModelKwitansi() {
    }

    public DataModelKwitansi(String id_proyek, String nama_proyek, String nama_client, String tgl_mulaiproyek, String tgl_selesaiproyek) {
        this.id_proyek = id_proyek;
        this.nama_proyek = nama_proyek;
        this.nama_client = nama_client;
        this.tgl_mulaiproyek = tgl_mulaiproyek;
        this.tgl_selesaiproyek = tgl_selesaiproyek;
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

    public String getTanggalMulaiProyek() {
        return tgl_mulaiproyek;
    }

    public void setTanggalMulaiProyek(String tgl_mulaiproyek) {
        this.tgl_mulaiproyek = tgl_mulaiproyek;
    }

    public String getNamaClient() {
        return nama_client;
    }

    public void setNamaClient(String nama_client) {
        this.nama_client = nama_client;
    }

    public String getTanggalSelesaiProyek() {
        return tgl_selesaiproyek;
    }

    public void setTanggalSelesaiProyek(String tgl_selesaiproyek) {
        this.tgl_selesaiproyek = tgl_selesaiproyek;
    }
}