package com.famoco.barcodelasersdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * Project : PX320 Barcode Wrapper
 * A basic wrapper to hide the use of intents and easily switch
 * between barcode and camera modes
 */
public class BarcodeLaserSdk {

    /**
     * Constant to define the keycode of the side button to trigger the laser
     */
    final static public int KEY_LASER = 280;
    final static String TAG = BarcodeLaserSdk.class.getSimpleName();
    private Context mContext;
    private IntentFilter mIntentFilter;
    private IntentFilter mCameraReleasedIntentFilter;
    private static BarcodeLaserSdk INSTANCE = null;
    private static BarcodeResultInterface mBarcodeInterface = null;

    /**
     * @param context of activity
     * @return A singleton that will allow to control the laser
     */
    public static synchronized BarcodeLaserSdk getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BarcodeLaserSdk(context);
        }
        return INSTANCE;
    }


    private BarcodeLaserSdk(Context context) {
        this.mContext = context;
        // Defining Broadcast receiver to receive and process intent that carries the value of the barcode
        BroadcastReceiver scanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String inputString = intent.getExtras().getString("com.zebra.sdl.extra.CONTENT");
                if (mBarcodeInterface != null) {
                    mBarcodeInterface.getResult(inputString);
                }
            }

        };

        // Registering broadcast receiver
        if (mIntentFilter == null) {
            mIntentFilter = new IntentFilter("com.zebra.sdl.action.SCAN_COMPLETE");
            this.mContext.registerReceiver(scanReceiver, mIntentFilter);
        }

        // Broadcast receiver to receive and process intent that carries information that camera has been released
        BroadcastReceiver cameraReleasedBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "Intent com.zebra.sdl.action.RELEASED received");
                if (mBarcodeInterface != null) {
                    mBarcodeInterface.onCameraReleased();
                }
            }
        };

        if (mCameraReleasedIntentFilter == null) {
            mCameraReleasedIntentFilter = new IntentFilter("com.zebra.sdl.action.RELEASED");
            this.mContext.registerReceiver(cameraReleasedBroadcastReceiver, mCameraReleasedIntentFilter);
        }

    }

    /**
     * @param barcodeResultInterface that will allow to retrieve events and the scanned results.
     *                               This function set current barcodeResultInterface
     */
    public void setCurrentListener(final BarcodeResultInterface barcodeResultInterface) {
        mBarcodeInterface = barcodeResultInterface;
    }

    /**
     * Requests access to scanner
     */
    public void enable() {
        Intent intentEnable = new Intent("com.zebra.sdl.action.DISABLE");
        intentEnable.setPackage("com.zebra.sdl");
        intentEnable.setFlags(Intent.FLAG_FROM_BACKGROUND);
        Log.d(TAG, "Calling com.zebra.sdl.action.DISABLE");
        mContext.startService(intentEnable);
    }

    /**
     * Releases scanner
     */
    public void disable() {
        Intent intentDisable = new Intent("com.zebra.sdl.action.ENABLE");
        intentDisable.setPackage("com.zebra.sdl");
        intentDisable.setFlags(Intent.FLAG_FROM_BACKGROUND);
        Log.d(TAG, "Calling com.zebra.sdl.action.ENABLE");
        mContext.startService(intentDisable);
    }

    /**
     * Release the camera so that other photo applications can work
     */
    public void releaseCamera() {
        /*Intent intentEnable = new Intent("com.zebra.sdl.action.DISABLE");
        intentEnable.setPackage("com.zebra.sdl");
        intentEnable.setFlags(Intent.FLAG_FROM_BACKGROUND);
        mContext.startService(intentEnable);

        Intent intentDisable = new Intent("com.zebra.sdl.action.ENABLE");
        intentDisable.setPackage("com.zebra.sdl");
        intentDisable.setFlags(Intent.FLAG_FROM_BACKGROUND);
        mContext.startService(intentDisable);*/

        Intent intentEnable = new Intent("com.zebra.sdl.action.RELEASE");
        intentEnable.setPackage("com.zebra.sdl");
        intentEnable.setFlags(Intent.FLAG_FROM_BACKGROUND);
        Log.d(TAG, "Calling com.zebra.sdl.action.RELEASE");
        mContext.startService(intentEnable);
    }

    /**
     * Turn on the laser to allow scanning. The first launch will systematically take about 1sec.
     */
    public void start(Bundle parameters) {
        Intent intentStart = new Intent("com.zebra.sdl.action.START");
        //TODO add here the parameters
//        Bundle bundle = new Bundle();
//        bundle.putInt("23", 55);
        intentStart.putExtra("com.zebra.sdl.extra.PARAMETERS", parameters);
        intentStart.setPackage("com.zebra.sdl");
        intentStart.setFlags(Intent.FLAG_FROM_BACKGROUND);
        Log.d(TAG, "Calling com.zebra.sdl.action.START");
        this.mContext.startService(intentStart);
    }

    /**
     * Turn off the laser.
     */
    public void stop() {
        Intent intentStop = new Intent("com.zebra.sdl.action.STOP");
        intentStop.setPackage("com.zebra.sdl");
        intentStop.setFlags(Intent.FLAG_FROM_BACKGROUND);
        Log.d(TAG, "Calling com.zebra.sdl.action.STOP");
        this.mContext.startService(intentStop);
    }
}

