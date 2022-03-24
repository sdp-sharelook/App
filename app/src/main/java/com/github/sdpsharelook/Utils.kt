package com.github.sdpsharelook

import android.content.Context
import android.widget.Toast

class Utils {
    companion object {
        fun toast(message: String, ctx: Context) {
            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
        }
    }
}