package com.jadaperkasa.station.tools;

import android.content.Context;
import android.widget.Toast;

import com.jadaperkasa.station.service.json.CustomerObject;
import com.jadaperkasa.station.service.json.TransaksiObject;
import com.jadaperkasa.station.service.model.Customer;
import com.jadaperkasa.station.service.model.Transaction;
import com.jadaperkasa.station.service.repository.RestRepo;
import com.jadaperkasa.station.viewmodel.NFCViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SyncronizeData {
    private Context context;
    private NFCViewModel nfcViewModel;

    public SyncronizeData(Context context, NFCViewModel nfcViewModel) {
        this.context = context;
        this.nfcViewModel = nfcViewModel;
    }
    public void executeSycn(){
        InternetService internetService = new InternetService();
        if (internetService.isOnline()){
           // Toast.makeText(context,"Connection Online",Toast.LENGTH_SHORT).show();
            try {
                List<Transaction> myList = nfcViewModel.ambilDataTransaksiBelumUpdate();
                List<TransaksiObject> myListJson = new ArrayList<>();
                RestRepo restRepo =  new RestRepo();
                int size = myList.size();
                if (size !=0) {
                    for (int i = 0; i < size; i++) {
                        myListJson.add(new TransaksiObject((int)myList.get(i).getId(),myList.get(i).getCust_id(),
                                myList.get(i).getTrans_datein(),myList.get(i).getTrans_datein(),0,
                                myList.get(i).getTipe_kendaraan(),myList.get(i).getHarga_sewa()));
                    }

                    restRepo.setTransaksi(myListJson);
                    restRepo.uploadDatalangsungDelete(nfcViewModel);
                    myListJson.clear();
                }

                List<Customer> myListCust = nfcViewModel.ambildataUserAll();
                List<CustomerObject> myListCustJSon = new ArrayList<>();
                size = myListCust.size();
                if (size !=0) {
                    for (int i = 0; i < size; i++) {
                        myListCustJSon.add(new CustomerObject(myListCust.get(i).getCust_id(),myListCust.get(i).getName(),
                                myListCust.get(i).getSaldo(),myListCust.get(i).getLast_modified(),
                                myListCust.get(i).getStatus(),myListCust.get(i).getAmbilSaldo()));
                    }

                    restRepo.setCustomer(myListCustJSon);
                    restRepo.sinkronCustomer(nfcViewModel);
                    myListJson.clear();
                }
                else{
                    restRepo.getCustomer(nfcViewModel);
                }





            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{
        //    Toast.makeText(context,"Connection Time Out pakai database lokal",Toast.LENGTH_SHORT).show();
        }

    }
}
