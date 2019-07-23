package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineObject {
    @Expose
    @SerializedName("idbarang")
    private String idbarang;

    @Expose
    @SerializedName("ket")
    private String ket;

    @Expose
    @SerializedName("batt")
    private Integer batt;

    @Expose
    @SerializedName("layar")
    private Integer layar;

    @Expose
    @SerializedName("wifi")
    private Integer wifi;

    @Expose
    @SerializedName("suara")
    private Integer suara;

    @Expose
    @SerializedName("reader")
    private Integer reader;

    @Expose
    @SerializedName("pinjam")
    private Integer pinjam;


    public MachineObject(String idbarang, String ket, Integer batt, Integer layar, Integer wifi, Integer suara, Integer reader,Integer pinjam) {
        this.idbarang = idbarang;
        this.ket = ket;
        this.batt = batt;
        this.layar = layar;
        this.wifi = wifi;
        this.suara = suara;
        this.reader = reader;
        this.pinjam = pinjam;
    }


    public String getIdbarang() {
        return idbarang;
    }

    public void setIdbarang(String idbarang) {
        this.idbarang = idbarang;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public Integer getBatt() {
        return batt;
    }

    public void setBatt(Integer batt) {
        this.batt = batt;
    }

    public Integer getLayar() {
        return layar;
    }

    public void setLayar(Integer layar) {
        this.layar = layar;
    }

    public Integer getWifi() {
        return wifi;
    }

    public void setWifi(Integer wifi) {
        this.wifi = wifi;
    }

    public Integer getSuara() {
        return suara;
    }

    public void setSuara(Integer suara) {
        this.suara = suara;
    }

    public Integer getReader() {
        return reader;
    }

    public void setReader(Integer reader) {
        this.reader = reader;
    }

    public Integer getPinjam() {
        return pinjam;
    }

    public void setPinjam(Integer pinjam) {
        this.pinjam = pinjam;
    }
}


