package com.image.process.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HeaderResponse{

	@SerializedName("All_Details_Of_Client")
	private List<AllDetailsOfClientItem> allDetailsOfClient;

	public void setAllDetailsOfClient(List<AllDetailsOfClientItem> allDetailsOfClient){
		this.allDetailsOfClient = allDetailsOfClient;
	}

	public List<AllDetailsOfClientItem> getAllDetailsOfClient(){
		return allDetailsOfClient;
	}

	@Override
 	public String toString(){
		return 
			"HeaderResponse{" + 
			"all_Details_Of_Client = '" + allDetailsOfClient + '\'' + 
			"}";
		}
}