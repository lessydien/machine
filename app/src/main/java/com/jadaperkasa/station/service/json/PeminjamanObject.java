package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PeminjamanObject {
    @Expose
    @SerializedName("idpinjam")
    private Integer idpinjam;


    @Expose
    @SerializedName("keluar")
    private String keluar;

    @Expose
    @SerializedName("masuk")
    private String masuk;


    @Expose
    @SerializedName("id_barang")
    private String id_barang;

    @Expose
    @SerializedName("idfield")
    private String idfield;

    @Expose
    @SerializedName("idfield2")
    private String idfield2;

    @Expose
    @SerializedName("idstation")
    private String idstation;

    @Expose
    @SerializedName("idstation2")
    private String idstation2;

    @Expose
    @SerializedName("wifi")
    private Integer wifi;

    @Expose
    @SerializedName("layar")
    private Integer layar;

    @Expose
    @SerializedName("reader")
    private Integer reader;

    @Expose
    @SerializedName("suara")
    private Integer suara;

    @Expose
    @SerializedName("ket")
    private String ket;

    @Expose
    @SerializedName("ket2")
    private String ket2;

    @Expose
    @SerializedName("batt_out")
    private Integer batt_out;

    @Expose
    @SerializedName("batt_in")
    private Integer batt_in;


    @Expose
    @SerializedName("status")
    private Integer status;


    public PeminjamanObject(Integer idpinjam, String id_barang, String idfield, String idstation, Integer wifi,
                            Integer layar, Integer reader, Integer suara, String ket, Integer batt_out, Integer batt_in,
                            Integer status, String idstation2, String ket2, String idfield2, String keluar, String masuk) {
        this.idpinjam = idpinjam;
        this.id_barang = id_barang;
        this.idfield = idfield;
        this.idstation = idstation;
        this.wifi = wifi;
        this.layar = layar;
        this.reader = reader;
        this.suara = suara;
        this.ket = ket;
        this.batt_out = batt_out;
        this.batt_in = batt_in;
        this.status = status;
        this.idstation2 = idstation2;
        this.ket2 =  ket2;
        this.idfield2 = idfield2;
        this.masuk = masuk;
        this.keluar = keluar;
    }

    public Integer getIdpinjam() {
        return idpinjam;
    }

    public void setIdpinjam(Integer idpinjam) {
        this.idpinjam = idpinjam;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getIdfield() {
        return idfield;
    }

    public void setIdfield(String idfield) {
        this.idfield = idfield;
    }

    public String getIdstation() {
        return idstation;
    }

    public void setIdstation(String idstation) {
        this.idstation = idstation;
    }


    public Integer getWifi() {
        return wifi;
    }

    public void setWifi(Integer wifi) {
        this.wifi = wifi;
    }

    public Integer getLayar() {
        return layar;
    }

    public void setLayar(Integer layar) {
        this.layar = layar;
    }

    public Integer getReader() {
        return reader;
    }

    public void setReader(Integer reader) {
        this.reader = reader;
    }

    public Integer getSuara() {
        return suara;
    }

    public void setSuara(Integer suara) {
        this.suara = suara;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public Integer getBatt_out() {
        return batt_out;
    }

    public void setBatt_out(Integer batt_out) {
        this.batt_out = batt_out;
    }

    public Integer getBatt_in() {
        return batt_in;
    }

    public void setBatt_in(Integer batt_in) {
        this.batt_in = batt_in;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdstation2() {
        return idstation2;
    }

    public void setIdstation2(String idstation2) {
        this.idstation2 = idstation2;
    }

    public String getIdfield2() {
        return idfield2;
    }

    public void setIdfield2(String idfield2) {
        this.idfield2 = idfield2;
    }

    public String getKeluar() {
        return keluar;
    }

    public void setKeluar(String keluar) {
        this.keluar = keluar;
    }

    public String getMasuk() {
        return masuk;
    }

    public void setMasuk(String masuk) {
        this.masuk = masuk;
    }

    public String getKet2() {
        return ket2;
    }

    public void setKet2(String ket2) {
        this.ket2 = ket2;
    }
}


