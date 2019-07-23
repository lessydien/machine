package com.jadaperkasa.station.service.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transaksi")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private @NonNull
    long id;
    private String cust_id;
    private int durasi;
    private String trans_datein;
    private String trans_dateout;
    private int pakaiParkir;
    private int tipe_kendaraan;
    private int status_update;
    private int harga_sewa;



    public Transaction( long id,String cust_id, int durasi, String trans_datein, String trans_dateout, int pakaiParkir, int tipe_kendaraan, int status_update, int harga_sewa) {
        this.id = id;
        this.cust_id = cust_id;
        this.durasi = durasi;
        this.trans_datein = trans_datein;
        this.trans_dateout = trans_dateout;
        this.pakaiParkir = pakaiParkir;
        this.tipe_kendaraan = tipe_kendaraan;
        this.status_update = status_update;
        this.harga_sewa = harga_sewa;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public void setDurasi(int durasi) {
        this.durasi = durasi;
    }

    public void setTipe_kendaraan(int tipe_kendaraan) {
        this.tipe_kendaraan = tipe_kendaraan;
    }
    public void setStatus_update (int status_update) {
        this.status_update = status_update;
    }


    public long getId() {
        return id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public int getDurasi() {
        return durasi;
    }

    public int getTipe_kendaraan() {
        return tipe_kendaraan;
    }

    public int getStatus_update() {
        return status_update;
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

    public int getPakaiParkir() {
        return pakaiParkir;
    }

    public void setPakaiParkir(int pakaiParkir) {
        this.pakaiParkir = pakaiParkir;
    }

    public int getHarga_sewa() {
        return harga_sewa;
    }

    public void setHarga_sewa(int harga_sewa) {
        this.harga_sewa = harga_sewa;
    }
}
