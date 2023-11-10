package com.example.bookstore.helpers;

import com.example.bookstore.dto.*;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface IHttpRequest {

    @POST("/api/Register.php")
    Call<RegisterResponseDTO> register(@Body RegisterRequestDTO body);

    @POST("/api/Login.php")
    Call<LoginResponseDTO> login (@Body LoginRequestDTO body );

//    @GET("/api/Get_all_notification.php")
//    Call<ListNotificationResponeDTO> getNotification();

    @GET("/api/Get_all_thongbao_new.php")
    Call<ListNotificationResponeDTO> getNotification();

    @GET("/api/Get_all_thongbao_new.php")
    Call<ListThongbaoResponeDTO> getThongBao();

}
