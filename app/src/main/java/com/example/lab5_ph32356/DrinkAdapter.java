package com.example.lab5_ph32356;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {

    Context context;
    List<DinkModel> ListSv;

    public DrinkAdapter(Context context, List<DinkModel> listSv) {
        this.context = context;
        ListSv = listSv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.itemdrink,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtname.setText("Name: "+ListSv.get(position).getName());

        holder.imgsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.itemupdate, null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                EditText edtname = view.findViewById(R.id.name);
                Button btnsua = view.findViewById(R.id.btnthem);
                edtname.setText(ListSv.get(position).getName());

                btnsua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Sửa");
                        builder.setMessage("Bạn có muốn sửa Sinh Viên không?");
                        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String id = ListSv.get(position).getId();
                                String ten = edtname.getText().toString().trim();



                                // Kiểm tra các trường thông tin có được nhập đầy đủ hay không
                                if (ten.isEmpty()) {
                                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Tạo một đối tượng SinhVien mới
                                DinkModel dink = new DinkModel(ten);

                                // Gửi đối tượng SinhVien cập nhật lên server
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(ApiService.DOMAIN)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                ApiService apiService = retrofit.create(ApiService.class);
                                Call<DinkModel> call = apiService.updateDink(id, dink);
                                call.enqueue(new Callback<DinkModel>() {


                                    @Override
                                    public void onResponse(Call<DinkModel> call, Response<DinkModel> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            ListSv.get(position).setName(ten);

                                            notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DinkModel> call, Throwable t) {
                                        Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                        Log.e("City_Adapter", "Error updating sinh vien", t);

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
        });
        holder.imgxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String id = ListSv.get(position).getId();

                        // Gửi yêu cầu xóa sinh viên đến máy chủ
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(ApiService.DOMAIN)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        ApiService apiService = retrofit.create(ApiService.class);
                        Call<DinkModel> call = apiService.deleteDink(id);
                        call.enqueue(new Callback<DinkModel>() {


                            @Override
                            public void onResponse(Call<DinkModel> call, Response<DinkModel> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                    ListSv.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DinkModel> call, Throwable t) {

                                Toast.makeText(context, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                                Log.e("City_Adapter", "Error deleting sinh vien", t);

                            }
                        });
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return ListSv.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        ImageView imgsua,imgxoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname=itemView.findViewById(R.id.txtname);
            imgsua=itemView.findViewById(R.id.imgsua);
            imgxoa=itemView.findViewById(R.id.imgxoa);
        }
    }
}
