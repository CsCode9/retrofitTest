package com.cimcitech.retrofotrequesttest

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cimcitech.permissionx.PermissionX
import com.cimcitech.retrofotrequesttest.adpter.FileAdapter
import com.cimcitech.retrofotrequesttest.bean.requestBean.CustomerRequest
import com.cimcitech.retrofotrequesttest.bean.responseBean.CustomerResponse
import com.cimcitech.retrofotrequesttest.network.Network
import com.cimcitech.retrofotrequesttest.network.Repository
import com.cimcitech.retrofotrequesttest.selectPicture.FullyGridLayoutManager
import com.cimcitech.retrofotrequesttest.selectPicture.GridImageAdapter
import com.cimcitech.retrofotrequesttest.ui.MainViewModel
import com.cimcitech.retrofotrequesttest.ui.PlaceViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.permissions.Permission
import com.luck.picture.lib.permissions.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val viewModel_caiyun by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var albumUri: Uri
    lateinit var outputImage: File
    private var files = ArrayList<File>()
    private lateinit var adapter: GridImageAdapter
    private var selectList = ArrayList<LocalMedia>()
    private val maxSelectNum = 9;
    private lateinit var pop: PopupWindow
    private lateinit var images: List<LocalMedia>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidget()
        btn_sure.setOnClickListener {
            // 创建File对象，用于存储拍照后的图片
            outputImage = File(externalCacheDir, "output_image.jpg")
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    this,
                    "com.example.cameraalbumtest.fileprovider",
                    outputImage
                );
            } else {
                Uri.fromFile(outputImage);
            }
            // 启动相机程序
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }

        startRequest.setOnClickListener {
            viewModel.getCustomer(CustomerRequest())
        }

        queryPlace.setOnClickListener {
            viewModel_caiyun.getPlace("北京")
        }

        uploadFile.setOnClickListener {
            viewModel.fileUpload(files)
        }

        open.setOnClickListener {
            // 打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            // 指定只显示照片
            intent.type = "image/*"
            startActivityForResult(intent, fromAlbum)
        }

        viewModel.fileUploadData.observe(this, Observer {
            val urls = it.getOrNull()
            if (urls != null) {
                Log.e("----------------", "onCreate: ${urls.size}")
            }
        })

        viewModel_caiyun.plaveData.observe(this, Observer {
            val place = it.getOrNull()
            if (place != null) {
//                Log.e("------------------", "\u5317\u4eac\u5e02")
                Log.e("------------", "${place.size}")

            }
        })

        viewModel.customerData.observe(this, Observer {
            val customer = it.getOrNull()
            if (customer != null) Log.e("------------", "${customer.size}")
        })

        callPhone.setOnClickListener {
            PermissionX.request(this, Manifest.permission.CALL_PHONE) { allGranted, deniedList ->
                if (allGranted) {
                    call()
                } else {
                    Toast.makeText(this, "You denied $deniedList", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    val bitmap =
                        BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageView.setImageBitmap(bitmap)

                }
            }
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的照片显示
                        albumUri = uri
                        val bitmap = getBitmapFromUri(uri)
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }

            PictureConfig.CHOOSE_REQUEST ->
                if (resultCode == Activity.RESULT_OK){
                    images = PictureSelector.obtainMultipleResult(data)
                    selectList.addAll(images)
                    files.clear()
                    for ((index, item) in images.withIndex()){
                        var file = File(images.get(index).path)
                        files.add(file)
                    }
                    adapter.setList(selectList)
                    adapter.notifyDataSetChanged()

                }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    private fun call() {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:10086")
            startActivity(intent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun initWidget() {
        val manager = FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        file_rv.layoutManager = manager
        adapter = GridImageAdapter(this, onAddPicClickListener)
        adapter.setList(selectList)
        adapter.setSelectMax(maxSelectNum)
        file_rv.adapter = adapter
        adapter.setOnItemClickListener(object : GridImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, v: View?) {
                if (selectList.size > 0) {
                    var media = selectList.get(position)
                    var pictureType = media.pictureType
                    var mediaType = PictureMimeType.pictureToVideo(pictureType)
                    when (mediaType) {
                        1 -> PictureSelector.create(this@MainActivity)
                            .externalPicturePreview(position, selectList)
                        2 -> PictureSelector.create(this@MainActivity)
                            .externalPictureVideo(media.path)
                        3 -> PictureSelector.create(this@MainActivity)
                            .externalPictureAudio(media.path)
                    }
                }
            }
        })
    }

    private val onAddPicClickListener: GridImageAdapter.onAddPicClickListener =
        object : GridImageAdapter.onAddPicClickListener {
            override fun onAddPicClick() {
                com.permissionx.guolindev.PermissionX.init(this@MainActivity)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, grantedList, deniedList ->
                        if (allGranted){
                            showPop()
                        }else{
                            Toast.makeText(this@MainActivity, "拒绝", Toast.LENGTH_SHORT).show();
                        }
                    }
//                var rxPermission = RxPermissions(this@MainActivity)
//                rxPermission.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .subscribe {
//                        @RequiresApi(Build.VERSION_CODES.N)
//                        object : Consumer<Permission> {
//                            override fun accept(t: Permission) {
//                                if (t.granted) {
//                                    Log.e("------------", "accept: 打印11111" )
//                                    showPop()
//                                } else {
//                                    Toast.makeText(this@MainActivity, "拒绝", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                        }
//                    }
            }
        }

    fun showPop() {
        val bottomView = View.inflate(this, R.layout.layout_bottom_dialog, null)
        val mAlbum: TextView = bottomView.findViewById(R.id.tv_album)
        val mCamera: TextView = bottomView.findViewById(R.id.tv_camera)
        val mCancel: TextView = bottomView.findViewById(R.id.tv_cancel)
        pop = PopupWindow(bottomView, -1, -2)
        pop.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pop.isOutsideTouchable = true
        pop.isFocusable = true
        val lp = window.attributes
        lp.alpha = 0.5f
        window.attributes = lp
        pop.setOnDismissListener(object : PopupWindow.OnDismissListener {
            override fun onDismiss() {
                val lp = window.attributes
                lp.alpha = 1f
                window.attributes = lp
            }
        })
        pop.animationStyle = R.style.main_menu_photo_anim
        pop.showAtLocation(window.decorView, Gravity.BOTTOM, 0, 0)
        val clickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                when (v?.id) {
                    R.id.tv_album ->
                        PictureSelector.create(this@MainActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .minSelectNum(1)
                            .imageSpanCount(4)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(PictureConfig.CHOOSE_REQUEST)
                    R.id.tv_camera ->
                        PictureSelector.create(this@MainActivity)
                            .openCamera(PictureMimeType.ofImage())
                            .forResult(PictureConfig.CHOOSE_REQUEST)
                    R.id.tv_cancel -> {
                    }
                }
                closePopupWindow()
            }
        }
        mAlbum.setOnClickListener(clickListener)
        mCamera.setOnClickListener(clickListener)
        mCancel.setOnClickListener(clickListener)
    }

    fun closePopupWindow() {
        if (pop != null && pop.isShowing) {
            pop.dismiss()
        }
    }
}
