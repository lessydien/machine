package com.jadaperkasa.station.service.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.jadaperkasa.station.service.json.CustomerContainer;
import com.jadaperkasa.station.service.json.CustomerObject;
import com.jadaperkasa.station.service.json.MachineObject;
import com.jadaperkasa.station.service.json.Operatorobject;
import com.jadaperkasa.station.service.json.PeminjamanObject;
import com.jadaperkasa.station.service.json.TransaksiContainer;
import com.jadaperkasa.station.service.json.TransaksiObject;
import com.jadaperkasa.station.service.json.responseUpload;
import com.jadaperkasa.station.service.rest.PostToCloud;
import com.jadaperkasa.station.service.rest.retroofitInstance;
import com.jadaperkasa.station.viewmodel.NFCViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestRepo {

    private TransaksiContainer transaksiContainer;
    private CustomerContainer customerContainer;


    private List<TransaksiObject> TransaksiList = new ArrayList<>();
    private List<CustomerObject> customerList = new ArrayList<>();

    private PostToCloud postToCloud;


    public RestRepo() {
        postToCloud = retroofitInstance.getRetrofitInstance().create(PostToCloud.class);
    }

    public void setTransaksi(List<TransaksiObject> myList){
        this.TransaksiList = myList;
    }

    public void setCustomer(List<CustomerObject> myList){
        this.customerList = myList;
    }




    public Single<responseUpload> tambahMesin(MachineObject machineObject) {

        return  postToCloud.tambahMesin(machineObject);


    }


    public Single<responseUpload> tambahPeminjaman(PeminjamanObject peminjamanObject) {

        return postToCloud.tambahPeminjaman(peminjamanObject);

    }

    public Single<responseUpload> updatePeminjaman(PeminjamanObject peminjamanObject) {

        return postToCloud.updatePinjam(peminjamanObject);

    }


    public Single<responseUpload> checkID(Operatorobject operatorobject) {

        return postToCloud.checkID(operatorobject);

    }

    public void uploadData() {

        transaksiContainer = new TransaksiContainer();
        transaksiContainer.setTransaksiObject(TransaksiList);

        Call<responseUpload> callUpload = postToCloud.uploadDataTransaksi(transaksiContainer);

        callUpload.enqueue(new Callback<responseUpload>() {
            @Override
            public void onResponse(@NotNull Call<responseUpload> call, @NotNull Response<responseUpload> response) {
                try{
                    if(response.isSuccessful()){

                        int pesan =response.body().getPesan();
                        Log.e("response-success",String.valueOf(pesan));

                    }
                    else{
                        //  Log.e("response-failed",response.errorBody().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<responseUpload> call, @NotNull Throwable t) {
                Log.e("response-failure", call.toString());
            }
        });

    }


    public void updateCustomerSaldo() {

        customerContainer = new CustomerContainer();
        customerContainer.setCustomerObject(customerList);

        Call<responseUpload> callUpload = postToCloud.updateCustomerSaldo(customerContainer);

        callUpload.enqueue(new Callback<responseUpload>() {
            @Override
            public void onResponse(@NotNull Call<responseUpload> call, @NotNull Response<responseUpload> response) {
                try{
                    Log.e("response-success", response.body().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<responseUpload> call, @NotNull Throwable t) {
                Log.e("response-failure", call.toString());
            }
        });

    }


    public void uploadDatalangsungDelete(final NFCViewModel nfcViewModel) {

        transaksiContainer = new TransaksiContainer();
        transaksiContainer.setTransaksiObject(TransaksiList);

        Call<responseUpload> callUpload = postToCloud.uploadDataTransaksi(transaksiContainer);

        callUpload.enqueue(new Callback<responseUpload>() {
            @Override
            public void onResponse(@NonNull Call<responseUpload> call, @NonNull Response<responseUpload> response) {
                try{
                    if(response.isSuccessful()){
                        int pesan =response.body().getPesan();
                        Log.e("response-success",String.valueOf(pesan));
                        nfcViewModel.deleteTransaksiSudahUpdate();
                        sinkronCustomer(nfcViewModel);

                    }
                    else{
                        //  Log.e("response-failed",response.errorBody().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<responseUpload> call, @NonNull Throwable t) {
                Log.e("response-failure", call.toString());
            }
        });

    }


    public void sinkronCustomer(final NFCViewModel nfcViewModel) {
        customerContainer = new CustomerContainer();
        customerContainer.setCustomerObject(customerList);

        Call<CustomerContainer> customerContainerResponse = postToCloud.sinkronCustomer(customerContainer);

        customerContainerResponse.enqueue(new Callback<CustomerContainer>() {
            @Override
            public void onResponse(@NonNull Call<CustomerContainer> call, @NonNull Response<CustomerContainer> response) {
                try{
                    if(response.isSuccessful()){
                        // response.body(); // have your all data
                        int size = response.body().getCustomerObject().size();
                        Log.e("response-success","save to local database ........!" + String.valueOf(size));
                        nfcViewModel.insertMultiCust(response.body().getCustomerObject());
                    }
                    else{
                        Log.e("response-failed",response.errorBody().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerContainer> call, @NonNull Throwable t) {
                Log.e("response-failure", call.toString());
            }
        });

    }

    public void getCustomer(final NFCViewModel nfcViewModel) {

        Call<CustomerContainer> customerContainerResponse = postToCloud.getCustomer();

        customerContainerResponse.enqueue(new Callback<CustomerContainer>() {
            @Override
            public void onResponse(@NonNull Call<CustomerContainer> call, @NonNull Response<CustomerContainer> response) {
                try{
                    if(response.isSuccessful()){
                        // response.body(); // have your all data
                        int size = response.body().getCustomerObject().size();
                        Log.e("response-success","save to local database ........!" + String.valueOf(size));
                        nfcViewModel.insertMultiCust(response.body().getCustomerObject());
                    }
                    else{
                        Log.e("response-failed",response.errorBody().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerContainer> call, @NonNull Throwable t) {
                Log.e("response-failure", call.toString());
            }
        });

    }

}
