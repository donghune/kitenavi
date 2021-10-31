package com.github.donghune.kitenavi.model

enum class AddressType(
    val korName : String
) {
    DEPARTURE("출발지"),
    DESTINATION("도착지"),
    STOPOVER("경유지")
}
