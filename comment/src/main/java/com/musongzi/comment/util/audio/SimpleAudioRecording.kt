package com.musongzi.comment.util.audio

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.musongzi.comment.bean.RecordAudioInfo
import com.musongzi.comment.util.audio.IAudioRecorder.Companion.MIN_TIME
import com.musongzi.comment.util.audio.IAudioRecorder.Companion.addADTStoPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer


/**
 * @Author : linhui

 * @Time : On 2024/8/19 17:25

 * @Description : SimpleRecording
 */
open class SimpleAudioRecording(val activity: AppCompatActivity) : IAudioRecorder {

    companion object {
        const val TAG = "SimpleRecording"


        // ADTS固定头字段
//        private static
        const val PROFILE: Int = 2 // AAC Low Complexity (LC)
//        private static
        const val SAMPLING_FREQUENCY_INDEX: Int = 4 // 44.1kHz
//        private static
        const val CHANNEL_CONFIGURATION: Int = 2 // 立体声
//        private static
        const val FRAME_LENGTH_FLAG: Int = 0 // 使用13位frameLength
//        private static
        const val BUFFER_FULLNESS: Int = 0x7FF // 缓冲区满度
//        private static
        const val NUMBER_OF_RAW_DATA_BLOCKS_IN_FRAME: Int = 1 // 一个AAC帧中只包含一个数据块

        // 计算ADTS头
        fun createADTSHeader(frameSize: Int): ByteArray {
            val profile = (PROFILE shl 6) or (SAMPLING_FREQUENCY_INDEX shl 2) or (FRAME_LENGTH_FLAG shr 1)
            val frameLength = (frameSize + 7) / 8 // 转换为字节，并向上取整
            val crcCheck = 0 // 0表示没有CRC校验


            // 计算ADTS头的12个字节
            val header = ByteArray(7)
            header[0] = 0xFF.toByte() // 同步字节
            header[1] = 0xF9.toByte() // 同步字节的下一字节，包含层信息和保护位
            header[1] = (header[1].toInt() or (((PROFILE - 1) shl 6) + (SAMPLING_FREQUENCY_INDEX shl 2) + (FRAME_LENGTH_FLAG shr 1)).toByte()
                .toInt()).toByte()
            header[2] = (((FRAME_LENGTH_FLAG and 0x01) shl 7) + (CHANNEL_CONFIGURATION shl 3) + (crcCheck and 0x07)).toByte()
            header[3] = (((frameLength and 0x1F80) shr 7) or 0x7F).toByte() // 帧长度的高位和ADTS缓冲满度的高位
            header[4] = ((frameLength and 0x007F) shl 1).toByte() // 帧长度的低位
            header[5] = ((BUFFER_FULLNESS and 0x1F80) shr 3).toByte() // ADTS缓冲满度的低位
            header[6] = (((BUFFER_FULLNESS and 0x007F) shl 5) or (NUMBER_OF_RAW_DATA_BLOCKS_IN_FRAME and 0x1F)).toByte()


            // 如果帧长度超过127字节（即frameLength的低7位不足以表示），则需要调整header[3]和header[4]
            if (frameLength > 0x007F) {
                header[3] = (header[3].toInt() or 0x00).toByte() // 清除header[3]中的frameLength高3位
                header[4] =
                    (header[4].toInt() or ((frameLength and 0x1800) shr 11)).toByte() // 将frameLength的高2位移到header[4]的低2位
            }

            return header
        }

    }

    private val mProcessListeners = mutableListOf<IAudioRecorder.OnProcessListener>()
    private val mSuccedListeners = mutableListOf<IAudioRecorder.OnSuccedListener>()
    private val mStateListeners = mutableListOf<IAudioRecorder.OnStateChangeListener>()

    private var realAudioRecord: AudioRecord? = null
    private var medaiCode: MediaCodec? = null
    private lateinit var mediaFormat:MediaFormat

//    var isPre = true

    internal var operateState = IAudioRecorder.NORMAL_STATE
        set(value) {
            dispatchState(value)
            field = value
        }

    var currentTime = 0L
    var limite = 0L
    private val sampleRate: Int = 44100 // CD音质
    private val channelConfig: Int = AudioFormat.CHANNEL_IN_STEREO
    private val audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize: Int =
        AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private var buffer: ByteArray = ByteArray(bufferSize)

//    private val errorInts =
//        intArrayOf(IAudioRecorder.CANCEL_ERROR_STATE, IAudioRecorder.CANCEL_STATE)

    override fun pre() {

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
//        realAudioRecord?.apply {
//            try {
//                release()
//            } finally {
//
//            }
//
//        }
        realAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )
        if (medaiCode == null) {
            medaiCode = MediaCodec.createEncoderByType("audio/mp4a-latm").apply {
                mediaFormat = MediaFormat.createAudioFormat(
                    "audio/mp4a-latm",
                    sampleRate,
                    channelConfig
                )
                mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 2);
                mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 44100);
                mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 100 * 1024);



            }

        }
        operateState = IAudioRecorder.PRE_STATE
    }

    override fun start() {

        if (!hasState(IAudioRecorder.PRE_STATE)) {
            return
        }
        activity.lifecycleScope.launch(Dispatchers.IO) {

            withContext(activity.lifecycleScope.coroutineContext) {
                operateState = IAudioRecorder.START_STATE
            }
            innerStart()
        }
    }

    private suspend fun innerStart() {
//        if (hasOperateFlag(IAudioRecorder.START_STATE)) {
        var readSize = 0
        var maxReadSize = 0L
        realAudioRecord?.let { audioRecord ->
            val p = getVoiceOuputstream()
            val ouputSteam = p?.first ?: return
            try {

//                val totalAudioLen = 0;
//                val totalDataLen = totalAudioLen + 36L;
//                val longSampleRate = sampleRate;
//                val channels = 2;
//                val byteRate = 16 * sampleRate * channels / 8L;
//
//                // 写入WAV文件头（如果需要）
//                writeWaveFileHeader(
//                    ouputSteam,
//                    totalAudioLen,
//                    totalDataLen,
//                    longSampleRate,
//                    channels,
//                    byteRate
//                );


                val startTime = System.currentTimeMillis();
                audioRecord.startRecording()

                val medaiCode = medaiCode!!
                medaiCode.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
                medaiCode.start()

//                val inputBuffer = medaiCode.getInputBuffer()
                val adtsFrame = ByteArray(7)
                val bufferInfo = MediaCodec.BufferInfo()
//                val bufferInfo = MediaCodec.BufferInfo()

                readSize = audioRecord.read(buffer, 0, bufferSize)
                while (readSize != AudioRecord.ERROR_INVALID_OPERATION
                    && readSize != AudioRecord.ERROR_BAD_VALUE && readSize != -1
                ) {
//                    AppLog.d(TAG, "recording ing --->")
                    maxReadSize += readSize
//                    AppLog.d(TAG, "recording ing maxReadSize = $maxReadSize")
                    if (currentTime >= IAudioRecorder.MAX_TIME) {
                        operateState = IAudioRecorder.STOP_STATE
                        break
                    }


                    if (hasState(IAudioRecorder.STOP_STATE)
                        || hasState(IAudioRecorder.CANCEL_STATE)
                        || hasState(IAudioRecorder.RELEASE_SATTE)
                    ) {
                        break
                    }
                    if (!hasState(IAudioRecorder.RECORDING_STATE)) {
                        withContext(activity.lifecycleScope.coroutineContext) {
                            operateState = IAudioRecorder.RECORDING_STATE
                        }
                    }

                    currentTime = System.currentTimeMillis() - startTime;
                    limite = currentTime - limite
                    if (limite > 200) {
                        limite = currentTime
                        withContext(Dispatchers.Main) {

                            dispatchProcess(maxReadSize, currentTime)
                        }
                    }


                    val inputBufferIndex = medaiCode.dequeueInputBuffer(1000)

                    if (inputBufferIndex > 0) {
                        val inputBuffer = medaiCode.getInputBuffer(inputBufferIndex)
                        inputBuffer?.put(buffer, 0, readSize)
                        medaiCode.queueInputBuffer(inputBufferIndex, 0, readSize, 0, 0);
                    }


                    var outputBufferIndex: Int = medaiCode.dequeueOutputBuffer(bufferInfo, 10000)
                    while (outputBufferIndex >= 0) {
                        val outputBuffer: ByteBuffer? = medaiCode.getOutputBuffer(outputBufferIndex)

                        if (bufferInfo.size > 0 && outputBuffer != null) {
                            val data = ByteArray(bufferInfo.size)
                            outputBuffer.get(data)
                            addADTStoPacket(adtsFrame, data.size + 7)
                            ouputSteam.write(adtsFrame,0,7)
                            ouputSteam.write(data)
                        }

                        // 处理输出数据
                        medaiCode.releaseOutputBuffer(outputBufferIndex, true)
                        outputBufferIndex = medaiCode.dequeueOutputBuffer(bufferInfo, 10000)
                    }


                    readSize = audioRecord.read(buffer, 0, bufferSize)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                audioRecord.stop()
                audioRecord.release()
                realAudioRecord = null
                try {
                    ouputSteam.close()
                    medaiCode?.stop()
                    medaiCode?.reset()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (hasState(IAudioRecorder.CANCEL_STATE)
                    || hasState(IAudioRecorder.RELEASE_SATTE) || currentTime < MIN_TIME
                ) {
                    if (readSize > 0) {
                        p.second.apply {
                            val f = File(this)
                            if (f.delete()) {
                            } else {
                            }
                        }

                    }
                } else if (hasState(IAudioRecorder.STOP_STATE)) {
                    dispatchSucced(RecordAudioInfo(p.second,currentTime))
                }
                currentTime = 0
                limite = 0
            }
        }


//        }
    }

    override fun hasState(preState: Int): Boolean {
        return operateState == preState
    }

    private fun getVoiceOuputstream(): Pair<OutputStream?, String>? {

        return File(activity.cacheDir, "send_audio_${System.currentTimeMillis()}.aac").let {
            FileOutputStream(it) to it.absolutePath
        }
//        return File(activity.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "send_audio_${System.currentTimeMillis()}.aac").let {
//            FileOutputStream(it) to it.absolutePath
//        }
    }


    override fun pause() {
//        operateState = IAudioRecorder
    }

    override fun stop() {
        operateState = IAudioRecorder.STOP_STATE
    }

    override fun cancel() {
        operateState = IAudioRecorder.CANCEL_STATE
    }

    override fun release() {
        operateState = IAudioRecorder.RELEASE_SATTE
    }


    private fun dispatchProcess(leng: Long, currentTime: Long) {
        for (l in mProcessListeners) {
            l.onProcess(leng, currentTime)
        }
    }

    private fun dispatchState(state: Int) {
        for (l in mStateListeners) {
            l.onChanged(state)
        }
    }

    private fun dispatchSucced(info: RecordAudioInfo) {
        for (l in mSuccedListeners) {
            l.onChanged(info)
        }
    }


    override fun addOnProcessListener(
        lifecycleOwner: LifecycleOwner?,
        listener: IAudioRecorder.OnProcessListener
    ) {
        mProcessListeners.add(listener)
    }

    override fun addOnSuccedListener(
        lifecycleOwner: LifecycleOwner?,
        listener: IAudioRecorder.OnSuccedListener
    ) {
        mSuccedListeners.add(listener)
    }

    override fun addOnStateListener(
        lifecycleOwner: LifecycleOwner?,
        listener: IAudioRecorder.OnStateChangeListener
    ) {
        mStateListeners.add(listener)
    }



}