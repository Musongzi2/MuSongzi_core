package com.musongzi.music.itf

import com.musongzi.music.bean.MusicPlayInfo
import com.musongzi.music.itf.small.OnReadyListener

/*** created by linhui * on 2022/7/28 */
interface PlayMusicPostionObervser : OnReadyListener {

    fun onMusicPostionChange(musicPlayInfo: MusicPlayInfo, postion: Long)


}