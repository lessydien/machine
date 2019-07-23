package com.jadaperkasa.station.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jadaperkasa.station.R;
import com.jadaperkasa.station.common.mainApp;
import com.jadaperkasa.station.service.json.CustomerObject;
import com.jadaperkasa.station.service.json.TransaksiObject;
import com.jadaperkasa.station.service.model.Transaction;
import com.jadaperkasa.station.service.repository.RestRepo;
import com.jadaperkasa.station.tools.InternetService;
import com.jadaperkasa.station.tools.SyncronizeData;
import com.jadaperkasa.station.viewmodel.NFCViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {


    private SharedPreferences myshare ;
    private boolean mesin_dipinjam;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences.Editor prefEdit;
    private AlertDialog dialogcreate;
    private InternetService internetService;
    private Snackbar mysnack;
    private TextView statusOperation;
    private int jenisHarga = mainApp.MOTOR;
    private int status_kendaraan;
    private NFCViewModel nfcViewModel;
    private List<TransaksiObject> myList = new ArrayList<>();
    private List<CustomerObject> myListCus = new ArrayList<>();
    private int  id;
    private String data;
    private TextView ektp_content, statusLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        internetService =  new InternetService();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        statusLogin = findViewById(R.id.statusLogin);
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        statusOperation = findViewById(R.id.statusOperation);
        myshare = getSharedPreferences(mainApp.MY_SHARE, MODE_PRIVATE);
        nfcViewModel = ViewModelProviders.of(this).get(NFCViewModel.class);
        ektp_content = findViewById(R.id.ektp_content);
    //    statusLogin.setText();
        if(internetService.isOnline()){
            tampilSnack("CONNECTION ONLINE");
            sycncronizeData();
        }
        else {
            tampilSnack("TIME OUT / LOCAL DATABASE");
        }

//        prefEdit= myshare.edit();
//        prefEdit.putBoolean(mainApp.MESIN_BALIK,false);
//        prefEdit.apply();
//        if(myshare.contains(mainApp.MESIN_DIPINJAM)){
//            mesin_dipinjam = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);
//        }
//        else {
//            mesin_dipinjam = false;
//        }

//       Toast.makeText(this,Boolean.toString(mesin_dipinjam),Toast.LENGTH_SHORT).show();
//        if(!mesin_dipinjam){
//           startLogin();
//        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
//                            case R.id.transaksi:
//                                item.setCheckable(true);
//                                return true;
                            case R.id.info:
                                item.setCheckable(true);
                                return true;
                            case R.id.exit:
                                logouttApp();
                                return true;
                        }

                        return false;
                    }
                });
    }


    public void onResume() {
        super.onResume();

        mesin_dipinjam = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);
        prefEdit= myshare.edit();
        prefEdit.putBoolean(mainApp.MESIN_BALIK,false);
        prefEdit.apply();
       //Toast.makeText(this,Boolean.toString(mesin_dipinjam),Toast.LENGTH_SHORT).show();
        if(!mesin_dipinjam){
           startLogin();
        }
        else{
            String name = myshare.getString(mainApp.FIELD_NAME,"");
            statusOperation.setText(name);
            sycncronizeData();
        }
        if (mainApp.getNfcAdapter() == null) {
            mainApp.setNfcAdapter(NfcAdapter.getDefaultAdapter(this));
        }
        mainApp.enableNfcForegroundDispatch(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        ektp_content.setText("");
        mainApp.disableNfcforegroundDispatch(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        prefEdit = myshare.edit();
        prefEdit.clear();
        prefEdit.commit();
        prefEdit.apply();

    }

    @Override
    public void onStop() {
        super.onStop();


    }



    public void startLogin(){
        Intent intent = new Intent(this, FieldAuth.class);
        startActivityForResult(intent, mainApp.HAL_FIELD );
    }

    public void logouttApp() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.logout_CAPS));
            dialog.setMessage(getString(R.string.dialog_keluar));
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.ic_warning);



            dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefEdit = myshare.edit();
                    prefEdit.putBoolean(mainApp.MESIN_BALIK,true);
                    prefEdit.apply();
                    dialogcreate.dismiss();
                    startLogin();

                }
            });

            dialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogcreate.dismiss();

                }
            });

            dialogcreate = dialog.create();
            if (!dialogcreate.isShowing()){
                dialogcreate.show();
            }



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
                if(requestCode == mainApp.HAL_FIELD  ){
                    mesin_dipinjam = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);
                    if(!mesin_dipinjam){
                        startLogin();
                    }
                    else {
                        tampilSnack("MESIN BERHASIL DIPINJAM");
                    }
                }
            }
        }
    }

    public void checkConnection(){
        internetService.setContext(this);
        if (!internetService.checkConnection()){
            startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS),mainApp.HALWIRELESS);
        }

    }

    public void tampilSnack(String s) {
        mysnack =  Snackbar.make(findViewById(R.id.top_coordinator),
                s,Snackbar.LENGTH_LONG);
        mysnack.getView().setBackgroundColor(Color.RED);
        mysnack.show();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    jenisHarga = mainApp.MOBIL;
                    status_kendaraan = 1;
                    tampilSnack("MODE MOBIL");

                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    jenisHarga = mainApp.MOTOR;
                    status_kendaraan = 2;
                    tampilSnack("MODE MOTOR");
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


    public void createAlert(String pesan, String judul){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(judul);
        builder.setMessage(pesan);
        builder.setPositiveButton("OK", null);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void displayNFCMessage(String s)  {

        //pesantampil.setBackgroundColor(Color.WHITE);
        String pesanParse=null;
        String saldoawal;
        int saldo_awal;

        String pesan = "USER BELUM AKTIF!";
        String judul = "Gagal";

        String text1 = "["+mainApp.FAILED + "] NOK";;
        String warna = mainApp.MERAH;
        String toatText;

        mainApp.ConnectmyMifare();
        byte[] customKey = mainApp.createHash(mainApp.getUID());
        boolean key2 = mainApp.checkcustomKey(mainApp.SECTORNUM, customKey);

        if (key2) {
            int hitung;
            final AtomicInteger fcount = new AtomicInteger();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int hitung = nfcViewModel.hitungDataUser(mainApp.byte2HexString(mainApp.getUID()));;
                    fcount.set(hitung);
                }
            });
            t.setPriority(10);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hitung = fcount.get();

            if (hitung == 1){
                byte[]  data = mainApp.ReadfromTagcustomKey(mainApp.SECTORNUM,mainApp.BLOCKNUM, customKey);
                String dataTerima = mainApp.byteToStringPark(data);
                int statusSaldo = Character.getNumericValue(dataTerima.charAt(0));

                if (statusSaldo != 0){
                    data = mainApp.ReadfromTagcustomKey(mainApp.SECTORNUM,mainApp.BLOCKNUM, customKey);
                    saldoawal = mainApp.byteToString(data);
                    saldo_awal = Integer.parseInt(saldoawal);
                    int saldo = saldo_awal - jenisHarga;
                    if (saldo < 0){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
//                        pesan = "Saldo anda tidak mencukupi. Saldo:" +saldoawal +" Biaya: "+ String.valueOf(jenisHarga) + " Minus: "+
//                                String.valueOf(saldo);
//                        judul = "Gagal";
//                        createAlert(pesan,judul);
                        tampilSnack("SALDO TIDAK CUKUP, SALDO: "+saldoawal + " BIAYA: "+ String.valueOf(jenisHarga));
                        playNotif("mission_failed.mp3");
                    }
                    else {

                        mainApp.WritetoTagcustomKey(mainApp.SECTORNUM,mainApp.BLOCKNUM, mainApp.StringToByte( String.valueOf(saldo))
                                , customKey);
                        text1 = "["+mainApp.SUCCEED + "] OK";
                        warna = mainApp.HIJAU;
                        toatText= "TERIMA KASIH SALDO: "+ saldo +" BIAYA: " + String.valueOf(jenisHarga) ;
                      //  Toast.makeText(this, toatText,Toast.LENGTH_SHORT).show();
                        tampilSnack(toatText);
                        if(internetService.isOnline()) {
                            id = (int ) mainApp.timeToLong(mainApp.tanggalSekarang());
                            myList.add(new TransaksiObject(id,mainApp.byte2HexString(mainApp.getUID()),mainApp.tanggalSekarang(),mainApp.tanggalSekarang(),0,status_kendaraan
                                    ,jenisHarga));
                            RestRepo restRepo =  new RestRepo();
                            restRepo.setTransaksi(myList);
                            restRepo.uploadData();
                            myList.clear();
                            myListCus.add(new CustomerObject(mainApp.byte2HexString(mainApp.getUID()),"", saldo,mainApp.tanggalSekarang(),1,0));
                            restRepo.setCustomer(myListCus);
                            restRepo.updateCustomerSaldo();
                            myListCus.clear();

                        } else {
                            nfcViewModel.insertTrans(new Transaction(id,mainApp.byte2HexString(mainApp.getUID()),
                                    0,mainApp.tanggalSekarang(),mainApp.tanggalSekarang()
                                    ,0,status_kendaraan,0,jenisHarga));
                        }

                        nfcViewModel.updateSaldoUser(mainApp.byte2HexString(mainApp.getUID()),
                                saldo,mainApp.tanggalSekarang());

                        playNotif("success_ding.mp3");

                    }

                }
                else {
//                    pesan = "Saldo kosong !";
//                    judul = "Gagal";
//                    createAlert(pesan,judul);
                    tampilSnack("SALDO KOSONG");
                    playNotif("mission_failed.mp3");
                }
            }
            else {
//                pesan = "User belum diaktifkan atau tidak terdaftar!";
//                judul = "Gagal";
                tampilSnack("USER TIDAK TERDAFTAR");
                playNotif("mission_failed.mp3");
//                createAlert(pesan,judul);
            }
        }
        else{
            tampilSnack(pesan);
            //createAlert(pesan,judul);
        }

        ektp_content.setText(text1);
        ektp_content.setTextColor(Color.parseColor(warna));


    }

    @Override
    public void onNewIntent(Intent intent) {
        // super.onNewIntent(intent);
        ektp_content.setText("");

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

        // startActivity(intent);

    }

    public void chooseCardTye(String card) {

        switch (card) {
            case mainApp.NFCA: {
                mainApp.setMyMifare(mainApp.getTag());
                displayNFCMessage("");
                break;
            }

            case mainApp.ISODEP: {
                tampilSnack(getString(R.string.error_ektp));
                playNotif("mission_failed.mp3");
                break;
            }
        }
    }
    public  void playNotif(String filename){
        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer player = new MediaPlayer();

        try {
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.start();

    }

    public  void sycncronizeData()  {
        if(internetService.isOnline()){
            //tampilSnack("CONNECTION ONLINE");
            new SyncronizeData(this,nfcViewModel).executeSycn();
        }
        else{
            tampilSnack("TIME OUT PAKAI LOKAL DATABASE");
        }

    }

}
