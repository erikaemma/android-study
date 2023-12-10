package com.bignerdranch.android.photogallery.api

import com.bignerdranch.android.photogallery.FlickrResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrApi {

    @GET(//api_key对应密码：9a4303b7b2a4b627
        "services/rest/?method=flickr.interestingness.getList" +
        "&api_key=63e8607cbfd17dd3e195226f1d74e5cd" +
        "&format=json" +
        "&nojsoncallback=1" +
        "&extras=url_s"
    )
    //@GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchPhotos(): Call<FlickrResponse>

    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>

    @GET(
        "services/rest/?method=flickr.photos.search" +
        "&api_key=63e8607cbfd17dd3e195226f1d74e5cd" +
        "&format=json" +
        "&nojsoncallback=1" +
        "&extras=url_s"
    )
    //@GET("services/rest/?method=flickr.photos.search")
    fun searchPhotos(@Query("text") query: String): Call<FlickrResponse>

}