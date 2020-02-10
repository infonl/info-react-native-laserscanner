package com.famoco.barcodelasersdk;

/**
 * Project : px320testbarcode
 */
public interface BarcodeResultInterface {

    /**
     * Method to be implemented in order to retrieve the scanned codes
     * @param result
     */
    void getResult(String result);

    void onCameraReleased();
}
