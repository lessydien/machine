package com.jadaperkasa.station.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.jadaperkasa.station.R;
import com.jadaperkasa.station.common.mainApp;
import com.jadaperkasa.station.service.json.Operatorobject;
import com.jadaperkasa.station.service.json.responseUpload;
import com.jadaperkasa.station.service.repository.RestRepo;
import com.jadaperkasa.station.tools.InternetService;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FieldAuth extends AppCompatActivity {


    private String idktp;
    private Snackbar mysnack;
    private InternetService internetService;
    private String nama;
    private SharedPreferences myshare ;
    private SharedPreferences.Editor prefEdit;
    private boolean field_login;
    private TextView myContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_auth);
        myContent= findViewById(R.id.ektp_content);
        internetService =  new InternetService();
       // myshare = getSharedPreferences(mainApp.MY_SHARE,MODE_PRIVATE);
        field_login = returnValue();
        if(!field_login) {
            setTitle(getString(R.string.field_off_full)+ " "+ getString(R.string.login_CAPS));
            myContent.setText(getString(R.string.field_off_short)+ " "+ getString(R.string.login_CAPS));
            myContent.setTextColor(Color.GREEN);
        }


        mainApp.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (!mainApp.getNfcAdapter().isEnabled()) {
            checkNFC(getString(R.string.error_nfc));
        }
        else{
            checkConnection();
        }

    }


    public boolean returnValue(){
         myshare = getSharedPreferences(mainApp.MY_SHARE, Context.MODE_PRIVATE);
         boolean check = myshare.contains(mainApp.FIELD_LOGIN);
        // tampilSnack(Boolean.toString(check));
         if(myshare.contains(mainApp.FIELD_LOGIN)){
            return myshare.getBoolean(mainApp.FIELD_LOGIN,false);
        }
        else {
            return false;
        }

    }

    public void onResume() {
        super.onResume();

        if(!myshare.getBoolean(mainApp.MESIN_BALIK,false) && myshare.getBoolean(mainApp.MESIN_DIPINJAM,false)  ){
            finish();
        }
        else{
            if(myshare.getBoolean(mainApp.MESIN_BALIK,false) && myshare.getBoolean(mainApp.MESIN_DIPINJAM,false)){
                setTitle(getString(R.string.field_off_full)+ " "+ getString(R.string.logout_CAPS));
                myContent.setText(getString(R.string.field_off_short)+ " "+ getString(R.string.logout_CAPS));
                myContent.setTextColor(Color.RED);
            }
            else {
                if(myshare.getBoolean(mainApp.SElESAI_PINJAM,false)){
                    tampilSnack("MESIN BERHASIL DIKEMBALIKAN");
                    prefEdit = myshare.edit();
                    prefEdit.putBoolean(mainApp.SElESAI_PINJAM,false);
                    prefEdit.apply();
                }
            }

        }

        if (mainApp.getNfcAdapter() == null) {
            mainApp.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        }
        mainApp.enableNfcForegroundDispatch(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mainApp.disableNfcforegroundDispatch(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        mainApp.setPendingIntent(intent);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            mainApp.setTag(tag);
            String[] techList = tag.getTechList();
            chooseCardTye(techList[0]);

        }
    }

    public void chooseCardTye(String card) {
        String idktp2;
        switch (card) {
            case mainApp.ISODEP: {

                mainApp.setMyIsodep(mainApp.getTag());
                idktp = mainApp.byte2HexString(mainApp.getUID());
                if(!field_login) {
                    checkAcc();
                }
                else {
                    idktp2 = myshare.getString(mainApp.FIELD_KTP,"");
                    prefEdit = myshare.edit();
                    prefEdit.putString(mainApp.FIELD_KTP,idktp2);
                    prefEdit.apply();
                    if(idktp.equals(idktp2)){
                        prefEdit = myshare.edit();
                        prefEdit.putBoolean(mainApp.FIELD_LOGIN, false);
                        prefEdit.apply();
                        startTest();
                    }
                    else {
                       tampilSnack("Field OFF. BERBEDA ORANG!!!");
                    }
                }

                break;
            }

            case mainApp.NFCA: {
                tampilSnack(getString(R.string.error_ektp));
                break;
            }
        }
    }

    public void checkAcc(){
        if (internetService.isOnline()) {
            RestRepo restRepo =  new RestRepo();
            Operatorobject operatorobject = new Operatorobject(idktp,"",1,"","",
                    "",mainApp.FIELD_TABLE);

            Single<responseUpload> callUpload = restRepo.checkID(operatorobject);

            callUpload.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            new SingleObserver<responseUpload>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(responseUpload responseupload) {
                                    setNamaField(responseupload);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    tampilSnack(getString(R.string.error_ektp_cloud));
                                }
                            });

        }
        else{
            tampilSnack(getString(R.string.erro_inet));
        }
    }
    public void tampilSnack(String s) {
        mysnack =  Snackbar.make(findViewById(R.id.top_coordinator),
                s,Snackbar.LENGTH_LONG);
        mysnack.getView().setBackgroundColor(Color.RED);
        mysnack.show();
    }

    public void checkConnection(){
        internetService.setContext(this);
        if (!internetService.checkConnection()){
            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),mainApp.HALWIRELESS);
        }

    }

    public void openNFCSetting(){
        startActivityForResult(new Intent(Settings.ACTION_NFC_SETTINGS),mainApp.HALNFC);
    }

    public void checkNFC(String s1) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informasi");
        builder.setMessage(s1);
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openNFCSetting();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mainApp.HALNFC) {
            checkConnection();
        }
        else{
            if (requestCode == mainApp.HALWIRELESS){
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS),mainApp.MOBILE);
            }
            else {
                if(requestCode == mainApp.HAL_TEST || requestCode == mainApp.HAL_STA ){
                   // tampilSnack("dari halaman station");
                    finish();
                }
            }
        }
    }

    public void startTest(){
        Intent intent = new Intent(this, MachineTest.class);
//        intent.putExtra("idktp",idktp);
//        intent.putExtra("nama", nama);
        startActivityForResult(intent,mainApp.HAL_TEST );
    }

    public void setNamaField(responseUpload responseupload){
        nama = responseupload.getNama();
        prefEdit = myshare.edit();
        prefEdit.putString(mainApp.FIELD_KTP,idktp);
        prefEdit.putString(mainApp.FIELD_NAME,nama);
        prefEdit.apply();
        startTest();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        prefEdit = myshare.edit();
//        prefEdit.clear();
//        prefEdit.apply();

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }



}
