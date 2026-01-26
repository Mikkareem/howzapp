package com.techullurgy.howzapp.common.utils.models

class DataErrorException(
    val error: DataError
) : Exception()