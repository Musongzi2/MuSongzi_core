package com.musongzi.core.itf.data;

import androidx.annotation.Nullable;

public interface ISimpleRespone<T> {

     @Nullable
     T getData();

     void setData(@Nullable T data);


}
