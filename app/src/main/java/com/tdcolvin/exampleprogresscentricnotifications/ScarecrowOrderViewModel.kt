package com.tdcolvin.exampleprogresscentricnotifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.ProgressStyle
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

const val ORDER_CHANNEL_ID = "scarecrow_order_channel_id"
const val ORDER_CHANNEL_NAME = "scarecrow_order_channel_name"
const val ORDER_NOTIFICATION_ID = 9999

class ScarecrowOrderViewModel(application: Application): AndroidViewModel(application) {
    private var order: CustomScarecrowOrder? = null

    val notificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val channel = NotificationChannel(ORDER_CHANNEL_ID, ORDER_CHANNEL_NAME, IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }

    fun placeOrder() {
        order = CustomScarecrowOrder().apply { startOrder() }

        viewModelScope.launch {
            order?.progress?.collect { progressStage ->
                showNotificationForProgress(progressStage)
            }
        }
    }

    fun showNotificationForProgress(progress: CustomScarecrowOrderProgress) {
        var notificationBuilder = NotificationCompat.Builder(application, ORDER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(
                IconCompat.createWithResource(
                    application, R.mipmap.ic_launcher
                ).toIcon(application)
            )
            .setOngoing(true)
            .setRequestPromotedOngoing(true)
            .setSilent(true)
            .setContentTitle("Building your scarecrow...")

        notificationBuilder = when(progress.state) {
            CustomScarecrowOrderProgressState.ReceivingOrder ->
                notificationBuilder
                    .setContentTitle("Receiving order")
                    .setContentText("Getting everything ready")
                    .setShortCriticalText("Starting")

            CustomScarecrowOrderProgressState.PickingClothes ->
                notificationBuilder
                    .setContentTitle("Picking clothes")
                    .setContentText("Checks or tweed? Hmm")
                    .setShortCriticalText("Clothes")

            CustomScarecrowOrderProgressState.Stuffing ->
                notificationBuilder
                    .setContentTitle("Stuffing")
                    .setContentText("Choosing the best materials")
                    .setShortCriticalText("Stuffing")

            CustomScarecrowOrderProgressState.AddingAccessories ->
                notificationBuilder
                    .setContentTitle("Adding accessories")
                    .setContentText("A handbag? A bowtie? Beautiful.")
                    .setShortCriticalText("Accessories")

            CustomScarecrowOrderProgressState.DrawingFace ->
                notificationBuilder
                    .setContentTitle("Drawing the face")
                    .setContentText("I told him his eyebrows were too high ... he looked surprised")
                    .setShortCriticalText("Face")

            CustomScarecrowOrderProgressState.Complete ->
                notificationBuilder
                    .setContentTitle("All done!")
                    .setContentText("Your scarecrow is ready for collection")
                    .setShortCriticalText("Done")
        }



//                notificationBuilder
//                    .addAction(
//                        NotificationCompat.Action.Builder(
//                            null, "Rate delivery", null).build()
//                    )

        val progressStyle = ProgressStyle()
            .setProgressPoints(
                listOf(
                    ProgressStyle.Point(10).setColor(0xFFFF0000.toInt()),
                    ProgressStyle.Point(30).setColor(0xFF00FF00.toInt()),
                    ProgressStyle.Point(50).setColor(0xFF0000FF.toInt()),
                    ProgressStyle.Point(80).setColor(0xFFFFFF00.toInt()),
                )
            )
            .setProgressSegments(
                listOf(
                    ProgressStyle.Segment(30).setColor(0xFF0000FF.toInt()),
                    ProgressStyle.Segment(20).setColor(0xFFFF0000.toInt()),
                    ProgressStyle.Segment(50).setColor(0xFF0000FF.toInt())
                )
            )
            .setProgressTrackerIcon(
                IconCompat.createWithResource(
                    application, R.drawable.scarecrow_simple
                )
            )
            .setProgress(progress.percentComplete)

        val notification = notificationBuilder
            .setStyle(progressStyle)
            .build()

        notificationManager.notify(ORDER_NOTIFICATION_ID, notification)
    }
}