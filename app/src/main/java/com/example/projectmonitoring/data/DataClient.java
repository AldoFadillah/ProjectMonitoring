package com.example.projectmonitoring.data;

public class DataClient {
    private String id_client, nama_client;

    public DataClient() {
    }

    public DataClient(String id_client, String nama_client) {
        this.id_client = id_client;
        this.nama_client = nama_client;
    }

    public String getIdClient() {
        return id_client;
    }

    public void setIdClient(String id_client) {
        this.id_client = id_client;
    }

    public String getNamaClient() {
        return nama_client;
    }

    public void setNamaClient(String nama_client) {
        this.nama_client = nama_client;
    }
}
