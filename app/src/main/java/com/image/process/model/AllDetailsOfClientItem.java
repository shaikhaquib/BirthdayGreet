package com.image.process.model;

import com.google.gson.annotations.SerializedName;

public class AllDetailsOfClientItem{

	@SerializedName("Client_No")
	private String clientNo;

	@SerializedName("Client_Name")
	private String clientName;

	@SerializedName("Party")
	private String party;

	@SerializedName("Header")
	private String header;

	@SerializedName("ElectionName")
	private String electionName;

	@SerializedName("ClientGroup")
	private String clientGroup;

	public void setClientNo(String clientNo){
		this.clientNo = clientNo;
	}

	public String getClientNo(){
		return clientNo;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}

	public String getClientName(){
		return clientName;
	}

	public void setParty(String party){
		this.party = party;
	}

	public String getParty(){
		return party;
	}

	public void setHeader(String header){
		this.header = header;
	}

	public String getHeader(){
		return header;
	}

	public void setElectionName(String electionName){
		this.electionName = electionName;
	}

	public String getElectionName(){
		return electionName;
	}

	public void setClientGroup(String clientGroup){
		this.clientGroup = clientGroup;
	}

	public String getClientGroup(){
		return clientGroup;
	}

	@Override
 	public String toString(){
		return 
			"AllDetailsOfClientItem{" + 
			"client_No = '" + clientNo + '\'' + 
			",client_Name = '" + clientName + '\'' + 
			",party = '" + party + '\'' + 
			",header = '" + header + '\'' + 
			",electionName = '" + electionName + '\'' + 
			",clientGroup = '" + clientGroup + '\'' + 
			"}";
		}
}