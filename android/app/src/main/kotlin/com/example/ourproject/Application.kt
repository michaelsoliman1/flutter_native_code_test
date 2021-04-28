package com.example.ourproject

import android.app.Application
import android.graphics.Color
import com.verifykit.sdk.VerifyKit
import com.verifykit.sdk.VerifyKitOptions
import com.verifykit.sdk.VerifyKitTheme

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        val theme = VerifyKitTheme(
                backgroundColor = Color.WHITE,
                toolbarTitle = "Test"
        )

        VerifyKit.init(
                this,
                VerifyKitOptions(
                        isLogEnabled = true,
                        verifyKitTheme = theme
                )
        )

    }
}