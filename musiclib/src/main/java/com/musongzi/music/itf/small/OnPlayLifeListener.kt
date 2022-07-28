package com.musongzi.music.itf.small

import com.musongzi.music.bean.MediaPlayInfo
import com.musongzi.music.bean.MusicPlayInfo

/*** created by linhui * on 2022/7/28 */
interface OnPlayLifeListener {
    fun onStart(info: MediaPlayInfo)
    fun onStop(info: MediaPlayInfo)
    fun onPause(info: MediaPlayInfo)
}