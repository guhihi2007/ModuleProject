package com.feisukj.base.baseclass;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 网络返回消息，最外层解析实体
 */
public class HttpResult<T> {
    private int errcode;
    private String errmsg;
    private T data;

    public int getCode() {
        return errcode;
    }

    public void setCode(int code) {
        this.errcode = code;
    }

    public String getMsg() {
        return errmsg;
    }

    public void setMsg(String msg) {
        this.errmsg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}
