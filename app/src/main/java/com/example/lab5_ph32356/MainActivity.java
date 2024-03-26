package com.example.lab5_ph32356;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    List<DinkModel> drinkList;
    DrinkAdapter drinkAdapter;
    EditText edTimkiem;
    FloatingActionButton floatAdd;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getDrinkList();

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themCongViec();
            }
        });

        edTimkiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = edTimkiem.getText().toString().trim();
                    searchDrink(key);
                    return true;
                }
                return false;
            }
        });
    }

    private void initViews() {
        floatAdd = findViewById(R.id.floatadd);
        recyclerView = findViewById(R.id.rcDunk);
        edTimkiem = findViewById(R.id.edtimkiem);
    }

    private void searchDrink(String key) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        // ... other code ...

        Call<ApiResponse> call = apiService.searchDink(key);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    drinkList = response.body().getData();
                    drinkAdapter = new DrinkAdapter(MainActivity.this, drinkList);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(drinkAdapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });


    }

    private void getDrinkList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<DinkModel>> call = apiService.getSinhVien();

        call.enqueue(new Callback<List<DinkModel>>() {
            @Override
            public void onResponse(Call<List<DinkModel>> call, Response<List<DinkModel>> response) {
                if (response.isSuccessful()) {
                    drinkList = response.body();
                    drinkAdapter = new DrinkAdapter(MainActivity.this, drinkList);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(drinkAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<DinkModel>> call, Throwable t) {
                Log.e("Main", t.getMessage());
            }
        });
    }



    void themCongViec() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.itemadd, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText edtname = view.findViewById(R.id.name);
        Button btnthemm = view.findViewById(R.id.btnthem);

        btnthemm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Thêm");
                builder.setMessage("Bạn có muốn thêm Drink không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ten = edtname.getText().toString().trim();


                        // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                        if (ten.isEmpty() ) {
                            Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Tạo một đối tượng SinhVien mới
                        DinkModel dinks = new DinkModel(ten);

                        // Gửi đối tượng SinhVien lên server
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiService apiService = retrofit.create(ApiService.class);
                        Call<List<DinkModel>> call = apiService.addDink(dinks);
                        call.enqueue(new Callback<List<DinkModel>>() {
                            @Override
                            public void onResponse(Call<List<DinkModel>> call, Response<List<DinkModel>> response) {
                                if (response.isSuccessful()) {
                                    getDrinkList();
                                    Log.e("thanhcong","jdsbfdsgfdg");

                                } else {
                                    Log.e("Thatbai","aaaaaaaaaaaaaa");
                                    Log.e("sinhvien",dinks.toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<java.util.List<DinkModel>> call, Throwable t) {
                                getDrinkList();

                                dialog.dismiss();
                            }


                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

}