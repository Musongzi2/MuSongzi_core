package com.musongzi.core.base.page2;


import com.musongzi.core.itf.page.IRead;

public interface FlipListener {

    boolean onRefreshBefore(IRead read);

    boolean onLoadMoreBefore(IRead read);


}