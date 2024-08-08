package com.musongzi.test.bean;

import com.musongzi.core.itf.data.ISimpleRespone;

public class ResponeCodeBean<T> implements ISimpleRespone<T> {

    private int code;

    private String msg;

    private T data;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    public ResponeCodeBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponeCodeBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
