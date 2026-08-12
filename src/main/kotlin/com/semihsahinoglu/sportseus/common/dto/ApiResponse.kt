package com.semihsahinoglu.sportseus.common.dto

data class ApiResponse<Type>(
    val success: Boolean,
    val data: Type
) {
    companion object {
        fun <Type> success(data: Type) = ApiResponse(true, data)

        fun <Type> fail(data: Type) = ApiResponse(false, data)
    }
}
