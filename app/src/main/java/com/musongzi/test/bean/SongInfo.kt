package com.musongzi.test.bean

import com.musongzi.core.StringChooseBean
import com.musongzi.core.base.bean.ICovertInfo
import com.musongzi.music.bean.MediaConfig
import com.musongzi.music.bean.MusicPlayInfoImpl
import com.musongzi.music.itf.IMediaPlayInfo
import kotlinx.android.parcel.Parcelize

/*** created by linhui * on 2022/8/3 */

class SongInfo(
    musicName: String? = null,
    var artist: String? = "",
    var createTime: Long = System.currentTimeMillis(),
    var album: AlbumInfo? = null,
    var styles: List<StringChooseBean> = ArrayList(),
    private var mCover: String? = null,
    musicConfig: MediaConfig? = null
) : MusicPlayInfoImpl(musicConfig, musicName), ICovertInfo {


    override fun getCover(): String? {
        return mCover
    }


}