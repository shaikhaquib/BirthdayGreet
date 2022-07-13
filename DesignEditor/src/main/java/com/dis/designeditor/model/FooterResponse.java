package com.dis.designeditor.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FooterResponse{

	@SerializedName("footer_images")
	private List<FooterImagesItem> footerImages;



	@SerializedName("custom_footer_images")
	private List<FooterImagesItem> customFooterIimages;

	public void setFooterImages(List<FooterImagesItem> footerImages){
		this.footerImages = footerImages;
	}
	public List<FooterImagesItem> getCustomFooterIimages() {
		return customFooterIimages;
	}

	public void setCustomFooterIimages(List<FooterImagesItem> customFooterIimages) {
		this.customFooterIimages = customFooterIimages;
	}
	public List<FooterImagesItem> getFooterImages(){
		return footerImages;
	}

	@Override
 	public String toString(){
		return 
			"FooterResponse{" + 
			"footer_images = '" + footerImages + '\'' +
			"custom_footer_images = '" + customFooterIimages + '\'' +
			"}";
		}
}