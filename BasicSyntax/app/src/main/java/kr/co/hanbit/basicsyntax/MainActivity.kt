package kr.co.hanbit.basicsyntax

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.hanbit.basicsyntax.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = CustomAdapter()
        binding.recyclerView.adapter = adapter

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrofit 빌더를 사용해서 레트로핏 생성
        // baseUrl이 되는 Github 도메인 주소와 JSON 데이터를 생성한 Repository의 컬렉션으로 변환해주는 컨버터 입력
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        binding.buttonRequest.setOnClickListener {
            // 요청 버튼 클릯 레트로핏으로 데이터를 불러오고 어댑터에 세팅
            val githubService = retrofit.create(GithubService::class.java) // create는 인터페이스를 실행 가능한 객체로
            githubService.users().enqueue(object: Callback<Repository> {
                // 비동기 통신으로 데이터를 가져오는 users메소드의 enqueue() 사용
                // 콜백 인터페이스 구현
                override fun onFailure(call: Call<Repository>, t: Throwable) {
                    // 실패 시 동작
                }

                override fun onResponse(call: Call<Repository>, response: Response<Repository>) {
                    // 성공 시 데이터를 꺼내서 형변환 후 어댑터의 userList에 담음
                    adapter.userList = response.body() as Repository
                    adapter.notifyDataSetChanged() // 리사이클뷰에 변경 사항 적용
                }
            })
        }
    }
}

// 레트로핏 인터페이스는 호출 방식, 주소, 데이터 등을 지정
// Retrofit 라이브러리는 인터페이스를 해석해 HTTP 통신을 처리
// 데이터 출처: https://api.github.com/users/Kotlin/repos
interface GithubService {
    @GET("users/Kotlin/repos") // 요청 주소에 Github 도메인은 제외
    fun users(): Call<Repository> // 하위 버전은 Call<List<Repository>>
}