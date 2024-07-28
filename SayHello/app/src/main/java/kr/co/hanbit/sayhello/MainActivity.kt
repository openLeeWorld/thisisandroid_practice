package kr.co.hanbit.sayhello

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.co.hanbit.sayhello.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Android의 Jetpack 라이브러리의 일부로,
        // 앱의 레이아웃이 기기 화면의 전체 영역(엣지 투 엣지)을 사용할 수 있도록 설정하는 함수
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // activity_main.xml과 매칭되는 viewBinding by android

        // setContentView(R.layout.activity_main) // kotlin extension method
        setContentView(binding.root) // viewBinding method
        binding.btnSay.setOnClickListener {
            binding.textSay.text = "Hello Kotlin!!!"
        } // textSay textbox의 text내용을 설정하는 리스너
        // id가 btnSay인 버튼에 리스너를 설정 (버튼이 클릭되면 내부 코드 동작)


        /* // 시스템 바가 시스템 UI를 가리지 않도록 설정, kotlin extension 방식
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         */
        // 시스템 바(상태 바 및 내비게이션 바) 영역을 고려하여 뷰의 레이아웃을 조정, binding 방식
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /** Javadoc을 이용해서 자동으로 문서화 할 수 있게 **을 붙임 **/
    }
}
