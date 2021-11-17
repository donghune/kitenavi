package com.github.donghune.kitenavi.model.network.service

import com.github.donghune.kitenavi.model.network.response.AddressSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AddressService {
    @GET("/v2/local/search/address.json")
    suspend fun search(
        @Query("query") query: String,
    ): AddressSearchResponse
}
