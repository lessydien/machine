package com.jadaperkasa.station.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.jadaperkasa.station.R;
import com.jadaperkasa.station.common.mainApp;
import com.jadaperkasa.station.service.json.MachineObject;
import com.jadaperkasa.station.service.json.responseUpload;
import com.jadaperkasa.station.service.repository.RestRepo;
import com.jadaperkasa.station.tools.InternetService;
import com.jadaperkasa.station.tools.ScreenTestRunnable;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MachineTest extends AppCompatActivity {

    private boolean writeSetting;
    private LinearLayout myLayout;
    private ConstraintLayout overLayout;
    private TypedArray warna;
    private Button nextButton;
    private TextView batt_txt;
    private AudioManager audioManager;
    private AlertDialog dialogcreate;
    private TextView audio_txt;
    private String ektp;
    private String nama;
    private SharedPreferences myshare;
    private SharedPreferences.Editor prefEdit;
    private int batt;
    private boolean layar_status=false;
    private boolean suara_status=false;
    private ScreenTestRunnable runnable;
    private LayoutInflater inflater;
    private View dialogView;
    private TextView keterangan;
    private String ket_text;
    private TelephonyManager telephonyManager;
    private String my_IMEI;
    private Snackbar mysnack;
    private boolean status_pinjam;
    private AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.machine_test);
        myLayout = findViewById(R.id.layout_container);
        overLayout = findViewById(R.id.mainContainer);
        nextButton = findViewById(R.id.next);
        batt_txt = findViewById(R.id.batt_content);
        audio_txt = findViewById(R.id.sound_content);
        nextButton.setEnabled(false);
        myshare = getSharedPreferences(mainApp.MY_SHARE, MODE_PRIVATE);
        status_pinjam = myshare.getBoolean(mainApp.MESIN_DIPINJAM,false);

        if(!status_pinjam){
            setTitle(getString(R.string.mesin)+ " " + getString(R.string.login_CAPS));

        }
        else {
            setTitle(getString(R.string.mesin)+" "+ getString(R.string.logout_CAPS));

        }

        warna = getResources().obtainTypedArray(R.array.test_color);
        TextView tv = findViewById(R.id.screen_content);
        tv.setText("");

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
               // return;

                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_PHONE_STATE}, 100);
            }
            else {
                my_IMEI = telephonyManager.getDeviceId();
            }
        }

        getBatteray();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            writeSetting = Settings.System.canWrite(this);
        }

        if(!writeSetting){
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS), mainApp.SETTING_PER);
        }
        else {

            getAudio();
          //  getScreen();
        }
    }



    private void changeScreenBrightness(Context context)
    {

//        int screenB = 0;
//        try {
//            screenB = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Settings.System.putInt(context.getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
//        for(int i=0;i<255;i=i+20) {
//            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, i);
//            try {FIELD_NAME
//                Thread.sleep(500);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, screenB);


        // ----------------------- END -----------

        final int[] colors = new int[warna.length()+1];
        for(int i=0;i<warna.length();i++) {
            colors[i] = warna.getColor(i,i);
        }
        colors[warna.length()] = getResources().getColor(R.color.mainbackground);
        warna.recycle();


        final Handler handler = new Handler(Looper.getMainLooper());

        runnable = new ScreenTestRunnable(this,colors,handler) ;
        handler.post(runnable);



    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
            my_IMEI = telephonyManager.getDeviceId();

        }

    }



    public void getBatteray(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;
        float batteryPctpersen = batteryPct*100;
        batt = (int)batteryPctpersen;
        String status="";
        if (batt > 60){
            status = " [ OK ]";
            batt_txt.setTextColor(Color.GREEN);
        }
        else {
            status = " [ NOK ]";
            batt_txt.setTextColor(Color.RED);
        }
        batt_txt.setText("" + batt+ "%" + status);

    }

    public void getAudio(){
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (!audioManager.isMicrophoneMute()){
            tampilSnack("SPEAKER ON");
        }
        int media_max_volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) /4   ;

        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC, // Stream type
                media_max_volume, // Index
                0 // /2Flags
        );

        Uri myAudio = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        MediaPlayer mp =  MediaPlayer.create(MachineTest.this,myAudio);
         mp.start();
         mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mp) {
                 dialogConfirmasi();
             }
         });

    }

    public void dialogConfirmasi() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        inflater = getLayoutInflater();
//        dialogView = inflater.inflate(R.layout.ket_audio, null);
//        dialog.setView(dialogView);
        dialog.setTitle("CEK AUDIO");
        dialog.setMessage("AUDIO BERFUNGSI DENGAN BAIK?");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_warning);


        dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogcreate.dismiss();
                audio_txt.setText("[ "+mainApp.SUCCEED+" ] OK");
                suara_status = true;
                getScreen();

            }
        });

        dialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogcreate.dismiss();
                audio_txt.setText("[ "+mainApp.FAILED+" ] TIDAK OK");
                audio_txt.setTextColor(Color.RED);
                getScreen();

            }
        });

        dialogcreate = dialog.create();
       // dialogcreate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogcreate.setCanceledOnTouchOutside(false);
        if (!dialogcreate.isShowing()){
            dialogcreate.show();
        }


    }

    public void getScreen(){
        if(writeSetting){
            //  myLayout.setVisibility(View.GONE);
              changeScreenBrightness(this);
        }
    }

    public void lanjutKeStation() {

        if(batt < 20 || !layar_status || !suara_status){
//            prefEdit = myshare.edit();
//            prefEdit.putBoolean(mainApp.MESIN_RUSAK,true);
//            prefEdit.apply();
            finish();

        }
        else {
            prefEdit = myshare.edit();
//            prefEdit.putBoolean(mainApp.MESIN_RUSAK,false);

            Intent intent = new Intent(this, StationAuth.class);
            intent.putExtra(mainApp.KET,ket_text);
//            intent.putExtra("nama",nama);
            prefEdit.putInt(mainApp.BATT_LEVEL,batt);
            prefEdit.putBoolean(mainApp.SUARA_STATUS,suara_status);
            prefEdit.putBoolean(mainApp.LAYAR_STATUS,layar_status);
            prefEdit.putString(mainApp.ID_MACHINE,my_IMEI);
            prefEdit.apply();
            startActivityForResult(intent,mainApp.HAL_STA );
        }

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    private void simpanKetMesin() {
       dialog = new AlertDialog.Builder(MachineTest.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.ket_mesin, null);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_edit);
        dialog.setTitle("LAPORAN KONDISI ALAT");


        keterangan  =  dialogView.findViewById(R.id.keterangan);


        dialog.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ket_text    = keterangan.getText().toString();
                InternetService internetService = new InternetService();
                if (internetService.isOnline()) {
                    RestRepo restRepo =  new RestRepo();
                    MachineObject machineObject = new MachineObject(my_IMEI,ket_text,batt,
                                layar_status?1:0,1,suara_status?1:0,1,2);

                    Single<responseUpload> callUpload = restRepo.tambahMesin(machineObject);

                    callUpload.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                            subscribe(
                                    new SingleObserver<responseUpload>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(responseUpload responseUpload) {
                                            tampilSnack("Mesin berhasil diperbaharui!");
                                            lanjutKeStation();


                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            tampilSnack("Gagal diperbaharui!");

                                        }
                                    });


                    dialog.dismiss();
                }
                else{
                    tampilSnack("No Internet");
                }

            }
        });


        dialogcreate = dialog.create();
      //  dialogcreate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (!dialogcreate.isShowing()){
            dialogcreate.show();
        }

    }

    public void tampilSnack(String s) {
        mysnack =  Snackbar.make(findViewById(R.id.top_coordinator),
                s,Snackbar.LENGTH_LONG);
        mysnack.getView().setBackgroundColor(Color.RED);
        mysnack.show();
    }

    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mainApp.HAL_STA ){
            finish();
        }
        else {
            if(requestCode == mainApp.SETTING_PER){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    writeSetting = Settings.System.canWrite(this);
                }

                getAudio();
               // getScreen();
            }

        }

    }

    public void bukaFormKet(View view) {
        layar_status = runnable.getLayar_status();
        simpanKetMesin();
    }


}
