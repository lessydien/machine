package com.jadaperkasa.station.service.repository;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jadaperkasa.station.service.adapter.CustomerDao;
import com.jadaperkasa.station.service.adapter.TransactionDao;
import com.jadaperkasa.station.service.databases.containerDB;
import com.jadaperkasa.station.service.json.CustomerObject;
import com.jadaperkasa.station.service.model.Customer;
import com.jadaperkasa.station.service.model.Transaction;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class adapterRepo {

    private CustomerDao customerDao;
    private LiveData<List<Transaction>> dataTransaksi;
    private TransactionDao transactionDao;



    public adapterRepo(Application application){
        containerDB database = containerDB.getInstance(application);
        customerDao = database.customerDao();
        transactionDao = database.transactionDao();

    }


    //-------------------- Customer -------//

    public List<Customer> ambildataUserAll() throws ExecutionException, InterruptedException {
        return new ambildataAllAsyncTask(customerDao).execute().get();
    }

    public void updateStatusUser(int status, int saldo, String updated, int ambil, String cust_id){
        new updateStatusUserAsyncTask(customerDao,status,saldo,updated,ambil,cust_id);
    }

    public List<Customer> selectALLByKTP(String noKTP) throws ExecutionException, InterruptedException{
        return new selectALLByKTPAsyncTask(customerDao, noKTP).execute().get();
    }

    public void insertCust(Customer customer){
        new InsertCustomerAsyncTask(customerDao).execute(customer);
    }

    public void insertMultiCust(List<CustomerObject> customerObjects){
        new InsertMultiCustomerAsyncTask(customerDao,customerObjects).execute();
    }

    public int hitungDataUser(String cust_id){
        return customerDao.hitungDataUser(cust_id);
    }

    public Customer ambildataUser(String cust_id) throws ExecutionException, InterruptedException {
        Customer dataUser = new findCustomerAsyncTask(customerDao, cust_id).execute().get();
        return dataUser;
    }

    public void updateSaldoUser(String cust_id, int saldo, String updated){
        new updateSaldoUserAsyncTask(customerDao,cust_id,saldo, updated).execute();

    }

    public void deleteCust(String cust_id){
        new deleteCustAsycnTask(customerDao,cust_id).execute();
    }


    // --------Transaksi ------//

    public void deleteAllTransaksi(){
        new deleteTransAsyncTask(transactionDao).execute();
    }

    public void insertTrans(Transaction transaction){
        new InsertTransAsyncTask(transactionDao).execute(transaction);
    }

    public  List<Transaction> ambilDataTransaksiBelumUpdate() throws ExecutionException, InterruptedException {
        return new ambilDataTransaksiBelumUpdateAsyncTask(transactionDao).execute().get();
    }

    public void updateTransaksiSudahUpdate(){
        new updateTransaksiSudahUpdateAsyncTask(transactionDao).execute();
    }

    public void deleteTransaksiSudahUpdate(){
        new deleteTransaksiSudahUpdateAsyncTask(transactionDao).execute();
    }

    public void updateTransaksiKeluarParkir(String cust_id, int durasi, String time_updated){
        new updateTransaksiKeluarParkirAsyncTask(transactionDao,cust_id,durasi,time_updated).execute();
    }





  // ------ customer asyncTask

    private static class ambildataAllAsyncTask extends AsyncTask<Void,Void,List<Customer>> {
        private CustomerDao customerDao;

        private ambildataAllAsyncTask(CustomerDao customerDao){

            this.customerDao = customerDao;


        }

        @Override
        protected  List<Customer> doInBackground(Void... voids) {
            return customerDao.ambilDataUserAll();

        }
    }

    private static class findCustomerAsyncTask extends AsyncTask<Void,Void,Customer> {
        private CustomerDao customerDao;
        private String cust_id;


        private findCustomerAsyncTask(CustomerDao customerDao, String cust_id){

            this.customerDao = customerDao;
            this.cust_id = cust_id;

        }

        @Override
        protected  Customer doInBackground(Void... voids) {
            return customerDao.ambilDataUser(cust_id);

        }
    }

    private static class InsertCustomerAsyncTask extends AsyncTask<Customer,Void,Void> {
        private CustomerDao customerDao;

        private InsertCustomerAsyncTask(CustomerDao customerDao){

            this.customerDao = customerDao;

        }

        @Override
        protected Void doInBackground(Customer... customers) {
            customerDao.insert(customers[0]);
            return null;
        }
    }

    private static class updateSaldoUserAsyncTask extends AsyncTask<Void,Void ,Void> {
        private CustomerDao customerDao;
        private int saldo;
        private String cust_id;
        private String updated;

        private updateSaldoUserAsyncTask(CustomerDao customerDao,String cust_id, int saldo,String updated){
            this.customerDao = customerDao;
            this.cust_id = cust_id;
            this.saldo = saldo;
            this.updated = updated;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customerDao.updateSaldoUser(cust_id,saldo,updated);
            return null;
        }
    }

    private static  class selectALLByKTPAsyncTask extends AsyncTask<Void,Void,List<Customer>> {

        private CustomerDao customerDao;
        private String noKTP;

        public selectALLByKTPAsyncTask(CustomerDao customerDao, String noKTP) {
            this.customerDao = customerDao;
            this.noKTP = noKTP;
        }

        @Override
        protected List<Customer> doInBackground(Void... voids) {
            return customerDao.selectALLByKTP(noKTP);
        }
    }

    private static class updateStatusUserAsyncTask extends AsyncTask<Void,Void,Void> {
        private CustomerDao customerDao;
        private int status;
        private int saldo;
        private String updated;
        private int ambil;
        private  String cust_id;

        public updateStatusUserAsyncTask(CustomerDao customerDao, int status, int saldo, String updated, int ambil,String cust_id) {
            this.customerDao = customerDao;
            this.status = status;
            this.saldo = saldo;
            this.updated = updated;
            this.ambil = ambil;
            this.cust_id = cust_id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customerDao.updateStatusUser(status,saldo,updated,ambil,cust_id);
            return null;
        }
    }

    private static class deleteCustAsycnTask extends AsyncTask<Void,Void,Void> {
        private CustomerDao customerDao;
        private String cust_id;
        public deleteCustAsycnTask(CustomerDao customerDao, String cust_id) {
            this.cust_id = cust_id;
            this.customerDao = customerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            customerDao.deleteCust(cust_id);
            return null;
        }
    }


    // ---- Transaksi AsyncTask
    private static class InsertTransAsyncTask extends AsyncTask<Transaction,Void,Void> {
        private TransactionDao transactionDao;

        private InsertTransAsyncTask(TransactionDao transactionDao){
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            transactionDao.insert(transactions[0]);
            return null;
        }
    }

    private static class updateTransaksiSudahUpdateAsyncTask extends AsyncTask<Void,Void,Void> {

        private TransactionDao transactionDao;

        public updateTransaksiSudahUpdateAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.updateTransaksiSudahUpdate();
            return null;
        }
    }

    private static class deleteTransaksiSudahUpdateAsyncTask extends AsyncTask<Void,Void,Void> {

        private TransactionDao transactionDao;

        public deleteTransaksiSudahUpdateAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.updateTransaksiSudahUpdate();
            transactionDao.deleteTransaksiSudahUpdate();
            return null;
        }
    }

    private static class updateTransaksiKeluarParkirAsyncTask extends AsyncTask<Void,Void,Void> {

        private TransactionDao transactionDao;
        private String cust_id;
        private int durasi;
        private String time_updated;

        public updateTransaksiKeluarParkirAsyncTask(TransactionDao transactionDao,String cust_id, int durasi,
                                                    String time_updated) {
            this.transactionDao = transactionDao;
            this.cust_id = cust_id;
            this.durasi = durasi;
            this.time_updated = time_updated;

        }
        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.updateTransaksiKeluarParkir(cust_id,durasi,time_updated);
            return null;
        }
    }

    private static class deleteTransAsyncTask extends AsyncTask<Void,Void,Void>{
        private  TransactionDao transactionDao;
        public deleteTransAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            transactionDao.deleteAllTransaksi();
            return null;
        }
    }

    private static class ambilDataTransaksiBelumUpdateAsyncTask extends AsyncTask<Void,Void,List<Transaction>> {
        private TransactionDao transactionDao;
        public ambilDataTransaksiBelumUpdateAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected List<Transaction> doInBackground(Void... voids) {
            return transactionDao.ambilDataTransaksiBelumUpdate();
        }
    }

    private static class InsertMultiCustomerAsyncTask extends AsyncTask<Void,Void,Void> {
        private CustomerDao customerDao;
        private List<CustomerObject> customerObjects;
        public InsertMultiCustomerAsyncTask(CustomerDao customerDao, List<CustomerObject> customerObjects) {
            this.customerDao = customerDao;
            this.customerObjects = customerObjects;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int size = customerObjects.size();
            if (size !=0) {
                for (int i = 0; i < size; i++) {
                    Customer customer = new Customer(customerObjects.get(i).getCust_id(),customerObjects.get(i).getName(),
                            customerObjects.get(i).getSaldo(),customerObjects.get(i).getLast_modified(),
                            customerObjects.get(i).getStatus(),customerObjects.get(i).getAmbilSaldo());

                    customerDao.insert(customer);
                }
            }
            return null;
        }
    }
}
