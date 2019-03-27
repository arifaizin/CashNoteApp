package com.imastudio.cashnoteapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.imastudio.cashnoteapp.database.RealmHelper;
import com.imastudio.cashnoteapp.model.DataItem;
import com.imastudio.cashnoteapp.model.ResponseInsert;
import com.imastudio.cashnoteapp.retrofit.ApiConfig;
import com.imastudio.cashnoteapp.worker.UpdateWorker;
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    private TextInputEditText edPengeluaran;
    private TextInputEditText edKeterangan;
    private TextInputEditText edTanggal;
    private Button btnUpdate;
    private Button btnDelete;
    private String dataId;

    RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        realmHelper = new RealmHelper(this);

        //terima data
        dataId = getIntent().getStringExtra(NoteAdapter.DATA_IDE);
        String dataKeterangan = getIntent().getStringExtra(NoteAdapter.DATA_KETERANGAN);
        String dataPengeluaran = getIntent().getStringExtra(NoteAdapter.DATA_PENGELUARAN);
        String dataTanggal = getIntent().getStringExtra(NoteAdapter.DATA_TANGGAL);

        initView();

        edTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragmentDialog dialog = DatePickerFragmentDialog.newInstance(new DatePickerFragmentDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerFragmentDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar kalender = Calendar.getInstance();
                        kalender.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        edTanggal.setText(format.format(kalender.getTime()));
                    }
                });

                dialog.show(getSupportFragmentManager(), "tag");
            }
        });

        edKeterangan.setText(dataKeterangan);
        edPengeluaran.setText(dataPengeluaran);
        edTanggal.setText(dataTanggal);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    updateData();
                } else {
                    updateDataOffline();
                    updateDataWorker();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    deleteData();
                } else {
                    deleteDataOffline();
                }
            }
        });

    }

    private void updateDataWorker() {
        WorkManager manager = WorkManager.getInstance();

        Data.Builder data = new Data.Builder();
        data.putString(NoteAdapter.DATA_IDE, dataId);
        data.putString(NoteAdapter.DATA_KETERANGAN, edKeterangan.getText().toString());
        data.putString(NoteAdapter.DATA_PENGELUARAN, edPengeluaran.getText().toString());
        data.putString(NoteAdapter.DATA_TANGGAL, edTanggal.getText().toString());
        Data newdata = data.build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(UpdateWorker.class)
                .setInputData(newdata)
                .setConstraints(constraints)
                .build();

        manager.enqueue(request);
    }

    private void deleteDataOffline() {
        realmHelper.deleteOneData(dataId);
        finish();
    }

    private void updateDataOffline() {
        DataItem dataItem = new DataItem();
        dataItem.setId(dataId);
        dataItem.setKeterangan(edKeterangan.getText().toString());
        dataItem.setPengeluaran(edPengeluaran.getText().toString());
        dataItem.setTanggal(edTanggal.getText().toString());

        realmHelper.updateData(dataItem);
        finish();

    }

    private void deleteData() {
        Call<ResponseInsert> rekues = ApiConfig.getApiService().deleteData(
                dataId
        );

        rekues.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getResult().equals("true")){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Response Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateData() {
        Call<ResponseInsert> rekues = ApiConfig.getApiService().updateData(
                dataId,
                edKeterangan.getText().toString(),
                edPengeluaran.getText().toString(),
                edTanggal.getText().toString()
        );

        rekues.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getResult().equals("true")){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Response Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        edPengeluaran = (TextInputEditText) findViewById(R.id.ed_pengeluaran);
        edKeterangan = (TextInputEditText) findViewById(R.id.ed_keterangan);
        edTanggal = (TextInputEditText) findViewById(R.id.ed_tanggal);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info!= null && info.isConnected()){
            return true;
        } else {
            return false;
        }

    }


}
