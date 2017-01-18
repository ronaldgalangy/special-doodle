package com.dynobjx.prototype

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.dynobjx.prototype.service.BackgroundService
import com.indooratlas.android.sdk.IALocation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ServiceConnection {

    private lateinit var backgroundService: BackgroundService

    private val onLocationChanged: (IALocation) -> Unit = {
        val text: String = "${log.text} \n"
        log.text = "$text T/${it.time}: Location : ${it.latitude}, ${it.longitude}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        val intent: Intent = Intent(this, BackgroundService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        super.onStart()
    }

    override fun onServiceConnected(componentName: ComponentName?, ibinder: IBinder?) {
        val serviceBinder: BackgroundService.ServiceBinder = ibinder as BackgroundService.ServiceBinder
        backgroundService = serviceBinder.getService()
        backgroundService.setLocationChangeListener(onLocationChanged)
    }

    override fun onServiceDisconnected(componentName: ComponentName?) {

    }
}
