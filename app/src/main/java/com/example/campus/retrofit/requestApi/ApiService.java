package com.example.campus.retrofit.requestApi;

import com.example.campus.protoModel.AccessUserMessage;
import com.example.campus.protoModel.BookDealHome;
import com.example.campus.protoModel.CourseContain;
import com.example.campus.protoModel.CategoryResponseOuterClass;
import com.example.campus.protoModel.CourseDetail;
import com.example.campus.protoModel.FriendsList;
import com.example.campus.protoModel.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tutorial.CourseDetailCreate;
import tutorial.ResponseOuterClass;


public interface ApiService {

    @GET("courseMessage/index")
    Call<CategoryResponseOuterClass.CategoryResponse> getCategoryView();

    @POST("courseMessage/detail")
    Call<CourseDetail.CourseDetailResponse> getCourseDetail(@Body CourseDetail.CourseDetailRequest request);

    @POST("courseMessage/courseContent")
    Call<CourseContain.CategoryContainResponse> getCourseContain(@Body CourseContain.CategoryContainRequest categoryContainRequest);

    @POST("bookDeal/homeBook")
    Call<BookDealHome.BookDealResponse> getCommodity(@Body BookDealHome.BookDealRequest request);

    @POST("account/login")
    Call<Login.LoginResponse> loginRequest(@Body Login.LoginRequest request);

    @POST("account/signUp")
    Call<Login.LoginResponse> signUpRequest(@Body Login.LoginRequest request);

    @POST("account/delete")
    Call<Login.LoginResponse> deleteAccountRequest(@Body Login.LoginRequest request);

    @POST("account/editPsw")
    Call<Login.LoginResponse> editPswRequest(@Body Login.LoginRequest request);

    @POST("courseMessage/publish")
    Call<ResponseOuterClass.Response> publishReview(@Body CourseDetailCreate.CourseDetailCreateRequest request);

    @POST("message/accessFriendList")
    Call<FriendsList.FriendsListResponse> accessFriendList(@Body FriendsList.FriendsListRequest request);

    @POST("message/accessUserMsg")
    Call<AccessUserMessage.AccessUserMsgResponse> accessUserMsg(@Field("searchContent") String searchContent);

}
