package com.imastudio.cashnoteapp.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseNote{

	@SerializedName("msg")
	private String msg;

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private List<DataItem> data;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setData(List<DataItem> data){
		this.data = data;
	}

	public List<DataItem> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"ResponseNote{" + 
			"msg = '" + msg + '\'' + 
			",result = '" + result + '\'' + 
			",data = '" + data + '\'' + 
			"}";
		}
}