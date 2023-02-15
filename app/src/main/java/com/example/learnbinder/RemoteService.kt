package com.example.learnbinder

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Parcel
import com.example.learnbinder.IBaseAidlInterface.Stub.TRANSACTION_callFromService

/**
 * @author    yiliyang
 * @date      2023/2/15 下午3:18
 * @version   1.0
 * @since     1.0
 */
class RemoteService : Service() {

    private var remoteBinder: IBinder? = null

    override fun onBind(intent: Intent?): IBinder {
        return MyBinder()
    }

    inner class MyBinder : IMyAidlInterface.Stub() {

        override fun test(data: String?): String {
            return "reply $data"
        }

        override fun transBinder(binder: IBinder?) {
            remoteBinder = binder
        }

        override fun testBinder(): String {
            println("testBinder " + Thread.currentThread())
            val data = Parcel.obtain()
            data.writeInterfaceToken("com.example.learnbinder.IBaseAidlInterface")
            val result = Parcel.obtain()
            remoteBinder!!.transact(TRANSACTION_callFromService, data, result, 0)
            result.readException()
            return result.readString() ?: "none"
        }
    }
}