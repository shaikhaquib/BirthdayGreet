package com.dis.designeditor.model;

import com.google.gson.annotations.SerializedName;

public class FooterImagesItem{

	@SerializedName("imgLink")
	private String imgLink;

	@SerializedName("id")
	private String id;

	public void setImgLink(String imgLink){
		this.imgLink = imgLink;
	}

	public String getImgLink(){
		return imgLink;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"FooterImagesItem{" + 
			"imgLink = '" + imgLink + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}