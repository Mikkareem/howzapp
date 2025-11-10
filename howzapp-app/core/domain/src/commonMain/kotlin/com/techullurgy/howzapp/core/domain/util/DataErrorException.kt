package com.techullurgy.howzapp.core.domain.util

class DataErrorException(
    val error: DataError
): Exception()