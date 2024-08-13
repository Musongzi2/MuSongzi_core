package com.musongzi.test.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.musongzi.core.ExtensionCoreMethod.adapter
import com.musongzi.core.ExtensionCoreMethod.getApi
import com.musongzi.core.ExtensionCoreMethod.linearLayoutManager
import com.musongzi.core.ExtensionCoreMethod.liveSaveStateObserver
import com.musongzi.core.ExtensionCoreMethod.refreshLayoutInit
import com.musongzi.core.ExtensionCoreMethod.saveStateChange
import com.musongzi.core.StringChooseBean
import com.musongzi.core.base.fragment.DataBindingFragment
import com.musongzi.core.base.manager.RetrofitManager
import com.musongzi.core.base.page2.PageLoader
import com.musongzi.core.base.page2.RequestObservableBean
import com.musongzi.core.base.page2.SimplePageCall
import com.musongzi.core.itf.page.IPageEngine
import com.musongzi.test.MszTestApi
import com.musongzi.test.api.SimpleApi
import com.musongzi.test.bean.ResponeCodeBean
import com.musongzi.test.databinding.FragmentRecyclePageBinding
import com.musongzi.test.databinding.ItemUserInfoBinding
import com.musongzi.test.vm.ListDataViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep
import java.util.Arrays

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecyclePageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclePageFragment : DataBindingFragment<FragmentRecyclePageBinding>() {

    private var pageLoad: IPageEngine<StringChooseBean>? = null
    val viewModel: ListDataViewModel by viewModels()

    override fun notifyDataSetChangedItem(postiont: Int) {
        dataBinding.idRecyclerView.adapter?.notifyItemChanged(postiont)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun notifyDataSetChanged() {
        dataBinding.idRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun initView() {

        "hahakey".liveSaveStateObserver<String>(this, viewModel) {
            Log.i(TAG, "ListDataViewModel liveSaveStateObserver: hahakey = $it")
            dataBinding.idTitle.text = it
        }

        dataBinding.idSmartRefreshLayout.refreshLayoutInit(safeGetPageEngine())

//        var playItemBean:UserInfo? = null

        if (dataBinding.idRecyclerView.adapter == null) {
            dataBinding.idRecyclerView.linearLayoutManager {
                safeGetPageEngine().adapter(ItemUserInfoBinding::class.java) { d, i, p ->
                    d.idTitleTv.setOnClickListener {
                        if (safePlayer().isPlaying) {
                            safePlayer().pause()
                        } else {
                            safePlayer().play()
                        }
//                        notifyDataSetChanged()

                    }
                }
            }
        }
    }

    private fun safeGetPageEngine(): IPageEngine<StringChooseBean> {
        if (pageLoad == null) {
            pageLoad = PageLoader.createInstance(object :
                SimplePageCall<StringChooseBean>(this@RecyclePageFragment) {
                override fun getRemoteData(page: Int, pageSize: Int): Observable<List<StringChooseBean>>? {
                    Log.i(TAG, "getRemoteData: page = $page , pageSize = $pageSize")
                    return getApi<MszTestApi>().getArrayEngine(page, pageSize).map {
                        it.data ?: mutableListOf()
                    }
//                    return Observable.create {
//                        val dataBean = ListDataBean<UserInfo>()
//                        it.onNext(dataBean.data.apply {
//
//
//                            if(page == 0) {
//                                add(UserInfo(System.currentTimeMillis(), "小米"))
//                                add(UserInfo(System.currentTimeMillis(), "小明"))
//                                add(UserInfo(System.currentTimeMillis(), "小白"))
//                                add(UserInfo(System.currentTimeMillis(), "小红", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小寻", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小志"))
//                                add(UserInfo(System.currentTimeMillis(), "小薰", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小强"))
//                                add(UserInfo(System.currentTimeMillis(), "小赞"))
//                                add(UserInfo(System.currentTimeMillis(), "小芸", gender = IUserInfo.WOMAN))
//                            }else if(page == 1) {
//                                add(UserInfo(System.currentTimeMillis(), "小欧"))
//                                add(UserInfo(System.currentTimeMillis(), "小林"))
//                                add(UserInfo(System.currentTimeMillis(), "小晨", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小婷", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小琪", gender = IUserInfo.WOMAN))
//                                add(UserInfo(System.currentTimeMillis(), "小国"))
//                                add(UserInfo(System.currentTimeMillis(), "小海"))
//                                add(UserInfo(System.currentTimeMillis(), "小痞"))
//                            }
//                        })
//                        it.onComplete()
//                    }
                }

                override fun pageSize(): Int = 10

                override fun handlerState(state: Int?) {
                    super.handlerState(state)
                    Log.i(TAG, "handlerState: $state")
                }

                override fun handlerDataChange(
                    data: MutableList<StringChooseBean>,
                    request: RequestObservableBean<List<StringChooseBean>>
                ) {
                    Log.i(TAG, "handlerDataChange: data.size = ${data.size}")
                    dataBinding.idRecyclerView.adapter?.notifyDataSetChanged()
                }
            }).apply {
//                setCheckDataEnd {
//                    (it?.size ?: 0) > 0
//                }
            }
        }
        return pageLoad!!
    }

    private var player: ExoPlayer? = null

    fun safePlayer() =
        if (player == null)
            ExoPlayer.Builder(this@RecyclePageFragment.getHolderContext()!!).build().apply {

//            val s: DataSource.Factory = DefaultDataSourceFactory(this@RecyclePageFragment.getHolderContext()!!)
//            val source = HlsMediaSource.Factory(s)
//
//
//            addMediaSource(source.createMediaSource(MediaItem.fromUri(music)))

                addMediaSource(DefaultMediaSourceFactory(this@RecyclePageFragment.requireContext()).createMediaSource(MediaItem.fromUri(music)))

//            addMediaItem()

                repeatMode = Player.REPEAT_MODE_ALL
                prepare()
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                Log.d(TAG, "onPlaybackStateChanged: STATE_BUFFERING")
                            }

                            Player.STATE_ENDED -> {
                                Log.d(TAG, "onPlaybackStateChanged: STATE_ENDED")
                            }

                            Player.STATE_IDLE -> {
                                Log.d(TAG, "onPlaybackStateChanged: STATE_IDLE")
                            }

                            Player.STATE_READY -> {
                                Log.d(TAG, "onPlaybackStateChanged: STATE_READY")
                            }
                        }
                    }
                })
                player = this
            } else {
            player!!
        }


    val music = "http://192.168.1.106:8080/我的刻苦铭心的恋人.mp3"

//    var sum = ObservableField(1)

    override fun initData() {

//        val job = viewmodel.viewModelScope.launch {
//            Toast.makeText(context, RetrofitManager.getInstance().getApi(SimpleApi::class.java).getArrayEngine(0, 2).data[0].title, Toast.LENGTH_SHORT).show()
//        }

//        job.cancel()

        viewModel.loadDataUser()

        ListDataViewModel.DATA.liveSaveStateObserver<List<StringChooseBean>?>(this, viewModel) {
            Log.i(TAG, "initData: ${if (it?.isEmpty() == true) null else Arrays.toString(it?.toTypedArray())}")
        }

    }

    override fun onDestroy() {
        player?.release()
        super.onDestroy()
    }

}