package com.jadaperkasa.station.service.databases;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jadaperkasa.station.service.adapter.CustomerDao;
import com.jadaperkasa.station.service.adapter.TransactionDao;
import com.jadaperkasa.station.service.model.Customer;
import com.jadaperkasa.station.service.model.Transaction;


@Database(entities = {Customer.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class containerDB extends RoomDatabase {
    private static containerDB instance;

    public abstract CustomerDao customerDao();
    public abstract TransactionDao transactionDao();

    public static containerDB getInstance(Context context){
        if (instance == null){
            synchronized (containerDB.class) {
                if (instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), containerDB.class,
                            "nfcvalidator.db").fallbackToDestructiveMigration().addCallback(roomCallBack)
                            .build();
                }

            }
        }

        return instance;
    }

    private static Callback roomCallBack = new Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDBAsyncTask(instance).execute();
            Log.d("nfcvalidator.db", "create database ........");
        }
    };

   /* private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private CustomerDao customerDao;
        private TransactionDao transactionDao;

        private PopulateDBAsyncTask(containerDB db){

            customerDao = db.customerDao();
            transactionDao = db.transactionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            customerDao.insert(new Customer("0001","Dien",5000,"2019-06-10",0,1));
            transactionDao.insert(new Transaction(1000,"1",1,"2019-06-10 10:10;10",
                    "2019-06-10 10:10;10",1,0,1,1000));
            return null;
        }
    }*/
}
