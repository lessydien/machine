package com.jadaperkasa.station.service.rest;


import com.jadaperkasa.station.service.json.CustomerContainer;
import com.jadaperkasa.station.service.json.MachineObject;
import com.jadaperkasa.station.service.json.Operatorobject;
import com.jadaperkasa.station.service.json.PeminjamanObject;
import com.jadaperkasa.station.service.json.TransaksiContainer;
import com.jadaperkasa.station.service.json.responseUpload;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface PostToCloud {

    @POST("mesin/insert.php")
    Single<responseUpload> tambahMesin(
            @Body MachineObject machineObject
    );


    @POST("peminjaman/insert.php")
    Single<responseUpload> tambahPeminjaman(
            @Body PeminjamanObject peminjamanObject
    );

    @POST("peminjaman/update.php")
    Single<responseUpload> updatePinjam(
            @Body PeminjamanObject peminjamanObject
    );

    @POST("operator/login_pass.php")
    Single<responseUpload> checkID(
            @Body Operatorobject operatorobject
    );

    @POST("customer/updateSaldo.php")
    Call<responseUpload> updateCustomerSaldo(
            @Body CustomerContainer customerContainer
    );


    @POST("transaksi/insert.php")
    Call<responseUpload> uploadDataTransaksi(
            @Body TransaksiContainer transaksiContainer
    );

    @POST("customer/sinkronCustomer.php")
    Call<CustomerContainer> sinkronCustomer(
            @Body CustomerContainer customerContainer
    );


    @GET("customer/read.php")
    Call<CustomerContainer> getCustomer();



}
