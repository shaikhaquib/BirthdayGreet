package com.image.process;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefaultResponse{

	@SerializedName("jsonRes")
	private List<JsonResItem> jsonRes;

	@SerializedName("error")
	private boolean error;

	public void setJsonRes(List<JsonResItem> jsonRes){
		this.jsonRes = jsonRes;
	}

	public List<JsonResItem> getJsonRes(){
		return jsonRes;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"DefaultResponse{" + 
			"jsonRes = '" + jsonRes + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}