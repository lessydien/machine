package com.jadaperkasa.station.service.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerObject {
    @Expose
    @SerializedName("cust_id")
    private String cust_id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("saldo")
    private Integer saldo;

    @Expose
    @SerializedName("last_modified")
    private String last_modified;

    @Expose
    @SerializedName("status")
    private Integer status;

    @Expose
    @SerializedName("ambilSaldo")
    private Integer ambilSaldo;


    public CustomerObject(String cust_id, String name, Integer saldo, String last_modified, Integer status, Integer ambilSaldo) {
        this.cust_id = cust_id;
        this.name = name;
        this.saldo = saldo;
        this.last_modified= last_modified;
        this.status = status;
        this.ambilSaldo = ambilSaldo;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAmbilSaldo() {
        return ambilSaldo;
    }

    public void setAmbilSaldo(Integer ambilSaldo) {
        this.ambilSaldo = ambilSaldo;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }
}


