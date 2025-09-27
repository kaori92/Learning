package com.example.learningnew.workmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import coil.compose.rememberImagePainter
import com.example.learningnew.ui.theme.LearningNewTheme

class WorkManagerActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

//        val downloadRequest = PeriodicWorkRequestBuilder<DownloadWorker>(
//            Duration.ofHours(5)
//        )
		
		val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
			.setConstraints(
				Constraints.Builder()
					.setRequiresStorageNotLow(true)
					.setRequiredNetworkType(NetworkType.CONNECTED)
					.setRequiresBatteryNotLow(true)
					.build()
			)
			.build()
		
		val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWorker>()
			.build()
		val workManager = WorkManager.getInstance(applicationContext)
		
		setContent {
			LearningNewTheme {
				val workInfos = workManager
					.getWorkInfosForUniqueWorkLiveData("download")
					.observeAsState()
					.value
				val downloadInfo = remember(key1 = workInfos) {
					workInfos?.find { it.id == downloadRequest.id }
				}
				val filterInfo = remember(key1 = workInfos) {
					workInfos?.find { it.id == colorFilterRequest.id }
				}
				
				val imageUri = remember { mutableStateOf<android.net.Uri?>(null) }
				
				LaunchedEffect(downloadInfo, filterInfo) {
					when {
						filterInfo?.state == WorkInfo.State.SUCCEEDED -> {
							val uri =
								filterInfo.outputData.getString(WorkerKeys.FILTER_URI)?.toUri()
							if (uri != null) {
								imageUri.value = uri
							}
						}
						
						downloadInfo?.state == WorkInfo.State.SUCCEEDED &&
								filterInfo?.state != WorkInfo.State.SUCCEEDED -> {
							val uri =
								downloadInfo.outputData.getString(WorkerKeys.IMAGE_URI)?.toUri()
							if (uri != null) {
								imageUri.value = uri
							}
						}
						
						else -> {
						}
					}
				}
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					imageUri.value?.let { uri ->
						Image(
							painter = rememberImagePainter(
								data = uri,
							),
							contentDescription = null
						)
					}
					
					Spacer(modifier = Modifier.height(16.dp))
					Button(
						onClick = {
							workManager
								.beginUniqueWork(
									"download",
									ExistingWorkPolicy.KEEP,
									downloadRequest
								)
								.then(colorFilterRequest)
								.enqueue()
						},
						enabled = downloadInfo?.state != WorkInfo.State.RUNNING
					) {
						Text("Start download")
					}
					Spacer(modifier = Modifier.height(16.dp))
					
					when (downloadInfo?.state) {
						WorkInfo.State.RUNNING -> Text("Downloading...")
						WorkInfo.State.SUCCEEDED -> Text("Download succeeded ")
						WorkInfo.State.FAILED -> Text("Download failed")
						WorkInfo.State.CANCELLED -> Text("Download cancelled")
						WorkInfo.State.ENQUEUED -> Text("Download enqueued")
						WorkInfo.State.BLOCKED -> Text("Download blocked")
						null -> Text("Download null")
					}
					Spacer(modifier = Modifier.height(16.dp))
					when (filterInfo?.state) {
						WorkInfo.State.RUNNING -> Text("Applying filter...")
						WorkInfo.State.SUCCEEDED -> Text("Filter succeeded ")
						WorkInfo.State.FAILED -> Text("Filter failed")
						WorkInfo.State.CANCELLED -> Text("Filter cancelled")
						WorkInfo.State.ENQUEUED -> Text("Filter enqueued")
						WorkInfo.State.BLOCKED -> Text("Filter blocked")
						null -> Text("Filter null")
					}
				}
			}
		}
	}
}
