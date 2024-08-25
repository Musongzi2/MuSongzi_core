package com.musongzi.test.fragment

import android.content.ContentProvider
import android.content.ContentResolver
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.mszsupport.util.ScreenUtil
import com.musongzi.comment.ExtensionMethod.dp
import com.musongzi.core.ExtensionCoreMethod.adapter
import com.musongzi.core.ExtensionCoreMethod.gridLayoutManager
import com.musongzi.core.ExtensionCoreMethod.refreshLayoutInit
import com.musongzi.core.base.fragment.BaseLayoutFragment
import com.musongzi.core.base.page3.PageLoader
import com.musongzi.core.itf.page.ISource
import com.musongzi.test.R
import com.musongzi.test.databinding.ItemPhotoBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
create by linhui , data = 2024/8/24 15:00
 **/
class PhotoSelectFragment : BaseLayoutFragment() {

    private val smartRefreshLayout by lazy {
        view?.findViewById<SmartRefreshLayout>(R.id.id_SmartRefreshLayout)?.apply {
            setNoMoreData(false)
        }
    }

    private val recyclerView by lazy {
        view?.findViewById<RecyclerView>(R.id.id_recyclerView)
    }

    private val read by lazy {
        PageLoader.Build { p, s ->
            loadRemoteData(p, s)
        }.dataChange {
            Log.i(TAG, "dataChange: it.size = ${it.size}")
            recyclerView?.adapter?.notifyDataSetChanged()
        }.pageSize(60).onMoreNext {
            smartRefreshLayout?.setNoMoreData(true)
            smartRefreshLayout?.setEnableFooterFollowWhenNoMoreData(true)
        }.onRefresh { page, size ->
            smartRefreshLayout?.setNoMoreData(false)
        }.lifecycleOwner(this@PhotoSelectFragment).build()
    }

    override fun getLayoutId() = R.layout.fragment_select_photo

    override fun initData() {


//
//        read.refresh()

    }

    private fun loadRemoteData(page: Int, pageSize: Int): Flow<List<String>> {
        val cursor = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = Bundle()
            bundle.putInt(ContentResolver.QUERY_ARG_OFFSET, pageSize * page)
            bundle.putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize + 1)
            bundle.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "${MediaStore.Images.Media.DATE_MODIFIED} desc")
            bundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, "? > 0")
            bundle.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(MediaStore.Images.Media.SIZE))
            context?.contentResolver?.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE), bundle, null)
        } else {
            context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE), "_size > 0", null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc limit $pageSize OFFSET ${pageSize * page}"
            )
        }


        val mutableList = mutableListOf<String>()
        cursor?.moveToFirst()

        while (cursor?.moveToNext() == true) {
            val p = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            if (p >= 0) {
                val data = cursor.getString(p)
                mutableList.add(data)
            }
        }
        return MutableStateFlow(mutableList)
    }

    override fun initView() {
        //  TODO("Not yet implemented")

        requireActivity().registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                loadData()
            }
        }.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

//    private val pageEngine: ICataloguePage2<String, List<String>> by lazy {
//        PageLoader.createInstance(object : SimplePageCall<String>(this@PhotoSelectFragment) {
//            override fun getRemoteData(page: Int, pageSize: Int): Observable<List<String>> {
//                return Observable.create {
//                    Log.i(TAG, "getRemoteData: page = $page , pageSize = $pageSize")
//                    val cursor = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//                        val bundle = Bundle()
//                        bundle.putInt(ContentResolver.QUERY_ARG_OFFSET, pageSize * page)
//                        bundle.putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize + 1)
//                        bundle.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, "${MediaStore.Images.Media.DATE_MODIFIED} desc")
//                        bundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, "? > 0")
//                        bundle.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(MediaStore.Images.Media.SIZE))
//                        context?.contentResolver?.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE), bundle, null)
//                    } else {
//                        context?.contentResolver?.query(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE), "_size > 0", null,
//                            MediaStore.Images.Media.DATE_MODIFIED + " desc limit $pageSize OFFSET ${pageSize * page}"
//                        )
//                    }
//
//
//                    val mutableList = mutableListOf<String>()
//                    cursor?.moveToFirst()
//
//                    while (cursor?.moveToNext() == true) {
//                        val data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
//                        mutableList.add(data)
//                    }
//
//                    Log.i(TAG, "getRemoteData: size = ${mutableList.size}")
//                    cursor?.close()
//                    it.onNext(mutableList)
//                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).bindToLifecycle(this@PhotoSelectFragment).map {
//                    it as List<String>
//                }
//            }
//
//            override fun transformDataToList(entity: List<String>?): MutableList<String> {
//                return super.transformDataToList(entity).apply {
//                    view?.findViewById<RecyclerView>(R.id.id_recyclerView)?.apply {
//                        adapter?.notifyDataSetChanged()
//
//                    }
//                    view?.findViewById<SmartRefreshLayout>(R.id.id_SmartRefreshLayout)?.apply {
//                        finishRefresh()
//                        finishLoadMore()
//                    }
//                }
//
//            }
//
////            override fun thisStartPage(): Int {
////                return 1;
////            }
//
//            override fun pageSize(): Int {
//                return 30
//            }
//
//        })
//    }

    private fun loadData() {

//        lifecycleScope.launch(Dispatchers.IO) {


//            withContext(lifecycleScope.coroutineContext) {


        smartRefreshLayout?.apply {

            refreshLayoutInit(read)

        }


        recyclerView?.apply {
            val _5 = 5.dp().toInt()
            val span = 3
            val width = (ScreenUtil.getScreenWidth() - 2 * 3 * _5) / span
            gridLayoutManager(span) {
                (read as ISource<String>).adapter(ItemPhotoBinding::class.java, { d, i ->
                    d.idImage.layoutParams.width = width
                    d.idImage.layoutParams.height = width
                }, { d, i, p ->

                })
            }



            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.set(_5, _5, _5, _5)
                }
            })
            read.refresh()
        }
//            }


//        }

    }
}