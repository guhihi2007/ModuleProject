package com.feisu.greendao.radio;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author : Gupingping
 * Date : 2019/3/27
 * QQ : 464955343
 */
@Entity
public class UserTokenEntity {

    private String mAccessToken;
    private Integer mExpiresIn;
    private String mRefreshToken;
    private String mTokenType;
    @Id
    private String mUserId;
    private Long mExpiresTime;

    @Generated(hash = 1403829435)
    public UserTokenEntity(String mAccessToken, Integer mExpiresIn,
                           String mRefreshToken, String mTokenType, String mUserId,
                           Long mExpiresTime) {
        this.mAccessToken = mAccessToken;
        this.mExpiresIn = mExpiresIn;
        this.mRefreshToken = mRefreshToken;
        this.mTokenType = mTokenType;
        this.mUserId = mUserId;
        this.mExpiresTime = mExpiresTime;
    }

    @Generated(hash = 1018679992)
    public UserTokenEntity() {
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public Integer getmExpiresIn() {
        return mExpiresIn;
    }

    public void setmExpiresIn(Integer mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }

    public String getmRefreshToken() {
        return mRefreshToken;
    }

    public void setmRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public void setmTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public Long getmExpiresTime() {
        return mExpiresTime;
    }

    public void setmExpiresTime(Long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    public String getMAccessToken() {
        return this.mAccessToken;
    }

    public void setMAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public Integer getMExpiresIn() {
        return this.mExpiresIn;
    }

    public void setMExpiresIn(Integer mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }

    public String getMRefreshToken() {
        return this.mRefreshToken;
    }

    public void setMRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public String getMTokenType() {
        return this.mTokenType;
    }

    public void setMTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public String getMUserId() {
        return this.mUserId;
    }

    public void setMUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public Long getMExpiresTime() {
        return this.mExpiresTime;
    }

    public void setMExpiresTime(Long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    @Override
    public String toString() {
        return "UserTokenEntity{" +
                " mUserId='" + mUserId + '\'' +
                ", mAccessToken='" + mAccessToken + '\'' +
                ", mRefreshToken='" + mRefreshToken + '\'' +
                ", mExpiresIn=" + mExpiresIn +
                ", mTokenType='" + mTokenType + '\'' +
                ", mExpiresTime=" + mExpiresTime +
                '}';
    }
}
