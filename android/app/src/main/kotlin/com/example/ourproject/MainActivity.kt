package com.example.ourproject
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast

import androidx.annotation.NonNull
import com.verifykit.sdk.VerifyCompleteListener
import com.verifykit.sdk.VerifyKit
import com.verifykit.sdk.VerifyKitOptions
import com.verifykit.sdk.VerifyKitTheme
import com.verifykit.sdk.base.VerifyKitError
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/battery"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()

                if (batteryLevel != -1) {
                    result.success(batteryLevel)
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else if (call.method == "callSendStringFun") {
                showHelloFromFlutter(call.argument("arg"))
                val temp = sendString()
                result.success(temp)
            }else if (call.method == "testVerify") {
                VerifyKit.startVerification(this, object : VerifyCompleteListener {
                    override fun onSuccess(sessionId: String) {
                        showHelloFromFlutter("success_from_verify")
                        result.success(sessionId)
                    }
                    override fun onFail(error: VerifyKitError) {
                        showHelloFromFlutter("fail_from_verify")

                        result.error("UNAVAILABLE", error.message, null)
                    }

                })

            } else {
                result.notImplemented()
            }
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }

    private fun sendString(): String {
        val stringToSend: String = "Hello from Kotlin"
        return stringToSend
    }

    private fun showHelloFromFlutter(argFromFlutter : String?){
        Toast.makeText(this, argFromFlutter, Toast.LENGTH_SHORT).show()
    }




    private fun startVer(){
        VerifyKit.startVerification(this, object : VerifyCompleteListener {
            override fun onSuccess(sessionId: String) {
                // TODO operate SUCCESS process
            }
            override fun onFail(error: VerifyKitError) {
                // TODO operate FAIL process
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        VerifyKit.onActivityResult(requestCode, resultCode, data)
    }

}
