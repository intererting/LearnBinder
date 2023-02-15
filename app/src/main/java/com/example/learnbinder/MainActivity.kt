package com.example.learnbinder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.IBinder.FLAG_ONEWAY
import android.os.Parcel
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myBaseBinder = MyBaseBinder()
        findViewById<Button>(R.id.startService).setOnClickListener {
            bindService(Intent(this, RemoteService::class.java), object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val remoteService = IMyAidlInterface.Stub.asInterface(service)
                    println(remoteService.test("hello"))
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                }

            }, Context.BIND_AUTO_CREATE)
        }
        findViewById<Button>(R.id.transBinder).setOnClickListener {
            bindService(Intent(this, RemoteService::class.java), object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val remoteService = IMyAidlInterface.Stub.asInterface(service)
                    remoteService.transBinder(myBaseBinder)
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                }

            }, Context.BIND_AUTO_CREATE)
        }

        findViewById<Button>(R.id.testBinder).setOnClickListener {
            bindService(Intent(this, RemoteService::class.java), object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder) {
                    Thread {
                        println("onServiceConnected " + Thread.currentThread())
                        val data = Parcel.obtain()
                        data.writeInterfaceToken("com.example.learnbinder.IMyAidlInterface")
                        val result = Parcel.obtain()
                        service.transact(
                            IMyAidlInterface.Stub.TRANSACTION_testBinder, data, result, FLAG_ONEWAY
                        )
                        println("waitting ${Thread.currentThread()}")
                        result.readException()
                        println(result.readString())
                    }.start()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                }

            }, Context.BIND_AUTO_CREATE)
        }
    }

    class MyBaseBinder : IBaseAidlInterface.Stub() {
        override fun callFromService(): String {
            println("callFromService " + Thread.currentThread())
            return "hello service"
        }

    }
}