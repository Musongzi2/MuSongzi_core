package com.musongzi.comment.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat


/**
 * @Author : linhui

 * @Time : On 2024/8/22 14:35

 * @Description : RecordAudioInfo
 */
@Parcelize
data class RecordAudioInfo(
    val outputPath: String,
    var duration: Long,
    /**
     * 1 "audio/ mp4a-latm" - AAC audio (note, this is raw AAC packets, not packaged in LATM!)
     * 2 = "audio/ 3gpp" - AMR narrowband audio
     * 3 = "audio/ amr-wb" - AMR wideband audio
     * 4 "audio/ mpeg" - MPEG1/ 2 audio layer III
     * 5 "audio/ vorbis" - vorbis audio
     * 6 "audio/ g711-alaw" - G.711 alaw audio
     * 7 "audio/ g711-mlaw" - G.711 ulaw audio
     */
    var format: Int = 1
) : Parcelable {

    fun asShowDuration() = duration.formatTime("mm:ss")

    fun asMusicCodeType(): String {
        return when (format) {
            1 -> {
                "audio/mp4a-latm"
            }
            4->{
                "audio/mpeg"
            }
            else -> {
                ""
            }
        }
    }

}

fun Long.formatTime(format: String = "mm:ss"): String {
    // TODO: 时间的解析
    return SimpleDateFormat(format).format(this)
}


