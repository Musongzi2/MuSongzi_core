package com.musongzi.test.activity

import android.Manifest
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.DrawableContainer
import android.os.Bundle
import android.view.Choreographer
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.musongzi.test.R
import com.musongzi.test.databinding.ActivityMainBinding

class SplashActivity : BaseActivity() {

    lateinit var d: ActivityMainBinding


//    val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        d = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0x99
        );


//        lifecycleScope.launch {
//            Log.i("Continuation", "onCreate: start")
//
//            val two = async {
//                two()
//            }.apply {
//                start()
//            }
//
//            val three = async {
//                three()
//            }.apply {
//                start()
//            }
//            Log.i("Continuation", "onCreate: ${two.await() + three.await()}")

//            Log.i(
//                "Continuation", "onCreate: sum = ${
//                    async {
//                        two()
//                    }.await() + async {
//                        three()
//                    }.await()
//                } "
//            )

//            val wait =  async {
//                withContext(Dispatchers.Default) {
//                    Thread.sleep(4000)
//                    Log.i("Continuation", "onCreate: Thread = ${Thread.currentThread().name}")
//                    "haha"
//                }
//            }
//
//            val waitResult = wait.start().apply {
//                wait.await()
//            }
//            Log.i("Continuation", "onCreate: 等待")
//            Log.i("Continuation", "onCreate: waitResult = $waitResult")

//        }

//        val c = object : Choreographer.FrameCallback {
//            override fun doFrame(frameTimeNanos: Long) {
//                System.out.println("fram : it = $frameTimeNanos")
//                Choreographer.getInstance().postFrameCallback(this)
//            }
//
//
//        }
//
//        Choreographer.getInstance().postFrameCallback(c)

    }


    override fun onResume() {
        super.onResume()
        var tag = d.idImage.tag

        if (tag == null) {
            d.idImage.tag = d.idImage.drawable

        }



        (d.idImage.drawable as? AnimationDrawable)?.start()
    }

    override fun onPause() {
        super.onPause()
        (d.idImage.drawable as? AnimationDrawable)?.stop()
    }

    fun goTow(v: View) {

        (d.idImage.drawable as? AnimationDrawable)?.apply {


            if (isRunning) {
                stop()
            } else {
                start()
            }

        }


//        PictureSelector.create(this)
//            .openGallery(SelectMimeType.ofImage())
//            .setImageEngine(GlideEngine.createGlideEngine())
//            .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                override fun onResult(result: ArrayList<LocalMedia?>?) {}
//                override fun onCancel() {}
//            })
//        MainIndexFragment::class.java.startActivityNormal(
//            "主页",
//            MainIndexActivity::class.java,
//            Color.WHITE,
//            null, //TestMainIndexBusiness::class.java.name
//        )
//        MainActivity::class.java.startActivity()
//        TowFragment::class.java.startActivityNormal("第二个页面")

//        TestImplFragment::class.java.startActivityNormal("滑动测试")

//        PhotoSelectFragment::class.java.startActivityNormal(
//            "数据测试",
//            dataBundleMethod = { bundle ->
//                bundle.putString("hahakey", "你好第二个页面")
//            })

//        BannerAndRetrofitMainFragment::class.java.startActivityNormal("Banner请求")

//        RecyleViewCheckFragment::class.java.startActivityNormal("测试recycle")

//        TowActivity::class.java.startActivity()

//        Camra2Fragment::class.java.startActivityNormal("TestMainFragment")
//        CollectionsViewFragment.

//        ArrayEngine::class.java.convertFragment().asInterfaceByEngine {
//
//        }

//        ArrayEngine::class.java.startRecycleActivity("选择")

    }

}
