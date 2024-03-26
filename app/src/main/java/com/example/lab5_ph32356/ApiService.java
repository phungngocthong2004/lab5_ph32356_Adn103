package com.example.lab5_ph32356;

import java.util.List;

        import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.DELETE;
        import retrofit2.http.GET;
        import retrofit2.http.POST;
        import retrofit2.http.PUT;
        import retrofit2.http.Path;
        import retrofit2.http.Query;

public interface ApiService {
  String DOMAIN = "http://10.24.40.72:3000/";

  @GET("/api/list")
  Call<List<DinkModel>> getSinhVien();

  @POST("/api/adddinks")
  Call<List<DinkModel>> addDink(@Body DinkModel dinkModel);

  @PUT("/api/updatedrink/{id}")
  Call<DinkModel> updateDink(@Path("id") String id, @Body DinkModel dinkModel);

  @DELETE("/api/deletedink/{id}")
  Call<DinkModel> deleteDink(@Path("id") String id);

    @GET("api/searchdink")
    Call<ApiResponse> searchDink(@Query("key") String key);
}
