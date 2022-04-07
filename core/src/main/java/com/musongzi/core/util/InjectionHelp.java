package com.musongzi.core.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.musongzi.core.base.business.collection.ListEngine;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class InjectionHelp {


    public static ListEngine findAnnotation(Class<?> thisClazz, Class<ListEngine> listEngineClass) {
//        if(thisClazz.getName().equals("java.lang.Object")){
//            return null;
//        }
        ListEngine listEngine = thisClazz.getAnnotation(listEngineClass);
        if (listEngine == null) {
            try {
                return findAnnotation(thisClazz.getSuperclass(), listEngineClass);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return listEngine;
        }
    }

    @Nullable
    public static <D extends ViewDataBinding> D findDataBinding(Class<?> aClass, ViewGroup parent, String name, int actualTypeArgumentsIndex) {

//        Log.i(TAG, "getDataBinding: " + aClass.getSuperclass().getName() + " , " + name);
        if (aClass.getSuperclass().getName().equals(name)) {
            Class c = null;
            //获取所有父类的泛型
            Type[] types = ((ParameterizedType) aClass.getGenericSuperclass()).getActualTypeArguments();

            for (int i = 0; i < types.length; i++) {
                //判断指定下标是否一致
                if (actualTypeArgumentsIndex == i) {
                    //一致转成class，class默认继承Type接口
                    c = (Class) types[i];
                    break;
                }
            }
            try {
                //反射获取Databinding通用的静态inflate方法
                Method method = c.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                //根据获取到泛型c class ，调用函数方法初始化指定泛型并且返回
                return (D) method.invoke(null, LayoutInflater.from(ActivityThreadHelp.getCurrentApplication()), parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //如果父类不符合名字要求，则递归往上层继续寻找
            return findDataBinding(aClass.getSuperclass(), parent, name, actualTypeArgumentsIndex);
        }

    }


    public static <T> Class<T> findGenericClass(@NotNull Class<?> aClass, int actualTypeArgumentsViewModelIndex) {
        Type type = aClass.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length > actualTypeArgumentsViewModelIndex) {
                return (Class<T>) types[actualTypeArgumentsViewModelIndex];
            }
        }
        return findGenericClass(aClass.getSuperclass(), actualTypeArgumentsViewModelIndex);
    }

    @org.jetbrains.annotations.Nullable
    public static <V> WeakReference<V> findViewModel(@NotNull Class<?> javaClass, ViewModelProvider viewModelProvider, int actualTypeArgumentsViewModelIndex) {

        Type type = javaClass.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            if (types.length > actualTypeArgumentsViewModelIndex) {
                Class c = (Class) types[actualTypeArgumentsViewModelIndex];
                new WeakReference(viewModelProvider.get(c));
            }
        }
       return findViewModel(javaClass, viewModelProvider, actualTypeArgumentsViewModelIndex);
    }
}