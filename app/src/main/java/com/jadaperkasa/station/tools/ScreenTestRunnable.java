package com.jadaperkasa.station.tools;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jadaperkasa.station.R;
import com.jadaperkasa.station.common.mainApp;
import com.jadaperkasa.station.ui.MachineTest;


import java.lang.ref.WeakReference;

public class ScreenTestRunnable implements Runnable {
    private WeakReference<AppCompatActivity> mActivity;
    private int[] colors ;
    private Handler myHandler;
    private int i=0;
    private TextView tv;
    private AlertDialog dialogcreate ;
    private Button button;
    private boolean layar_status;

    public ScreenTestRunnable(AppCompatActivity mActivity, int[] colors, Handler myHandler) {
        this.colors  = colors;
        this.myHandler = myHandler;
        this.mActivity = new WeakReference<>(mActivity);
    }
    @Override

    public void run() {
        AppCompatActivity activity = mActivity.get();
        if (activity != null) {
            ConstraintLayout overlay = activity.findViewById(R.id.mainContainer);
            LinearLayout myLayout = activity.findViewById(R.id.layout_container);

            overlay.setBackgroundColor(colors[i]);
            //Log.d(TAG, "Run test count: " + count);

            if (i++ < colors.length - 1) {

                myHandler.postDelayed(this, 2500);
            } else {
                tv = activity.findViewById(R.id.screen_content);
                button = activity.findViewById(R.id.next);
                myLayout.setVisibility(View.VISIBLE);
                dialogConfirmasi(mActivity.get());

            }
        }

    }




    public void dialogConfirmasi(Context context) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("CEK LAYAR");
        dialog.setMessage("LAYAR BERFUNGSI DENGAN BAIK?");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.ic_warning);



        dialog.setPositiveButton("YA", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogcreate.dismiss();
                tv.setText("[ "+mainApp.SUCCEED+" ] OK");
                setLayar_status(true);
                button.setEnabled(true);
            }
        });

        dialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogcreate.dismiss();
                tv.setText("[ "+mainApp.FAILED+" ] TIDAK OK");
                tv.setTextColor(Color.RED);
                setLayar_status(false);
                button.setText("REPORT AND EXIT");
                button.setEnabled(true);
            }
        });

        dialogcreate = dialog.create();
        if (!dialogcreate.isShowing()){
            dialogcreate.show();
        }


    }


    public boolean getLayar_status() {
        return layar_status;
    }

    public void setLayar_status(boolean layar_status) {
        this.layar_status = layar_status;
    }
}
