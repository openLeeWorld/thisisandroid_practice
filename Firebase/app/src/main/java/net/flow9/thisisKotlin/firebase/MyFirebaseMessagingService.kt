package net.flow9.thisisKotlin.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        //super.onNewToken(token)
        Log.d("토큰", "$token") // 토큰 갱신 전까지는 다시 호출되지 않음
    }
}