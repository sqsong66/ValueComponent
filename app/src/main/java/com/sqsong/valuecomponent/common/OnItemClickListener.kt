package com.sqsong.valuecomponent.common

interface OnItemClickListener<T> {
    fun onItemClick(data: T, position: Int)
}