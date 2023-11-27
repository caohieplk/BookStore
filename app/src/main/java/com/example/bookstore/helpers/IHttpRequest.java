package com.example.bookstore.helpers;

import com.example.bookstore.dto.BannerResponse;
import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.CategoryResponse;
import com.example.bookstore.dto.ListNotificationResponeDTO;
import com.example.bookstore.dto.LoginRequestDTO;
import com.example.bookstore.dto.LoginResponseDTO;
import com.example.bookstore.dto.RegisterRequestDTO;
import com.example.bookstore.dto.RegisterResponseDTO;
import com.example.bookstore.dto.SearchBook;
import com.example.bookstore.dto.SearchBookResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface IHttpRequest {

    @POST("/api/Register.php")
    Call<RegisterResponseDTO> register(@Body RegisterRequestDTO body);

    @POST("/api/Login.php")
    Call<LoginResponseDTO> login(@Body LoginRequestDTO body);


    @GET("/api/get-list-banner.php")
    Call<BannerResponse> getBannerBook();

    //lấy danh sách category
    @GET("/api/get-list-category.php")
    Call<CategoryResponse> getListCategory();

    //lấy danh sách book
    @GET("/api/get-list-book.php")
    Call<BookResponse> getListBook();


    //api tìm kiếm sách theo tên sách
    @POST("/api/search-book.php")
    Call<SearchBookResponse> searchBook(@Body SearchBook searchBook);

    @GET("/api/Get_all_thongbao_new.php")
    Call<ListNotificationResponeDTO> getNotification();

    @GET("/api/Get_all_thongbao_new.php")
    Call<ListThongbaoResponeDTO> getThongBao();

}
