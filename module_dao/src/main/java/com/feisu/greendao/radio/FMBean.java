package com.feisu.greendao.radio;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Author : Gupingping
 * Date : 2019/1/15
 * QQ : 464955343
 */
@Entity
public class FMBean {

    /**
     * radioId : 74
     * name : CNR中国之声
     * parentId : 73
     * radioFm : FM106.1
     * radioUrl : http://audio1.china-plus.net:31080/10.102.62.44/radios/100001/index_100001.m3u8
     * backUrl : null
     * state : 1
     * sort : 1
     * level : 3
     * isExisUrl : 1
     * addTime : null
     * updateTime : 1503572700000
     */

    private int radioId;
    private String name;
    private int parentId;
    @Id
    private String radioFm;
    private String radioUrl;
    private String backUrl;
    private int state;
    private int sort;
    private int level;
    private int isExisUrl;
    private String addTime;
    private long updateTime;

    public FMBean(String name, String radioFm, String radioUrl) {
        this.name = name;
        this.radioFm = radioFm;
        this.radioUrl = radioUrl;
    }



    @Generated(hash = 1444454602)
    public FMBean(int radioId, String name, int parentId, String radioFm, String radioUrl,
                  String backUrl, int state, int sort, int level, int isExisUrl, String addTime,
                  long updateTime) {
        this.radioId = radioId;
        this.name = name;
        this.parentId = parentId;
        this.radioFm = radioFm;
        this.radioUrl = radioUrl;
        this.backUrl = backUrl;
        this.state = state;
        this.sort = sort;
        this.level = level;
        this.isExisUrl = isExisUrl;
        this.addTime = addTime;
        this.updateTime = updateTime;
    }



    @Generated(hash = 434680286)
    public FMBean() {
    }



    public int getRadioId() {
        return radioId;
    }

    public void setRadioId(int radioId) {
        this.radioId = radioId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRadioFm() {
        return radioFm;
    }

    public void setRadioFm(String radioFm) {
        this.radioFm = radioFm;
    }

    public String getRadioUrl() {
        return radioUrl;
    }

    public void setRadioUrl(String radioUrl) {
        this.radioUrl = radioUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsExisUrl() {
        return isExisUrl;
    }

    public void setIsExisUrl(int isExisUrl) {
        this.isExisUrl = isExisUrl;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
