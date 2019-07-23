package com.jadaperkasa.station.service.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "customer")
public class Customer {
    @PrimaryKey
    private @NonNull
    String cust_id;
    private @NonNull
    String name;
    private int saldo;
    private int status;
    private String last_modified;
    private int ambilSaldo;

    public Customer(String cust_id, String name, int saldo, String last_modified, int status,int ambilSaldo) {
        this.cust_id = cust_id;
        this.name = name;
        this.saldo = saldo;
        this.last_modified = last_modified;
        this.status = status;
        this.ambilSaldo = ambilSaldo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    @NotNull
    public String getCust_id() {
        return cust_id;
    }

    public String getName() {
        return name;
    }

    public int getSaldo() {
        return saldo;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCust_id(@NonNull String cust_id) {
        this.cust_id = cust_id;
    }

    public int getAmbilSaldo() {
        return ambilSaldo;
    }

    public void setAmbilSaldo(int ambilSaldo) {
        this.ambilSaldo = ambilSaldo;
    }
}
