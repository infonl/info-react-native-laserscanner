package com.reactlibrary;

import android.os.Bundle;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.famoco.barcodelasersdk.BarcodeLaserSdk;
import com.famoco.barcodelasersdk.BarcodeResultInterface;

public class LaserscannerModule extends ReactContextBaseJavaModule implements BarcodeResultInterface {

    private final ReactApplicationContext reactContext;
    private Promise promise;

    public LaserscannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Laserscanner";
    }

    @ReactMethod
    public void scan(Promise promise) {
        try {
            this.promise = promise;
            BarcodeLaserSdk sdk = BarcodeLaserSdk.getInstance(reactContext);
            sdk.setCurrentListener(this);
            Bundle params = new Bundle();
            sdk.start(params);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void stop(Promise promise) {
        try {
            BarcodeLaserSdk sdk = BarcodeLaserSdk.getInstance(reactContext);
            sdk.stop();
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @Override
    public void getResult(String result) {
        if (promise != null) {
            promise.resolve(result);
        }
    }

    @Override
    public void onCameraReleased() {
        this.promise = null;
    }
}
