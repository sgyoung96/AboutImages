package com.example.imagefromalbum

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var imageName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        try {
//            var imagePath: String = "$cacheDir/$imageName"
//            var bitmap: Bitmap = BitmapFactory.decodeFile(imagePath)
//            iv_image.setImageBitmap(bitmap)
//            Toast.makeText(applicationContext, "파일 로드 성공", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Toast.makeText(applicationContext, "파일 로드 실패", Toast.LENGTH_SHORT).show()
//        }

        // 앨범에서 이미지 가져오기
        tv_get_image.setOnClickListener {
            val intent: Intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 101)
        }

        // 로컬에서 이미지 삭제하기
//        tv_delete_image.setOnClickListener {
//            try {
//                var file: File = cacheDir
//                var fileList: Array<File> = file.listFiles()
//                for (i in 0..fileList.size) {
//                    if (fileList[i].name == imageName) {
//                        fileList[i].delete()
//                        Toast.makeText(applicationContext, "파일 삭제 성공", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//                Toast.makeText(applicationContext, "파일 삭제 실패", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                var fileUri: Uri? = data?.data
                var contentResolver: ContentResolver = contentResolver
                try {
                    var inputStream: InputStream? = contentResolver.openInputStream(fileUri!!)
                    var imageBitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    iv_image.setImageBitmap(imageBitmap)
                    inputStream?.close()

                    // 내부 저장소에 저장
//                    saveBitmapToJpeg(imageBitmap)
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "파일 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    fun saveBitmapToJpeg(bitmap: Bitmap) {
//        var tempFile: File = File(cacheDir, imageName)
//        try {
//            tempFile.createNewFile()
//            var fileOutputStream: FileOutputStream = FileOutputStream(tempFile)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//            fileOutputStream.close()
//            Toast.makeText(applicationContext, "파일 저장 성공", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Toast.makeText(applicationContext, "파일 저장 실패", Toast.LENGTH_SHORT).show()
//        }
//    }
}