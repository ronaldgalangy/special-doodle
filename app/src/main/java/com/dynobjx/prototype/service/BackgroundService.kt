package com.dynobjx.prototype.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import com.indooratlas.android.sdk.IALocation
import com.indooratlas.android.sdk.IALocationListener
import com.indooratlas.android.sdk.IALocationManager
import com.indooratlas.android.sdk.IALocationRequest
import android.graphics.Bitmap
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.support.v7.app.NotificationCompat
import com.dynobjx.prototype.MainActivity
import com.dynobjx.prototype.R


class BackgroundService : Service(), IALocationListener {

    private  lateinit var onLocationChangeListener: (IALocation) -> Unit

    private val iaLocationManager: IALocationManager by lazy {
        IALocationManager.create(this)
    }

    private var iBinder: IBinder = ServiceBinder()

    override fun onCreate() {
        val locationRequest: IALocationRequest = IALocationRequest.create()
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 1F
        iaLocationManager.requestLocationUpdates(locationRequest, this)
        //startForegroundService()
        super.onCreate()
    }

    private fun startForegroundService() {
        val icon = BitmapFactory.decodeResource(resources,
                R.mipmap.ic_launcher)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent
                .FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentTitle("Enjoy is running on Lockscreen")
                .setContentText("Tap to open Enjoy.")
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)

        val notification = notificationBuilder.build()
        startForeground(9999, notification)
    }

    override fun onDestroy() {
        iaLocationManager.removeLocationUpdates(this)
        iaLocationManager.destroy()
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder {
        return iBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int)= START_STICKY_COMPATIBILITY

    override fun onLocationChanged(iaLocation: IALocation) {
        onLocationChangeListener(iaLocation)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    fun setLocationChangeListener(listener: (IALocation) -> Unit) {
        onLocationChangeListener = listener
    }

    inner class ServiceBinder : Binder() {
        fun getService()= this@BackgroundService
    }

}
