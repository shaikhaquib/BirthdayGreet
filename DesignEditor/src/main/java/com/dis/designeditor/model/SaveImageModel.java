
package com.dis.designeditor.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SaveImageModel {

    @Expose
    private Boolean error;
    @Expose
    private String fileUrl;
    @SerializedName("Flag")
    private String flag;
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
