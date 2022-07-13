package com.dis.designeditor.model;

import com.google.gson.annotations.Expose;

public class FavouriteModel {
    @Expose
    private Boolean error;
    @Expose
    private String errormsg;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
}
