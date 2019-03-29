package com.feisu.greendao.radio;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Author : Gupingping
 * Date : 2019/1/21
 * QQ : 464955343
 */
@Entity

public class SearchHistoryEntity {
    @Id(autoincrement = false)
    private String key;
    private long time;
    @Generated(hash = 298103995)
    public SearchHistoryEntity(String key, long time) {
        this.key = key;
        this.time = time;
    }

    @Generated(hash = 691068747)
    public SearchHistoryEntity() {
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
