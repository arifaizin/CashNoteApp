package com.imastudio.cashnoteapp.retrofit;

import com.imastudio.cashnoteapp.model.ResponseInsert;
import com.imastudio.cashnoteapp.model.ResponseNote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServices {
    @GET("show_cash_note.php")
    Call<ResponseNote> tampilDatacatatan();

    //post
    @FormUrlEncoded
    @POST("add_note.php")
    Call<ResponseInsert> tambahData(
         @Field("keterangan") String keterangan,
         @Field("pengeluaran") String pengeluaran,
         @Field("tanggal") String tanggal
    );

    @FormUrlEncoded
    @POST("update_note.php")
    Call<ResponseInsert> updateData(
            @Field("id") String id,
            @Field("keterangan") String keterangan,
            @Field("pengeluaran") String pengeluaran,
            @Field("tanggal") String tanggal
    );

    @FormUrlEncoded
    @POST("delete_note.php")
    Call<ResponseInsert> deleteData(
            @Field("id") String id
    );

}
