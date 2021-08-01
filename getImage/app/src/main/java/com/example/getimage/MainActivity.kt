package com.example.getimage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Activity 는 메인과 서브 두 개만 구성한다.
 * 이 둘 사이에서만 데이터 주고 받으며 왔다갔다 한다.
 * 버튼 클릭시, 서브액티비티를 스택(?)에서 제거하고, 데이터는 메인으로 넘기고, 화면을 저장한다.
 *
 * [1]
 * 이미지뷰를 눌렀을 때,
 * setOnClickListener 함수 실행
 * - Dialog 띄우기 : 사진을 선택하세요 // 앨범 / 카메라
 * >> 앨범 클릭시, 핸드폰 갤러리에 접근
 * -> 선택시, 이미지뷰에 사진 넣어지고, 변수에 사진 정보 할당
 * >> 카메라 클릭시, 사진 촬영 기능
 * -> 촬영시, 갤러리에 저장 + 이미지뷰에 사진 넣어짐 + 변수에 사진 정보 할당
 *
 * [2]
 * 완료 버튼 클릭시,
 * 화면 전환되며, 저장된 정보를 화면에 뿌려줌
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_intent.setOnClickListener {
            startActivity(Intent(this, SubActivity::class.java))
        }
    }
}