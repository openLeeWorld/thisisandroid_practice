package kr.individual.lkp.seoulpubliclibraries.data

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Row( // 마커에 해당하는 클래스
    val ADRES: String,
    val CODE_VALUE: String,
    val FDRM_CLOSE_DATE: String,
    val GU_CODE: String,
    val HMPG_URL: String,
    val LBRRY_NAME: String,
    val LBRRY_SEQ_NO: String,
    val LBRRY_SE_NAME: String,
    val OP_TIME: String,
    val TEL_NO: String,
    val XCNTS: String,
    val YDNTS: String
) : ClusterItem { // 데이터 클래스에 ClusterItem을 추가
    override fun getPosition(): LatLng {
        return LatLng(XCNTS.toDouble(), YDNTS.toDouble()) // 개별 마커가 표시될 좌표
    } // 해당 마커의 좌표 계산용, 클러스터링: 특정 범위 안에 있는 마커들을 묶어서 하나의 마커로 만들고 숫자 표시

    override fun getSnippet(): String? {
        return ADRES // 마커가 클릭했을 때 나타나는 서브타이틀 : 주소
    }

    override fun getTitle(): String? {
        return LBRRY_NAME // 마커가 클릭했을 때 나타나는 타이틀 : 도서관 이름
    }

    override fun hashCode(): Int {
        return LBRRY_SEQ_NO.toInt() // id에 해당하는 유일한 값을 Int로 반환
    }
}