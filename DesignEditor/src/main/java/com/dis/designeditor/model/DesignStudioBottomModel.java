
package com.dis.designeditor.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class DesignStudioBottomModel {

    @SerializedName("ClientWiseMaterialList")
    private ArrayList<ClientWiseMaterialList> clientWiseMaterialList;

    public ArrayList<ClientWiseMaterialList> getClientWiseMaterialList() {
        return clientWiseMaterialList;
    }

    public void setClientWiseMaterialList(ArrayList<ClientWiseMaterialList> clientWiseMaterialList) {
        this.clientWiseMaterialList = clientWiseMaterialList;
    }

    public static class ClientWiseMaterialList {

        public ClientWiseMaterialList(){}
        public ClientWiseMaterialList(int favourite, Long iD, String imageURL, String name, Long premiumCodes, String rowNum) {
            this.favourite = favourite;
            this.iD = iD;
            this.imageURL = imageURL;
            this.name = name;
            this.premiumCodes = premiumCodes;
            this.rowNum = rowNum;
        }

        @SerializedName("Favourite")
        private int favourite;
        @SerializedName("ID")
        private Long iD;
        @SerializedName("ImageURL")
        private String imageURL;
        @SerializedName("Name")
        private String name;
        @SerializedName("PremiumCodes")
        private Long premiumCodes;
        @SerializedName("RowNum")
        private String rowNum;

        public int getFavourite() {
            return favourite;
        }

        public void setFavourite(int favourite) {
            this.favourite = favourite;
        }

        public Long getID() {
            return iD;
        }

        public void setID(Long iD) {
            this.iD = iD;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getPremiumCodes() {
            return premiumCodes;
        }

        public void setPremiumCodes(Long premiumCodes) {
            this.premiumCodes = premiumCodes;
        }

        public String getRowNum() {
            return rowNum;
        }

        public void setRowNum(String rowNum) {
            this.rowNum = rowNum;
        }

    }


}
