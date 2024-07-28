package kr.co.hanbit.googlemaps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.co.hanbit.googlemaps.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var locationPermission: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            results ->
            if (results.all { it.value }) {
                startProcess()
                Log.d("Location!!", "Start process!!")
            } else {
                Toast.makeText(this,
                    "권한 승인이 필요합니다.", Toast.LENGTH_LONG).show()
            }
        }

        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION, // 네트워크 위치
                Manifest.permission.ACCESS_FINE_LOCATION // 네트워크와 GPS 위치
            )
        )
    }

    fun startProcess() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // GoogleMap이 완료 시
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
        /*
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this) // 검색 위치 클라이언트
        Log.d("Location!!", "onMapReady process!!")
        updateLocation() // 위치 정보 변경시 업데이트
    }

    @SuppressLint("MissingPermission") // 해당 코드를 체크하지 않아도 됨
    fun updateLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.run () { // 위치 정보를 요청할 정확도와 주기를 설정
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000 // 1초마다 한 번씩 요청
        }

        locationCallback = object: LocationCallback() { // 1초마다 반환받을 콜백
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.let { // 변화된 위치 정보가 onLocationResult로 전달됨
                    for (location in it.locations) {
                        Log.d("Location!!", "${location.latitude}, ${location.longitude}")
                        setLastLocation(location) // 위치 정보를 받아서 마커를 그리고 화면을 이동
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        // 권한 처리는 생략(Suppress), myLooper는 현재 스레드와 관련있는 Looper 객체
    }

    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
        val markerOptions = MarkerOptions()
            .position(LATLNG)
            .title("Here!")

        val cameraPosition = CameraPosition.Builder()
            .target(LATLNG)
            .zoom(15.0f)
            .build()

        mMap.clear() // 이전에 그려진 마커가 있으면 지움
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}