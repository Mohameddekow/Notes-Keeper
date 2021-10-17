package com.example.room.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun setupToast(context: Context, message: String){
    Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
}