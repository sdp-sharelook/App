package com.github.sdpsharelook.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class PermissionManager(
    private val permission: String,
    caller: ActivityResultCaller
) {
    private lateinit var onGrantedCallback: () -> Unit
    private lateinit var onDeniedCallback: () -> Unit
    private lateinit var context: Context

    private val requestPermissionLauncher =
        caller.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // retry
                onGrantedCallback()
            } else {
                onDeniedCallback()
            }
        }

    private fun checkAndGrant() {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
            onGrantedCallback()
        else requestPermissionLauncher.launch(
            permission
        )
    }

    fun grantPermission(context: Context, onGranted: () -> Unit, onDenied: () -> Unit) {
        onGrantedCallback = onGranted
        onDeniedCallback = onDenied
        this.context = context
        checkAndGrant()
    }

    fun grantPermission(context: Context, onGranted: () -> Unit) =
        grantPermission(context, onGranted) {}
}