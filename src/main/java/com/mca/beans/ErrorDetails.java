package com.mca.beans;

import com.opencsv.bean.CsvBindByPosition;

/**
 * Bean to represent error during execution
 */
public class ErrorDetails {

    @CsvBindByPosition(position = 0)
    String din;

    @CsvBindByPosition(position = 1)
    String errorMsg;

    public String getDin() {
        return din;
    }

    public void setDin(String din) {
        this.din = din;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "ErrorDetails [din=" + din + ", errorMsg=" + errorMsg + "]";
    }
}
