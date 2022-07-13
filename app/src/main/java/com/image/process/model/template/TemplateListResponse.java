package com.image.process.model.template;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TemplateListResponse{

	@SerializedName("BirthdayBaseImageList")
	private List<BirthdayBaseImageListItem> birthdayBaseImageList;

	public void setBirthdayBaseImageList(List<BirthdayBaseImageListItem> birthdayBaseImageList){
		this.birthdayBaseImageList = birthdayBaseImageList;
	}

	public List<BirthdayBaseImageListItem> getBirthdayBaseImageList(){
		return birthdayBaseImageList;
	}

	@Override
 	public String toString(){
		return 
			"TemplateListResponse{" + 
			"birthdayBaseImageList = '" + birthdayBaseImageList + '\'' + 
			"}";
		}
}