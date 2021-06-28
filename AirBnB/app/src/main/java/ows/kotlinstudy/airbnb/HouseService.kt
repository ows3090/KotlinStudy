package ows.kotlinstudy.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/2f8162db-37bb-4f01-80e5-cf5e9a303070")
    fun getHouseList(): Call<HouseDto>
}