package com.imastudio.cashnoteapp.worker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.imastudio.cashnoteapp.MainActivity;
import com.imastudio.cashnoteapp.NoteAdapter;
import com.imastudio.cashnoteapp.UpdateActivity;
import com.imastudio.cashnoteapp.model.ResponseInsert;
import com.imastudio.cashnoteapp.retrofit.ApiConfig;

import java.beans.IndexedPropertyChangeEvent;

import androidx.work.ListenableWorker.Result;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateWorker extends Worker {

   private Context context;
    public UpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            String dataId = getInputData().getString(NoteAdapter.DATA_IDE);
            String dataKeterangan = getInputData().getString(NoteAdapter.DATA_KETERANGAN);
            String dataPengeluaran = getInputData().getString(NoteAdapter.DATA_PENGELUARAN);
            String dataTanggal = getInputData().getString(NoteAdapter.DATA_TANGGAL);

            Call<ResponseInsert> rekues = ApiConfig.getApiService().updateData(
                    dataId,
                    dataKeterangan,
                    dataPengeluaran,
                    dataTanggal
            );

            rekues.enqueue(new Callback<ResponseInsert>() {
                @Override
                public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        if (response.body().getResult().equals("true")){
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseInsert> call, Throwable t) {
                    Toast.makeText(context, "Response Failure", Toast.LENGTH_SHORT).show();
                }
            });

            return Result.success();
        } catch (Exception e){
            return Result.failure();
        }
    }
}
