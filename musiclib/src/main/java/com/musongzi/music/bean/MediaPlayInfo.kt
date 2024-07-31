package com.musongzi.music.bean

import android.net.Uri
import com.musongzi.comment.ExtensionMethod.getResUri
import com.musongzi.core.itf.IAttribute
import com.musongzi.core.itf.IHolderRes
import com.musongzi.core.itf.IHolderUri
import com.musongzi.music.itf.IMediaPlayInfo

/*** created by linhui * on 2022/7/28 */
open class MediaPlayInfo(var musicName: String? = null,uri: String? = null) : IMediaPlayInfo {



    private var valueFlag = 0;
    private var stringUrl: String? = ""
        set(value) {
            valueFlag = 0
            if (mediaId.isEmpty() && value != null) {
                mediaId = value
            }
            field = value
        }
    init {
        stringUrl = uri
    }
    private var mediaId: String = ""

    private var androidRes = 0
        set(value) {
            valueFlag = 1
            field = value
        }

    override fun setUri(uri: Uri) {
        stringUrl = uri.toString()
    }

    override fun setUriString(dataString: String) {
        stringUrl = dataString
    }

    override fun setRes(res: Int) {
        androidRes = res;
    }

    override fun getHolderSting(): String? = stringUrl

    override fun getHolderRes(): Int? {
        TODO("Not yet implemented")
    }

    override fun holderFlag(): Int {
        return valueFlag
    }

    override fun getHolderUri(): Uri? {
        return if (stringUrl == null) androidRes.getResUri() else Uri.parse(stringUrl);
    }


    override fun getAttributeId(): String {
        return mediaId
    }

    override fun equals(other: Any?): Boolean {
        return other?.let {
            if (other is MusicPlayInfoImpl && attributeId == other.attributeId) {
                return true
            } else {
                super.equals(other)
            }
        } ?: false
    }

    override fun hashCode(): Int {
        var result = valueFlag
        result = 31 * result + (stringUrl?.hashCode() ?: 0)
        result = 31 * result + (musicName?.hashCode() ?: 0)
        result = 31 * result + mediaId.hashCode()
        result = 31 * result + androidRes
        return result
    }

}