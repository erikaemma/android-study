package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R

private const val TAG = "BlurWorker"

// BlurViewModel
// Worker：后台执行的实际工作的代码
// 对图片进行模糊处理的Worker
class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext

        // 从Data获取URI
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        // 显示状态通知
        makeStatusNotification("Blurring image", appContext)

        // 减慢工作速度，以便轻松地查看每个WorkRequest的启动情况
        sleep()

        return try {
            /*
            // 此处硬编码
            // 利用纸杯蛋糕图片创建一个Bitmap
            // 对res/drawable/android_cupcake.png图片模糊处理
            val picture = BitmapFactory.decodeResource(
                appContext.resources,
                R.drawable.android_cupcake)
            */

            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(TAG, "Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            // 从用户图片的URI获取图片
            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri))
            )

            // 通过从WorkerUtils调用blurBitmap方法，获取此位图模糊处理后的版本
            val output = blurBitmap(picture, appContext)

            // 从WorkerUtils调用writeBitmapToFile方法，将该位图写入临时文件。
            // 将返回的URI保存到局部变量。
            val outputUri = writeBitmapToFile(appContext, output)

            // 从WorkerUtils调用makeStatusNotification方法
            makeStatusNotification("Output is $outputUri", appContext)

            // 返回
            //Result.success()

            // 创建新的Data
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
            // 返回输出数据的URI
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}