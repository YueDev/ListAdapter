package com.example.listadapter

import java.util.UUID


data class PagBean(
    val text: String,
    val color: Int,
    val isSelect: Boolean = false,
    val uuid: String = UUID.randomUUID().toString(),
)

