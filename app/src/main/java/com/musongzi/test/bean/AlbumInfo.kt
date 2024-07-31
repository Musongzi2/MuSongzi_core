package com.musongzi.test.bean

import com.musongzi.core.base.bean.ICovertInfo
import kotlinx.android.parcel.Parcelize

/*** created by linhui * on 2022/8/3 */
class AlbumInfo : SongArrayInfo(), ICovertInfo {

    private var mCover: String? = null

    var likeCount = 0
//    override fun getCovert(): String? {
//        return cover
//    }

    override fun getCover(): String? {
        return mCover
    }

}