package com.github.donghune.kitenavi.model.response

data class AddressSearchResponse(
    val documents: List<Document>,
    val meta: Meta
)

data class Document(
    val address: Address,
    val address_name: String,
    val address_type: String,
    val road_address: RoadAddress,
    val x: Double,
    val y: Double
)

data class Address(
    val address_name: String,
    val b_code: String,
    val h_code: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_h_name: String,
    val region_3depth_name: String,
    val sub_address_no: String,
    val x: Double,
    val y: Double
)

data class RoadAddress(
    val address_name: String,
    val building_name: String,
    val main_building_no: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val sub_building_no: String,
    val underground_yn: String,
    val x: Double,
    val y: String,
    val zone_no: String
)

data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)
