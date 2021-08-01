package com.example.imagedb

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// java.lang.IllegalArgumentException: Failed to find configured root that contains /storage/emulated/0/Pictures/ImageDB/JPEG_20210801_113705.jpg
// Manifest 와 file_paths 의 경로는 내 패키지여야 함
class MainActivity : AppCompatActivity() {

    // 카메라와 앨범 접근 권한에 필요한 변수들
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val PERMISSION_CAMERA = 1
    val PERMISSION_STORAGE = 2
    val REQUEST_CAMERA = 3
    val REQUEST_STORAGE = 4

    val REQUEST_TAKE_PHOTO = 1
    var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 여기를 클릭하여 앨범 혹은 카메라로부터 이미지 가져오기
        iv_setIamge.setOnClickListener {
            getImage()
        }

        // et_saveFlag 의 텍스트를 입력값으로 받아서 DB 에 이미지 저장하기
        // 여기를 클릭하여 이미지와 플래그를 저장하기
        btn_save.setOnClickListener {

        }

        // et_searchFlag 의 텍스트를 입력값으로 받아서 DB 에서 이미지 가져오기
        // 여기를 클릭하여 이미지 가져와 아래 iv_getImage 에 세팅하기
        btn_load.setOnClickListener {

        }
    }

    // 사진을 찍기 전, 사진이 저장되는 임시 파일 생성. 파일의 형식 지정
    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File? = null
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Pictures", "ImageDB")
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    // 권한 승인했는지 확인
    private fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, flag)
                return false
            }
        }
        return true
    }

    private fun getImage() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("이미지 등록")
        builder.setMessage("이미지 등록하기")

        var listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    openGallery()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    openCamera()
                }
            }
        }
        builder.setNeutralButton("앨범", listener)
        builder.setPositiveButton("카메라", listener)

        builder.show()
    }

    private fun openGallery() {
        if (checkPermission(STORAGE_PERMISSION, PERMISSION_CAMERA)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_STORAGE)
        }
    }

    private fun openCamera() {
        if (checkPermission(CAMERA_PERMISSION, PERMISSION_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (intent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.e("captureCamera Error", ex.toString())
                    return
                }
                if (photoFile != null) { // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                }
            }

            startActivityForResult(intent, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    val uri = data?.data
                    iv_setIamge.setImageURI(uri)
                }
                REQUEST_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    iv_setIamge.setImageBitmap(bitmap)
                }
                REQUEST_TAKE_PHOTO -> {
                    Log.i("REQUEST_TAKE_PHOTO", "${Activity.RESULT_OK}" + " " + "${resultCode}");
                    if (resultCode == RESULT_OK) {
                        try {
                            galleryAddPic()
                        } catch (e: Exception) {
                            Log.e("REQUEST_TAKE_PHOTO", e.toString());
                        }

                    } else {
                        Toast.makeText(this@MainActivity, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    // 이미지를 로컬 폴더에 저장
    fun galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val file = File(mCurrentPhotoPath)
        val contentUri: Uri = Uri.fromFile(file)
        intent.setData(contentUri)
        sendBroadcast(intent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 로컬에 저장한 이미지 이미지뷰에 세팅
        iv_setIamge.setImageURI(contentUri)
    }
}