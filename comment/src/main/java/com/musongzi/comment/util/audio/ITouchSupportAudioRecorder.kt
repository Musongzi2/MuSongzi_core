package com.musongzi.comment.util.audio

import android.view.View
import com.musongzi.comment.ExtensionMethod.dp

/**
 * @Author : linhui

 * @Time : On 2024/8/20 11:55

 * @Description : ITouchSupportAudioRecorder
 */
interface ITouchSupportAudioRecorder : IHolderAudioRecorder {

    companion object{
         val CANCEL_DP = 100.dp()
    }

    fun bindView(view: View)

    fun addTouchListener(listener: TouchListener)

    fun removeTouchLisener(listener: TouchListener)

    fun setAudioRecorderOperate(audioRecorder: IAudioRecorder)
    interface TouchListener {
        fun onDownEvent(downY: Float){}
        fun onMoveEvent(sum: Float){}

        /**
         * 这里的值已经是绝对值
         */
        fun onMoveUpEvent(sumAbs: Float){}
        fun onMoveDownEvent(sumAbs: Float){}

        fun onUpEvent(endSum: Float){}
        fun onMoveReadyCancelEvent(sum:Float){}
        fun onMoveNormalStateEvent(sum:Float){}
        fun onUpCancelRecording(from:Int){}
        fun onUpFinishRecording(){}
    }
}