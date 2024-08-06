package com.musongzi.test.spi;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.musongzi.spi.ISpiRequest;

public class MyTestSpiImp implements ISpiRequest {
    @NonNull
    @Override
    public Class<?> getRequestLoaderClass() {
        return Test.class;
    }

    @NonNull
    @Override
    public String orderName() {
        return "MyTestSpiImp";
    }


    public static class Test{


        public void hello(){
            Log.d("MyTestSpiImp", "hello: --->");
        }

    }

}
