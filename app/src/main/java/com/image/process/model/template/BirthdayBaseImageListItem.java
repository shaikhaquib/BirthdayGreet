package com.image.process.model.template;

import com.google.gson.annotations.SerializedName;

public class BirthdayBaseImageListItem{

	@SerializedName("BirthdayImage")
	private String birthdayImage;

	@SerializedName("BirthdayCd")
	private String birthdayCd;

	public void setBirthdayImage(String birthdayImage){
		this.birthdayImage = birthdayImage;
	}

	public String getBirthdayImage(){
		return birthdayImage;
	}

	public void setBirthdayCd(String birthdayCd){
		this.birthdayCd = birthdayCd;
	}

	public String getBirthdayCd(){
		return birthdayCd;
	}

	@Override
 	public String toString(){
		return 
			"BirthdayBaseImageListItem{" + 
			"birthdayImage = '" + birthdayImage + '\'' + 
			",birthdayCd = '" + birthdayCd + '\'' + 
			"}";
		}
}