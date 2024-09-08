package com.musongzi.comment.util.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.math.abs

/**
 * @Author : linhui

 * @Time : On 2024/8/20 11:54

 * @Description : TouchSupoortAudioRecorder
 */
class TouchSupoortAudioRecorder private constructor(activity: AppCompatActivity) :
    SimpleAudioRecording(activity),
    ITouchSupportAudioRecorder {

    private val startRunnbale = Runnable {
        pre()
        start()
    }

    private val recordingListener by lazy {
        object : ITouchSupportAudioRecorder.TouchListener {
            override fun onDownEvent(downY: Float) {
                handler?.removeCallbacks(startRunnbale)
                handler?.postDelayed(startRunnbale, downLimite)
            }

            override fun onMoveDownEvent(sumAbs: Float) {
//                AppLog.d(TAG, "onMoveDownListener sumDown = $sum")
                if (recordingMoveState != RECORDING_NORMAL_STATE) {
                    recordingMoveState = RECORDING_NORMAL_STATE
                }
            }

            override fun onUpEvent(endSum: Float) {
//                AppLog.d(TAG, "onUpEvent sumDown = $endSum")
                handler?.removeCallbacks(startRunnbale)
            }

            override fun onUpCancelRecording(from: Int) {
                getHodlerAudioRecorder().cancel()
            }

            override fun onUpFinishRecording() {
                getHodlerAudioRecorder().stop()
            }


        }
    }

    private val requestPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            enablePermisson = it
            if (!enablePermisson) {
//                ToastUtils.showToast(activity, activity.getString(R.string.permission_audio_tip))
            }
        }
    private var enablePermisson = false

    private val listener: MutableList<ITouchSupportAudioRecorder.TouchListener> =
        mutableListOf(recordingListener)
    private var downY = 0f
    private var sumMove = downY
    private var audioRecorder: IAudioRecorder = this

    private val downLimite = 200L
    private var handler: Handler? = null
    private var lifeState = 0;
    private var recordingMoveState = UNRECORDING_STATE;

    companion object {
        const val TAG = "TouchSupoortAudioRecorder"

        const val RECORDING_READY_CANCEL_STATE = 2
        const val RECORDING_NORMAL_STATE = 1
        const val UNRECORDING_STATE = -1

        fun createInstance(activity: AppCompatActivity): ITouchSupportAudioRecorder {
            return TouchSupoortAudioRecorder(activity);
        }
    }


    init {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {


            override fun onStop(owner: LifecycleOwner) {
                getHodlerAudioRecorder().cancel()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                handler?.removeCallbacksAndMessages(null)
                handler = null
                lifeState = -1
                getHodlerAudioRecorder().release()
            }


        })
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun bindView(view: View) {
        view.setOnTouchListener (object :OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (enablePermisson != true ){
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if(ActivityCompat.checkSelfPermission(activity,Manifest.permission.RECORD_AUDIO)
                            == PackageManager.PERMISSION_GRANTED){
                            enablePermisson = true
                            return onTouch(v,event)
                        }
                        checkPermission()
                    }
                    return true
                }
              return  when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downY = event.y
                        dispatchDown(v, event, downY)
                        dispatchMoveNormalCancel(sumMove)
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        sumMove = downY - event.y
//                    this.sumMove = sumMove
                        dispatchMove(v, event, sumMove)
                        dispatchMoveUp(v, event, sumMove)
                        if (sumMove > 0) {
                            if (sumMove > ITouchSupportAudioRecorder.CANCEL_DP) {
                                if (recordingMoveState != RECORDING_READY_CANCEL_STATE) {
                                    recordingMoveState = RECORDING_READY_CANCEL_STATE
                                    dispatchMoveReadyCancel(sumMove)
                                }
                            } else if (recordingMoveState == RECORDING_READY_CANCEL_STATE) {
                                recordingMoveState = RECORDING_NORMAL_STATE
                                dispatchMoveNormalCancel(sumMove)
                            }
                        } else {
                            dispatchMoveDown(v, event, sumMove)
                        }
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (recordingMoveState == RECORDING_READY_CANCEL_STATE) {
                            dispatchUpCancelRecording(0)
                        } else {
                            dispatchUpFinishRecording()
                        }
                        dispatchUp(v, event, sumMove)
                        true
                    }

                    else -> {
                        false
                    }
                }
            }


        })
        if (lifeState == 0) {
            handler = Handler(Looper.getMainLooper())
        }
    }

    private fun checkPermission() {
        requestPermission.launch(android.Manifest.permission.RECORD_AUDIO)
    }


    override fun addTouchListener(listener: ITouchSupportAudioRecorder.TouchListener) {
        this.listener.add(listener)
    }

    override fun removeTouchLisener(listener: ITouchSupportAudioRecorder.TouchListener) {
        this.listener.remove(listener)
    }

    private fun dispatchMoveDown(v: View?, event: MotionEvent?, sumMove: Float) {
        for (l in listener) {
            l.onMoveDownEvent(abs(sumMove))
        }
    }

    private fun dispatchMoveUp(v: View?, event: MotionEvent?, sumMove: Float) {
        for (l in listener) {
            l.onMoveUpEvent(abs(sumMove))
        }
    }

    private fun dispatchDown(v: View?, event: MotionEvent?, downY: Float) {
        for (l in listener) {
            l.onDownEvent(downY)
        }
    }

    private fun dispatchMove(v: View?, event: MotionEvent?, sumMove: Float) {
        for (l in listener) {
            l.onMoveEvent(sumMove)
        }
    }

    private fun dispatchUp(v: View?, event: MotionEvent?, endSume: Float) {
        for (l in listener) {
            l.onUpEvent(endSume)
        }
    }

    private fun dispatchMoveNormalCancel(sumMove: Float) {
        for (l in listener) {
            l.onMoveNormalStateEvent(sumMove)
        }
    }

    private fun dispatchMoveReadyCancel(sumMove: Float) {
        for (l in listener) {
            l.onMoveReadyCancelEvent(sumMove)
        }
    }

    private fun dispatchUpCancelRecording(from: Int) {
        for (l in listener) {
            l.onUpCancelRecording(from)
        }
    }

    private fun dispatchUpFinishRecording() {
        for (l in listener) {
            l.onUpFinishRecording()
        }
    }


    override fun setAudioRecorderOperate(audioRecorder: IAudioRecorder) {
        this.audioRecorder = audioRecorder
    }

    override fun getHodlerAudioRecorder(): IAudioRecorder = audioRecorder


}