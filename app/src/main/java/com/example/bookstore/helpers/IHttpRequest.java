package com.example.bookstore.helpers;

import com.example.bookstore.model.AuthorRequest;
import com.example.bookstore.model.AuthorResponse;
import com.example.bookstore.model.BankInfoResponse;
import com.example.bookstore.model.BannerResponse;
import com.example.bookstore.model.BaseResponse;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.BookResponse;
import com.example.bookstore.model.CategoryResponse;
import com.example.bookstore.model.ChapterRequest;
import com.example.bookstore.model.ChapterResponse;
import com.example.bookstore.model.EditProfileResponse;
import com.example.bookstore.model.FavoriteBookResponse;
import com.example.bookstore.model.ListNotificationResponeDTO;
import com.example.bookstore.model.LoginGoogleRequest;
import com.example.bookstore.model.LoginRequest;
import com.example.bookstore.model.LoginResponse;
import com.example.bookstore.model.LogoutResponse;
import com.example.bookstore.model.RegisterRequest;
import com.example.bookstore.model.RegisterResponse;
import com.example.bookstore.model.ResetPasswordResponse;
import com.example.bookstore.model.SearchBook;
import com.example.bookstore.model.SearchBookResponse;
import com.example.bookstore.model.Transaction;
import com.example.bookstore.model.TransactionResponse;
import com.example.bookstore.model.UserInfo;
import com.example.bookstore.model.CategoryRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface IHttpRequest {

    @POST("/api/Register.php")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("/api/Login.php")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("/api/author-google.php")
    Call<LoginResponse> loginWithGoogle(@Body LoginGoogleRequest body);

    @GET("/api/get-list-banner.php")
    Call<BannerResponse> getBannerBook();

    //lấy danh sách category
    @GET("/api/get-list-category.php")
    Call<CategoryResponse> getListCategory();

    //lấy danh sách book
    @GET("/api/get-list-book.php")
    Call<BookResponse> getListBook();


    //lấy danh sách book theo id category
    @POST("/api/get-list-book-by-id-category.php")
    Call<BookResponse> getListBookByIdCategory(@Body CategoryRequest body);


    //lấy thông tin tác giả
    @POST("/api/get-author.php")
    Call<AuthorResponse> getAuthor(@Body AuthorRequest body);

    //lấy list chapter
    @POST("/api/get-list-chapter.php")
    Call<ChapterResponse> getChapters(@Body ChapterRequest body);

    //lấy list giao dịch Transaction
    @GET("/api/get-list-transaction.php")
    Call<TransactionResponse> getTransactions();

    //tạo 1 transaction mua 1 quyển sách
    @POST("/api/make-transaction.php")
    Call<TransactionResponse> makeTransaction(@Body Transaction body);

    //kiểm tra 1 quyển sách đã thanh toán chưa
    @POST("/api/check-transaction.php")
    Call<BaseResponse> checkTransaction(@Body Transaction body);

    //xoá 1 giao dịch
    @POST("/api/delete-transaction.php")
    Call<BaseResponse> deleteTransaction(@Body Transaction body);


    //lấy list sách favorite
    @GET("/api/get-list-favorite.php")
    Call<FavoriteBookResponse> getFavorites();


    //favorite = 1/ 0: chưa yêu thích
    //bỏ yêu thích 1 cuốn sách
    @POST("/api/check-favorite.php")
    Call<BaseResponse> checkFavorite(@Body Book body);


    //bỏ yêu thích 1 cuốn sách
    @POST("/api/favorite-book.php")
    Call<BaseResponse> favoriteBook(@Body Book body);

    //bỏ yêu thích 1 cuốn sách
    @POST("/api/un-favorite-book.php")
    Call<BaseResponse> unFavorite(@Body Book body);


    //api tìm kiếm sách theo tên sách: "S"
    @POST("/api/search-book.php")
    Call<SearchBookResponse> searchBook(@Body SearchBook searchBook);


    //api lấy thông tin ngân hàng trong màn PaymentFragment để user tự chuyển khoản
    @GET("/api/get-list-bank-info.php")
    Call<BankInfoResponse> getBankInfos();

    //api edit profile
    @POST("/api/edit-profile.php")
    Call<EditProfileResponse> editProfile(@Body UserInfo body);


    //api reset password
    @POST("/api/reset-password.php")
    Call<ResetPasswordResponse> resetPassword(@Body UserInfo body);

    //api log out
    @GET("/api/logout.php")
    Call<LogoutResponse> logout();

    @GET("/api/get_list_notification.php")
    Call<ListNotificationResponeDTO> getNotification();

}
