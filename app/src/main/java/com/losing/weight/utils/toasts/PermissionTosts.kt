package com.losing.weight.utils.toasts

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.losing.weight.App
import com.losing.weight.R

object PermissionTosts {
    fun showDeniedPhoto(context: Context){
        var layout = LayoutInflater.from(context).inflate(R.layout.toast_denied, null)
        var toast = Toast(App.getInstance())
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}