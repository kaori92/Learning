package com.example.learningnew.workmanager

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.learningnew.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

class DownloadWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        startForegroundService()
        delay(5000)
        val response = FileApi.instance.downloadImage()
        response.body()?.let { body ->
            return withContext(Dispatchers.IO) {
                val file = File(context.cacheDir, "image.jpg")
                val outputStream = FileOutputStream(file)
                outputStream.use { stream ->
                    try {
                        stream.write(body.bytes())
                    } catch(e: IOException) {
                        return@withContext Result.failure(
                            workDataOf(
                                WorkerKeys.ERROR_MSG to e.localizedMessage
                            )
                        )
                    }
                }
                Result.success(
                    workDataOf(
                        WorkerKeys.IMAGE_URI to file.toUri().toString()
                    )
                )
            }

        }
        if(!response.isSuccessful){
            if(response.code().toString().startsWith("5")) {
                return Result.retry()
            }
            return Result.failure(
                workDataOf(
                    WorkerKeys.ERROR_MSG to "NetworkError"
                )
            )
        }
        return Result.failure(
            workDataOf(
                WorkerKeys.ERROR_MSG to "Unknown error"
            )
        )
    }

    private suspend fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val foregroundInfo = ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "download channel")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText("Downloading...")
                    .setContentTitle("Download in progress")
                    .build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
            setForeground(foregroundInfo)
        } else {
            setForeground(
                ForegroundInfo(
                    Random.nextInt(),
                    NotificationCompat.Builder(context, "download channel")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText("Downloading...")
                        .setContentTitle("Download in progress")
                        .build()
                )
            )
        }
    }
}