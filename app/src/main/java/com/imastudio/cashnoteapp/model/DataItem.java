package com.imastudio.cashnoteapp.model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Generated("com.robohorse.robopojogenerator")
public class DataItem extends RealmObject {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("pengeluaran")
	private String pengeluaran;

	@PrimaryKey
	@SerializedName("id")
	private String id;

	@SerializedName("tanggal")
	private String tanggal;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setPengeluaran(String pengeluaran){
		this.pengeluaran = pengeluaran;
	}

	public String getPengeluaran(){
		return pengeluaran;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}

	public String getTanggal(){
		return tanggal;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"keterangan = '" + keterangan + '\'' + 
			",pengeluaran = '" + pengeluaran + '\'' + 
			",id = '" + id + '\'' + 
			",tanggal = '" + tanggal + '\'' + 
			"}";
		}
}