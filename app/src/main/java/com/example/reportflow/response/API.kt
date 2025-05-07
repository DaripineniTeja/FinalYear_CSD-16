package com.example.reportflow.response

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface API {

    @FormUrlEncoded
    @POST("entries.php")
    fun entryRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("mobile") mobile: String,
        @Field("type") type: String,
        @Field("role") role: String,
        @Field("location") location: String,
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("uploadRequest.php")
    fun submitComplaint(
        @Field("userId") userId: String,
        @Field("userName") userName: String,
        @Field("userMobile") userMobile: String,
        @Field("description") description: String,
        @Field("imageUri") imageUri: String,
        @Field("location") location: String,
        @Field("timeStamp") timeStamp: String,
        @Field("status") status: String,
        @Field("type") type: String
    ): Call<CommonResponse>


    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("updateRequest.php")
    fun updateStatus(
        @Field("id") id: Int,
        @Field("workerId") workerId: String,
        @Field("workerName") workerName: String,
        @Field("workerMobile") workerMobile: String,
        @Field("status") status: String,
    ): Call<LoginResponse>


    @GET("viewcontent.php")
    fun getContent(): Call<LoginResponse>

    @GET("getEntries.php")
    fun getRole(
        @Query("role") role: String,
    ): Call<CommonResponse>

    @GET("getComplaintRequest.php")
    fun getComplaints(): Call<CommonResponse>
}