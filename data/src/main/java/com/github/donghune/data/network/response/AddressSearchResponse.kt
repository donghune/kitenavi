package com.github.donghune.data.network.response

import com.github.donghune.domain.entity.Address
import com.google.gson.annotations.SerializedName

data class AddressSearchResponse(
    @SerializedName("documents") val document: List<DocumentResponse>
)

fun DocumentResponse.toAddress(): Address {
    return Address(
        0,
        addressName,
        x,
        y,
        -1
    )
}

data class DocumentResponse(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("x") val x: Double,
    @SerializedName("y") val y: Double
)