package com.musongzi.test.vm

import androidx.databinding.ObservableField
import com.musongzi.core.base.business.EmptyBusiness
import androidx.lifecycle.DataDriveViewModel


class BannerViewModel : DataDriveViewModel<EmptyBusiness>() {


    var textContextField = ObservableField<String>()

    fun loadCheckBanner() {
//        showDialog("等待")
//        getApi().grilPic().sub {
//            disimissDialog()
//            Log.i(TAG, "loadCheckBanner:grilPic succed")
//            BANNER_BITMAP_KEY.saveStateChange(localSavedStateHandle(),BitmapFactory.decodeStream(it.byteStream()))
//        }
    }

    fun login(s: String, s1: String) {

//        RetrofitManager.getInstance().getApi(MszTestApi::class.java, this).login2(LoginBean(s, s1))
//            .sub {
//                Log.i(TAG, "login: ${it.toJson()}")
//            }

    }

    fun upload() {

//        PictureSelector.create(getHolderClient() as? Fragment)
//            .openGallery(ofAll())
//            .setSelectionMode(SelectModeConfig.SINGLE)
//            .setImageEngine(GlideEngine.createGlideEngine())
//            .forResult(object : OnResultCallbackListener<LocalMedia> {
//                override fun onCancel() {
//                    // 取消
//                }
//
//                override fun onResult(result: ArrayList<LocalMedia>?) {
//                    result?.let { it ->
//                        Log.i(TAG, "onResult: path ${it[0].realPath}")
//                        val photoRequestBody: RequestBody =
//                            RequestBody.create(MediaType.parse("image/jpg"), File(it[0].realPath))
//                        val photo = MultipartBody.Part.createFormData(
//                            "headerFile",
//                            "headerFile2.jpg",
//                            photoRequestBody
//                        )
//                        RetrofitManager.getInstance().getApi(MszTestApi::class.java, this@BannerViewModel).postPath(photo).sub{ re->
//                            Log.i(TAG, "onResult: $re")
//                            ToastUtils.showToast(ActivityLifeManager.getInstance().getTopActivity(),"成功")
//                        }
//                    }
//                }
//            })
    }


    companion object {
        const val BANNER_BITMAP_KEY = "BANNER_BITMAP_KEY"
        const val BANNER_KEY = "banner_key"
    }


}