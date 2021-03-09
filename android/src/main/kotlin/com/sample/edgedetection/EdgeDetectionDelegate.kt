package com.sample.edgedetection

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.sample.edgedetection.processor.TAG
import com.sample.edgedetection.scan.ScanActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class EdgeDetectionDelegate(activity: Activity) : PluginRegistry.ActivityResultListener {

    private var activity: Activity = activity
    private var result: MethodChannel.Result? = null
    private var methodCall: MethodCall? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (null != data && null != data.extras) {
                    Log.e(TAG, "usao1!");
                    val filePath = data.extras!!.getString(SCANNED_RESULT)
                    Log.e(TAG, "usao2: !" + filePath);
                    finishWithSuccess(filePath)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                finishWithSuccess(null)
            }
            return true;
        }

        return false;
    }

    fun OpenCameraActivity(call: MethodCall, result: MethodChannel.Result) {

        if (!setPendingMethodCallAndResult(call, result)) {
            finishWithAlreadyActiveError()
            return
        }

        var intent = Intent(Intent(activity.applicationContext, ScanActivity::class.java))
        activity.startActivityForResult(intent,REQUEST_CODE)
    }

    private fun setPendingMethodCallAndResult(methodCall: MethodCall, result: MethodChannel.Result): Boolean {
        if (this.result != null) {
            return false
        }

        this.methodCall = methodCall
        this.result = result
        return true
    }

    private fun finishWithAlreadyActiveError() {
        finishWithError("already_active", "Edge detection is already active")
    }

    private fun finishWithError(errorCode: String, errorMessage: String) {
        result?.error(errorCode, errorMessage, null)
        clearMethodCallAndResult()
    }

    private fun finishWithSuccess(imagePath: String?) {
        Log.e(TAG, "uspjesno zavrsavam!");
        result?.success(imagePath)
        Log.e(TAG, "zavrsio!");
        clearMethodCallAndResult()
    }

    private fun clearMethodCallAndResult() {
        methodCall = null
        result = null
    }

}