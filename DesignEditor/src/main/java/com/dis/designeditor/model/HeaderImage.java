package com.dis.designeditor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeaderImage {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imgLink")
    @Expose
    private String imgLink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

}
