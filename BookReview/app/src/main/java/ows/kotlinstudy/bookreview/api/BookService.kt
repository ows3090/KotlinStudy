package ows.kotlinstudy.bookreview.api

import ows.kotlinstudy.bookreview.model.BestSellerDto
import ows.kotlinstudy.bookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService{
    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keywork: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>

}