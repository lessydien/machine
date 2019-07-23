package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class TransaksiObject {

    @Expose
    @SerializedName("id")
    private Integer id;

    @Expose
    @SerializedName("cust_id")
    private String cust_id;

    @Expose
    @SerializedName("trans_datein")
    private String trans_datein;

    @Expose
    @SerializedName("trans_dateout")
    private String trans_dateout;

    @Expose
    @SerializedName("durasi")
    private Integer durasi;

    @Expose
    @SerializedName("tipe_kendaraan")
    private Integer tipe_kendaraan;

    @Expose
    @SerializedName("harga_sewa")
    private Integer harga_sewa;

    public TransaksiObject(Integer id, String cust_id, String trans_datein, String trans_dateout, Integer durasi, Integer tipe_kendaraan, Integer harga_sewa) {
        this.id = id;
        this.cust_id = cust_id;
        this.trans_datein = trans_datein;
        this.trans_dateout = trans_dateout;
        this.durasi = durasi;
        this.tipe_kendaraan = tipe_kendaraan;
        this.harga_sewa = harga_sewa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getTrans_datein() {
        return trans_datein;
    }

    public void setTrans_datein(String trans_datein) {
        this.trans_datein = trans_datein;
    }

    public String getTrans_dateout() {
        return trans_dateout;
    }

    public void setTrans_dateout(String trans_dateout) {
        this.trans_dateout = trans_dateout;
    }

    public Integer getDurasi() {
        return durasi;
    }

    public void setDurasi(Integer durasi) {
        this.durasi = durasi;
    }

    public Integer getTipe_kendaraan() {
        return tipe_kendaraan;
    }

    public void setTipe_kendaraan(Integer tipe_kendaraan) {
        this.tipe_kendaraan = tipe_kendaraan;
    }

    public Integer getHarga_sewa() {
        return harga_sewa;
    }

    public void setHarga_sewa(Integer harga_sewa) {
        this.harga_sewa = harga_sewa;
    }
}
