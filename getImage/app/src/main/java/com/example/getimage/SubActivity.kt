package com.example.getimage

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_sub.*

class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        iv_sub.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("프로필 사진 선택")
            builder.setMessage("사진을 선택하세요")

            var listener = DialogInterface.OnClickListener { _, a ->
                when (a) {
                    DialogInterface.BUTTON_POSITIVE ->
                    {
                        //startActivity(Intent(this, MainActivity::class.java))
                        getImageFromAlbum()
                    }
                    DialogInterface.BUTTON_NEUTRAL -> {
                        //startActivity(Intent(this, MainActivity::class.java))
                        getImageFromCamera()
                    }
                }
            }
            builder.setNeutralButton("Album", listener)
            builder.setPositiveButton("Camera", listener)

            builder.show()
        }
    }

    fun getImageFromAlbum() {

    }

    fun getImageFromCamera() {

    }
}
