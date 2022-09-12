package com.total.eyecare

public interface OnItemClickListener<T> {
    fun onItemClick (position : Int, item : T)
}