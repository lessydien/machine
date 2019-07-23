package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class responseUpload {
    @Expose
    @SerializedName("pesan")
    private Integer pesan;

    @Expose
    @SerializedName("idpinjam")
    private Integer idpinjam;

    @Expose
    @SerializedName("nama")
    private String nama;

    public responseUpload(Integer pesan) {
        this.pesan = pesan;
    }

    public Integer getPesan() {
        return pesan;
    }

    public void setPesan(Integer pesan) {
        this.pesan = pesan;
    }


    public Integer getIdpinjam() {
        return idpinjam;
    }

    public void setIdpinjam(Integer idpinjam) {
        this.idpinjam = idpinjam;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
}
