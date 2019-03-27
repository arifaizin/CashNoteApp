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
import com.shagi.materialdatepicker.date.DatePickerFragmentDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahDataActivity extends AppCompatActivity {

    private TextInputEditText edPengeluaran;
    private TextInputEditText edKeterangan;
    private TextInputEditText edTanggal;
    private Button btnPost;
    private RealmHelper realmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);
        initView();

        realmHelper = new RealmHelper(this);

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

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline()) {
                    addData();
                } else {
                    addDataOffline();
                }
            }
        });
    }

    private void addDataOffline() {
        DataItem data = new DataItem();
        data.setKeterangan(edKeterangan.getText().toString());
        data.setPengeluaran(edPengeluaran.getText().toString());
        data.setTanggal(edTanggal.getText().toString());
        realmHelper.insertData(data);
    }

    private void addData() {
        Call<ResponseInsert> rekues = ApiConfig.getApiService().tambahData(
                edKeterangan.getText().toString(),
                edPengeluaran.getText().toString(),
                edTanggal.getText().toString()
        );

        rekues.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.isSuccessful()){
                    Toast.makeText(TambahDataActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getResult().equals("true")){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(TambahDataActivity.this, "Response Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        edPengeluaran = (TextInputEditText) findViewById(R.id.ed_pengeluaran);
        edKeterangan = (TextInputEditText) findViewById(R.id.ed_keterangan);
        edTanggal = (TextInputEditText) findViewById(R.id.ed_tanggal);
        btnPost = (Button) findViewById(R.id.btnPost);
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
