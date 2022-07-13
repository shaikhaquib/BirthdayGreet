package com.dis.designeditor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class HeaderModel {

    @SerializedName("header_images")
    @Expose
    private List<HeaderImage> headerImages = null;

    public List<HeaderImage> getHeaderImages() {
        return headerImages;
    }

    public void setHeaderImages(List<HeaderImage> headerImages) {
        this.headerImages = headerImages;
    }

}
