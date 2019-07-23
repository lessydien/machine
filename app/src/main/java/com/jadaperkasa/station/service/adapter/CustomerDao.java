package com.jadaperkasa.station.service.adapter;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jadaperkasa.station.service.model.Customer;

import java.util.List;

@Dao
public interface CustomerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Customer customer);

    @Query("SELECT * FROM customer WHERE cust_id = :cust_id")
    Customer ambilDataUser(String cust_id);

    @Query("SELECT * FROM customer WHERE name = :noKTP AND status=1")
    List<Customer> selectALLByKTP(String noKTP);

    @Query("DELETE  FROM customer WHERE cust_id = :cust_id")
    void deleteCust(String cust_id);


    @Query("SELECT * FROM customer ")
    List<Customer> ambilDataUserAll();

    @Query("SELECT COUNT(cust_id) FROM customer WHERE cust_id = :cust_id AND status = 1")
    int hitungDataUser(String cust_id);

    @Query("UPDATE customer SET saldo = :saldo , last_modified = :updated WHERE cust_id= :cust_id ")
    void updateSaldoUser(String cust_id, int saldo, String updated);

    @Query("UPDATE customer SET status = :status, saldo =:saldo , last_modified = :updated, ambilSaldo= :ambil WHERE cust_id= :cust_id ")
    void updateStatusUser(int status, int saldo, String updated, int ambil, String cust_id);
}

