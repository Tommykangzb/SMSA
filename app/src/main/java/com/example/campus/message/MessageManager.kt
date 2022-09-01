package com.example.campus.message

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.campus.netty.IMessage
import com.example.campus.protoModel.MessageBase
import com.google.protobuf.ByteString

object MessageManager : Handler(Looper.getMainLooper()){

    const val TAG = "MessageManager"
    // 1xx netty message
    const val WHAT_WS_MESSAGE = 100

    //2xx message fetch fail
    const val WHAT_WS_MESSAGE_FAIL = 200


    @Volatile
    private var managerSingleton: MessageManager? = null
    fun getInstance(): MessageManager? {
        if (managerSingleton == null) {
            synchronized(MessageManager::class.java) {
                if (managerSingleton == null) {
                    managerSingleton = MessageManager
                }
            }
        }
        return managerSingleton
    }
    private var listenerList = ArrayList<IMessage>()

    override fun handleMessage(message: Message) {
        when (message.what) {
            WHAT_WS_MESSAGE -> {
                message.apply {
                    data?.also {
                        val builder = MessageBase.Message.newBuilder()
                        builder.setSenderId("1234")
                            .setReceiverId("1234")
                            .setTimeStamp(System.currentTimeMillis().toFloat())
                            .setData(
                                ByteString.copyFrom(
                                    it.getString("IMESSAGE_STRING")?.toByteArray()
                                )
                            )
                            .setAckMsgId("123")
                            .setMsgId("123")
                            .setType(2).source = 1
                        dispatchMsg(builder.build())
                    }
                }
            }
            WHAT_WS_MESSAGE_FAIL -> {
                Log.e(TAG, "webSocket message fetch failed!")
                return
            }
        }
    }

    fun addListener(l: IMessage) {
        if (!listenerList.contains(l)) {
            listenerList.add(l)
        }
    }

    fun removeListener(l: IMessage) {
        if (listenerList.contains(l)) {
            listenerList.remove(l)
        }
    }

    private fun dispatchMsg(message: MessageBase.Message?) {
        listenerList.forEach {
            it.onMessage(message)
        }
    }
}