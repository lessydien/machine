package com.jadaperkasa.station.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jadaperkasa.station.R;
import com.jadaperkasa.station.common.mainApp;
import com.jadaperkasa.station.service.json.Operatorobject;
import com.jadaperkasa.station.service.json.PeminjamanObject;
import com.jadaperkasa.station.service.json.responseUpload;
import com.jadaperkasa.station.service.repository.RestRepo;
import com.jadaperkasa.station.tools.InternetService;

import java.text.BreakIterator;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StationAuth extends AppCompatActivity {

    private String idktp;
    private Snackbar mysnack;
    private InternetService internetService;
    private String nama;
    private SharedPreferences myshare ;
    private SharedPreferences.Editor prefEdit;
    private boolean sta_login=false;
    private TextView myContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station_auth);
        internetService =  new InternetService();
        myContent = findViewById(R.id.ektp_content);
        myshare = getSharedPreferences(mainApp.MY_SHARE,MODE_PRIVATE);
        sta_login = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);

        if(!sta_login){
            setTitle(getString(R.string.sta_off_full)+" "+ getString(R.string.login_CAPS));
            myContent.setText(getString(R.string.sta_off_short)+ " "+ getString(R.string.login_CAPS));
            myContent.setTextColor(Color.GREEN);

        }
        else {
            setTitle(getString(R.string.sta_off_full)+ " " +getString(R.string.logout_CAPS));
            myContent.setText(getString(R.string.sta_off_short)+ " "+ getString(R.string.logout_CAPS));
            myContent.setTextColor(Color.RED);
        }

        mainApp.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        if (!mainApp.getNfcAdapter().isEnabled()) {
            checkNFC(getString(R.string.error_nfc));
        }
        else{
            checkConnection();
        }
    }

    public void onResume() {
        super.onResume();
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
        switch (card) {
            case mainApp.ISODEP: {
                mainApp.setMyIsodep(mainApp.getTag());
                idktp = mainApp.byte2HexString(mainApp.getUID());
                checkAcc();
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
                    "",mainApp.STA_TABLE);

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
                if(requestCode == mainApp.HAL_TEST ){
                    finish();
                }
            }
        }
    }



    public void setNamaField(responseUpload responseupload){
        if(!sta_login) {
            nama = responseupload.getNama();
            prefEdit = myshare.edit();
            prefEdit.putBoolean(mainApp.STA_LOGIN, true);
            prefEdit.putString(mainApp.STA_KTP,idktp);
            prefEdit.putString(mainApp.STA_NAME,nama);
            prefEdit.apply();

        }
        simpanPeminjamMesin();

    }

    public void saveLogin(responseUpload responseupload){

        prefEdit = myshare.edit();
        prefEdit.putBoolean(mainApp.FIELD_LOGIN, !sta_login);
        prefEdit.putBoolean(mainApp.STA_LOGIN,!sta_login);
        prefEdit.putBoolean(mainApp.MESIN_DIPINJAM,!sta_login);
        prefEdit.apply();

        sta_login = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);
        if(sta_login){
            prefEdit.putInt(mainApp.ID_PINJAM,responseupload.getIdpinjam());
            prefEdit.putBoolean(mainApp.SElESAI_PINJAM,false);
            prefEdit.apply();

        }
        else{
            prefEdit.putBoolean(mainApp.SElESAI_PINJAM,true);
            prefEdit.apply();
        }


        finish();
    }

    private void simpanPeminjamMesin() {
        String field_ktp = myshare.getString(mainApp.FIELD_KTP,"");
        int batt = myshare.getInt(mainApp.BATT_LEVEL,0);
        String my_IMEI = myshare.getString(mainApp.ID_MACHINE,"");
        int suara_status=1,layar_status=1;
        String ket =  getIntent().getStringExtra(mainApp.KET);
        Single<responseUpload> callUpload;
        if(sta_login){
            boolean suara2 = myshare.getBoolean(mainApp.SUARA_STATUS,false);
            if(suara2) suara_status=3;
            boolean layar2 = myshare.getBoolean(mainApp.SUARA_STATUS,false);
            if(layar2) layar_status=3;

        }

        if (internetService.isOnline()) {
            RestRepo restRepo =  new RestRepo();
            PeminjamanObject peminjamanObject = new PeminjamanObject(0,my_IMEI,field_ktp,idktp,1,layar_status,1,
                    suara_status,ket,batt,batt,1,idktp,ket,field_ktp,mainApp.tanggalSekarang(),mainApp.tanggalSekarang());

            if(sta_login) {
                peminjamanObject.setIdpinjam(myshare.getInt(mainApp.ID_PINJAM,0));
                peminjamanObject.setStatus(0);
                callUpload = restRepo.updatePeminjaman(peminjamanObject);
            }
            else {
                callUpload = restRepo.tambahPeminjaman(peminjamanObject);
            }


            callUpload.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                    subscribe(
                            new SingleObserver<responseUpload>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(responseUpload responseupload) {
                                    saveLogin(responseupload);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    tampilSnack("GAGAL !");


                                }
                            });

        }
        else{
            tampilSnack(getString(R.string.erro_inet));
        }
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
