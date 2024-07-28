package kr.individual.lkp.seoulpubliclibraries

import kr.individual.lkp.seoulpubliclibraries.data.Library
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// 서울OpenApi 변수
class SeoulOpenApi {
    companion object {
        val DOMAIN = "http://openAPI.seoul.go.kr:8088/"
        val API_KEY = "<실제 서울 공공 데이터 API>"
    }
}

// 레트로핏에서 사용할 인터페이스
interface SeoulOpenService {
    @GET("{api_key}/json/SeoulPublicLibraryInfo/1/200")
    fun getLibrary(@Path("api_key") key: String): Call<Library>
}