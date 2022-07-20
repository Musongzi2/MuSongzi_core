
package com.musongzi.comment.util

import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.ViewTarget
import com.musongzi.core.util.ScreenUtil.SCREEN_1_3_WDITH

/*** created by linhui * on 2022/7/20 */


var placeholderId: Int = 0;
var errorId: Int = 0;


fun RequestBuilder<Drawable>.placeholderRes(): RequestBuilder<Drawable> {
    return if (placeholderId != 0) {
        placeholder(placeholderId)
    } else {
        this
    }
}

fun RequestBuilder<Drawable>.errorRes(): RequestBuilder<Drawable> {
    return if (errorId != 0) {
        error(errorId)
    } else {
        this
    }
}

fun RequestBuilder<Drawable>.memoryCacheStrategy(): RequestBuilder<Drawable> {
    return this
}

fun RequestManager.loadByAny(res: Any): RequestBuilder<Drawable> {
    return if (res is Int) {
        load(res)
    } else if (res is Uri) {
        load(res)
    } else if (res is String) {
        load(res)
    } else {
        load(res)
    }
}

fun RequestBuilder<Drawable>.overrideInto(
    imageView: ImageView,
    conifgOverride: (() -> Pair<Int, Int>)? = null
): ViewTarget<ImageView, *> {
    return conifgOverride?.invoke()?.let {
        override(it.first, it.second).into(imageView)
    } ?: into(imageView)
}

fun ImageView.showImage(uri: Any, conifgOverride: (() -> Pair<Int, Int>)? = null) {
    Glide.with(this).loadByAny(uri).placeholderRes().errorRes().memoryCacheStrategy()
        .overrideInto(this, conifgOverride)
}

@BindingAdapter("setTextNormal")
fun setText(textView: TextView, str: CharSequence) {
    textView.text = str
}

@BindingAdapter("setTextNormal")
fun setText(textView: TextView, res: Int) {
    textView.setText(res)
}

@BindingAdapter("imageLoadRect")
fun imageLoadRect(image: ImageView, uri: Uri) {
    image.showImage(uri) {
        SCREEN_1_3_WDITH to SCREEN_1_3_WDITH
    }
}

@BindingAdapter("imageLoadRect")
fun imageLoadRect(image: ImageView, id: Int) {
    image.showImage(id) {
        SCREEN_1_3_WDITH to SCREEN_1_3_WDITH
    }
}

@BindingAdapter("imageLoadRect")
fun imageLoadRect(image: ImageView, uri: String) {
    image.showImage(uri) {
        SCREEN_1_3_WDITH to SCREEN_1_3_WDITH
    }
}

@BindingAdapter("imageLoadNormal")
fun imageLoadNormal(image: ImageView, uri: Uri) {
    image.showImage(uri)
}

@BindingAdapter("imageLoadNormal")
fun imageLoadNormal(image: ImageView, res: Int) {
    image.showImage(res)
}

@BindingAdapter("imageLoadNormal")
fun imageLoadNormal(image: ImageView, uri: String) {
    image.showImage(uri)
}


