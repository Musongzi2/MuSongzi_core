package com.musongzi.core.base.page2;

import androidx.annotation.Nullable;

public interface PageFactory {


    @Nullable
    default <T, D> ICataloguePage2<T, D> createInstance(PageCallBack<T, D> callBack){
        return PageLoader.instance(callBack);
    }

    String TAG = "PageFactory";

}
