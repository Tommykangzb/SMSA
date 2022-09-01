package com.example.campus.netty

interface ConnectServerManager {
    fun connectToServer()
    fun connectSuccess()
    fun connectFail()
    fun reConnect()
}