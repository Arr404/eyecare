package com.total.eyecare.ui.dashboard

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.total.eyecare.classes.BriefInformation

class HomeViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "this is home"
    }

    val text: LiveData<String> = _text
}