package com.musongzi.comment.util.audio

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.musongzi.comment.bean.RecordAudioInfo

/**
 * @Author : linhui

 * @Time : On 2024/8/19 17:44

 * @Description : IAudioRecorder
 */
interface IAudioRecorder {


    fun hasState(state:Int):Boolean

    fun pre()

    fun start()

    fun pause()

    fun stop()

    fun cancel()

    fun release()

    fun getMaxTime() = MAX_TIME

    fun addOnProcessListener(lifecycleOwner: LifecycleOwner?,listener: OnProcessListener)

    fun addOnSuccedListener(lifecycleOwner: LifecycleOwner?,listener: OnSuccedListener)

    fun addOnStateListener(lifecycleOwner: LifecycleOwner?,listener: OnStateChangeListener)

    interface OnProcessListener{
        fun onProcess(readSize:Long,time:Long)
    }

    interface OnSuccedListener :Observer<RecordAudioInfo>

    interface OnStateChangeListener : Observer<Int>

    companion object {
        const val MIN_TIME = 1_100
        const val MAX_TIME = 60_000
        const val NORMAL_STATE = 0
        const val INIT_SATTE = 1
        const val PRE_STATE = 4
        const val START_STATE = 8
        const val RECORDING_STATE = 16
        const val STOP_STATE = 128
        const val CANCEL_STATE = 32
        const val CANCEL_ERROR_STATE = 64
        const val RELEASE_SATTE = 256

        /**
         * 添加aac格式中，每个帧的adts头
         * 注意这里的默认是44100k 通道2 取样精度16
         */
        fun addADTStoPacket(packet: ByteArray, packetLen: Int) {
            val profile = 2 // AAC LC
            val freqIdx = 4 // 44.1KHz
            val chanCfg = 2 // CPE


            // fill in ADTS data
            packet[0] = 0xFF.toByte()
            packet[1] = 0xF9.toByte()
            packet[2] = (((profile - 1) shl 6) + (freqIdx shl 2) + (chanCfg shr 2)).toByte()
            packet[3] = (((chanCfg and 3) shl 6) + (packetLen shr 11)).toByte()
            packet[4] = ((packetLen and 0x7FF) shr 3).toByte()
            packet[5] = (((packetLen and 7) shl 5) + 0x1F).toByte()
            packet[6] = 0xFC.toByte()
        }
    }
}