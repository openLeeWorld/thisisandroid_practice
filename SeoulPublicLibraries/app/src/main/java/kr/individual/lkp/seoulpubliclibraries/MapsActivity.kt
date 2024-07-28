package kr.individual.lkp.seoulpubliclibraries

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kr.individual.lkp.seoulpubliclibraries.data.Library
import kr.individual.lkp.seoulpubliclibraries.data.Row
import kr.individual.lkp.seoulpubliclibraries.databinding.ActivityMapsBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var clusterManager: ClusterManager<Row>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("SeoulOpenApi", "onMapReady!!")
        loadLibraries()

        //클러스터 매니저 세팅
        clusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager) // 화면을 이동 후 멈췄을 때 설정
        mMap.setOnMarkerClickListener(clusterManager) // 마커 클릭 설정

        /*
        // 마커 클릭 리스너로 tag가 있으면 인텐트로 홈페이지를 띄움
        mMap.setOnMarkerClickListener { marker ->
            marker.tag?.let {
                var url = it as String //tag(it)를 대입
                if (!url.startsWith("http")) {
                    url = "http://${url}"
                }
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Action_VIEW: 주어진 데이터(uri)을 이용하여 적절한 액티비티를 실행
                startActivity(intent) // 안드로이드가 액티비티(웹 브라우저) 호출
            }
            true // 아니면 그냥 넘김
        }
        */
    }

    fun loadLibraries() {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(SeoulOpenApi.DOMAIN)
            .client(client) // Set custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val seoulOpenService = retrofit.create(SeoulOpenService::class.java) // 실행 가능한 서비스 객체로 변환

        seoulOpenService
            .getLibrary(SeoulOpenApi.API_KEY) // 레트로핏과 API 키로 데이터 가져옴
            .enqueue(object: Callback<Library> {
                override fun onFailure(call: Call<Library>, t: Throwable) {
                    Log.e("SeoulOpenApi", "Error loading libraries", t)
                    Toast.makeText(baseContext, "서버에서 데이터를 가져올 수 없습니다.", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Library>, response: Response<Library>) {
                    if (response.isSuccessful) {
                        Log.d("SeoulOpenApi", "onResponse Succeeded!!")
                        response.body()?.let {
                            showLibraries(it)
                        } ?: run {
                            Log.e("SeoulOpenApi", "Response body is null")
                            Toast.makeText(baseContext, "서버 응답에 오류가 있습니다.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Log.e("SeoulOpenApi", "Response not successful: ${response.code()}")
                        Toast.makeText(baseContext, "서버 응답에 오류가 있습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    fun showLibraries(libraries: Library) {
        val latLngBounds = LatLngBounds.Builder() // 마커 전체의 영역

        for(lib in libraries.SeoulPublicLibraryInfo.row) {
            clusterManager.addItem(lib)
            val position = LatLng(lib.XCNTS.toDouble(), lib.YDNTS.toDouble()) // 마커의 좌표 생성

            /*
            val marker = MarkerOptions().position(position).title(lib.LBRRY_NAME)
            val obj = mMap.addMarker(marker) // 마커 추가
            obj?.tag = lib.HMPG_URL // 홈페이지를 tag에 추가(마커에 태그 설정)
             */

            latLngBounds.include(position) // 마커의 영역만큼 보여줌
        }

        val bounds = latLngBounds.build() // 마커 영역 구함
        val padding = 0 // 마커의 영역에 얼마만큼의 여백?

        val updated = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.moveCamera(updated) // 변경 카메라 지도에 반영
        Log.d("SeoulOpenApi", "showLibraries Succeeded!!")
    }
}