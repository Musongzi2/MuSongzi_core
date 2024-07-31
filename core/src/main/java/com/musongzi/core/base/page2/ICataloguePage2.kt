package com.musongzi.core.base.page2

import com.musongzi.core.base.page.ICataloguePage
import com.musongzi.core.itf.page.IPageEngine2


interface ICataloguePage2<T, D> : ICataloguePage<T>, IPageEngine2<T, D> {

    var flipListener: FlipListener?

}