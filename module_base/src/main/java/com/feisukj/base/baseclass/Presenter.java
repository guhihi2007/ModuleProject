package com.feisukj.base.baseclass;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
