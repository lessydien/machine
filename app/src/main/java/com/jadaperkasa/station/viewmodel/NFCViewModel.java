package com.jadaperkasa.station.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.jadaperkasa.station.service.json.CustomerObject;
import com.jadaperkasa.station.service.model.Customer;
import com.jadaperkasa.station.service.model.Transaction;
import com.jadaperkasa.station.service.repository.adapterRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NFCViewModel extends AndroidViewModel {
    private adapterRepo adR;
    public NFCViewModel(@NonNull Application application) {
        super(application);
        adR = new adapterRepo(application);

    }

    public void insertCust(Customer customer){
        adR.insertCust(customer);
    }

    public void deleteCust(String cust_id){
        adR.deleteCust(cust_id);
    }


    public  void insertMultiCust(List<CustomerObject> customerObjects){
        adR.insertMultiCust(customerObjects);
    }

    public Customer ambildataUser(String cust_id){
        Customer customer = null;
        try {
            customer =   adR.ambildataUser(cust_id);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public void updateSaldoUser(String cust_id, int saldo, String updated){
        adR.updateSaldoUser(cust_id,saldo,updated);
    }

    public void updateStatusUser(int status,int saldo,String updated,int ambil, String cust_id) {
        adR.updateStatusUser(status,saldo,updated,ambil,cust_id);
    }

    public int  hitungDataUser(String cust_id) {
        return adR.hitungDataUser(cust_id);
    }


    public List<Customer> ambildataUserAll() throws ExecutionException, InterruptedException {
        return adR.ambildataUserAll();
    }

    public List<Customer> selectALLByKTP(String noKTP) throws ExecutionException, InterruptedException {
        return adR.selectALLByKTP(noKTP);
    }

    //------------transaksi
    public void insertTrans(Transaction transaction) {
        adR.insertTrans(transaction);
    }


   public  List<Transaction> ambilDataTransaksiBelumUpdate() throws ExecutionException, InterruptedException {
        return  adR.ambilDataTransaksiBelumUpdate();
    }

    public void deleteAllTransaksi(){
        adR.deleteAllTransaksi();
    }

  /*  public void updateTransaksiSudahUpdate(){
        adR.updateTransaksiSudahUpdate();
    }*/

    public void deleteTransaksiSudahUpdate(){
        adR.deleteTransaksiSudahUpdate();
    }

    /*
    public void updateTransaksiKeluarParkir(String cust_id, int durasi, String time_updated){
        adR.updateTransaksiKeluarParkir(cust_id,durasi,time_updated);
    }
    */


}
