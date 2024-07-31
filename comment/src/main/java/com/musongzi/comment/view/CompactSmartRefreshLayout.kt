package com.musongzi.comment.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.musongzi.comment.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader

/**
create by linhui , data = 2024/7/31 19:55
 **/
open class CompactSmartRefreshLayout(context: Context?, attrs: AttributeSet?) :
    SmartRefreshLayout(context, attrs) {

    protected fun initSetRefreshHeader(mRefreshHeader: RefreshHeader?) {
        setRefreshHeader(mRefreshHeader ?: ClassicsHeader(context))
    }

    protected fun initSetRefreshFooter(mRefreshFooter: RefreshFooter?) {
        setRefreshFooter(mRefreshFooter ?: ClassicsFooter(context))
    }

    init {
        var heart: String? = null
        var foolter: String? = null
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable") val array =
                context?.obtainStyledAttributes(attrs, R.styleable.refresh_layout)
            if (array != null) {
                heart = array.getString(R.styleable.refresh_layout_heart_view)
                foolter = array.getString(R.styleable.refresh_layout_footer_view)
                array.recycle()
            }
        }
        var mRefreshHeader: RefreshHeader? = null
        if (heart == null) {
//            mRefreshHeader = ClassicsHeader(context)
        } else {
            try {
                mRefreshHeader = Class.forName(heart).getDeclaredConstructor(Context::class.java)
                    .newInstance(context) as RefreshHeader
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        initSetRefreshHeader(mRefreshHeader)
        var mRefreshFooter: RefreshFooter? = null
        if (foolter == null) {
            //setRefreshFooter(SimpleFooterView(context))
        } else {

            try {
                mRefreshFooter = Class.forName(foolter).getDeclaredConstructor(Context::class.java)
                    .newInstance(context) as RefreshFooter
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            setRefreshFooter(view ?: ClassicsFooter(context))
        }
        initSetRefreshFooter(mRefreshFooter)
    }

}