package net.flow9.thisisKotlin.firebase

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import net.flow9.thisisKotlin.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("토큰", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener // 콜백 함수 실행 흐름을 중단하거나 람다식 실행을 종료
            }
            val token = task.result
            Log.d("토큰", "재호출=${token.toString()}")
        })
    }
}

