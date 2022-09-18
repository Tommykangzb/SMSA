package com.example.campus.message

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.example.campus.helper.SnowFlake
import com.example.campus.netty.ClientHandler
import com.example.campus.netty.IMessage
import com.example.campus.protoModel.BaseMessageOuterClass.BaseMessage
import java.util.concurrent.ConcurrentHashMap

object MessageManager : Handler(Looper.getMainLooper()) {

    const val TAG = "MessageManager"

    // 1xx netty message
    const val WHAT_WS_MESSAGE = 100

    //2xx message fetch fail
    const val WHAT_WS_MESSAGE_FAIL = 200

    private var msgMap = ConcurrentHashMap<Int, MutableSet<IMessage>>()


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

    override fun handleMessage(message: Message) {
        when (message.what) {
            WHAT_WS_MESSAGE -> {
                dispatchMsg(message)
                confirmReceivedMsg(message)
            }
            WHAT_WS_MESSAGE_FAIL -> {
                Log.e(TAG, "webSocket message fetch failed!")
                return
            }
        }
    }

    fun addListener(type: Int, l: IMessage) {
        if (!msgMap.contains(type)) {
            msgMap[type] = HashSet<IMessage>().apply { add(l) }
            return
        }
        msgMap[type]?.add(l)
    }

    fun removeListener(type: Int, l: IMessage) {
        msgMap[type]?.remove(l)
    }

    private fun dispatchMsg(message: Message?) {
        val msg = message?.obj as? BaseMessage ?: return
        val listenerList = msgMap[msg.type] ?: return
        listenerList.forEach {
            it.onMessage(msg)
        }
    }

    private fun confirmReceivedMsg(message: Message?) {
        val msg = message?.obj as? BaseMessage ?: return
        val builder = BaseMessage.newBuilder()
        builder.setSenderId(msg.receiverId)
            .setReceiverId(msg.senderId)
            .setTimeStamp(System.currentTimeMillis().toFloat())
            .setAckMsgId("-1L")
            .setMsgId(SnowFlake.getInstance().nextMsgId())
            .setType(MessageType.CHAT_MESSAGE)
            .setSource(1)
            .build()
        ClientHandler.getInstance().writeMsg(builder.build())
    }
}