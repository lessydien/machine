package com.jadaperkasa.station.service.adapter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.jadaperkasa.station.service.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction transaction);

    @Query("SELECT * FROM  transaksi where status_update = 0 ")
    List<Transaction> ambilDataTransaksiBelumUpdate();

    @Query("UPDATE transaksi SET status_update = 1 where  status_update = 0 ")
    void updateTransaksiSudahUpdate();

    @Query("DELETE FROM transaksi  where  status_update = 1 ")
    void deleteTransaksiSudahUpdate();

    @Query("UPDATE transaksi SET trans_dateout = :time_updated , durasi = :durasi, pakaiParkir=0 WHERE cust_id= :cust_id  AND pakaiParkir=1")
    void updateTransaksiKeluarParkir(String cust_id, int durasi, String time_updated);

    @Query("DELETE FROM transaksi   ")
    void deleteAllTransaksi();



}
