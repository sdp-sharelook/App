package com.github.sdpsharelook.textDetection

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.sdpsharelook.databinding.ActivityTextDetectionBinding
import com.github.sdpsharelook.databinding.CardSectionBinding
import com.github.sdpsharelook.databinding.PopupBinding

class TextDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextDetectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        // check if we have the permissions if not ask them
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
//            ActivityCompat.requestPermissions(this, Manifest.permission.CAMERA,0)
        }
    }
}