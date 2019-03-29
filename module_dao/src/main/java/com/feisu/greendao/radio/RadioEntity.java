package com.feisu.greendao.radio;


import com.feisu.greendao.db.gen.CategoryBeanDao;
import com.feisu.greendao.db.gen.DaoSession;
import com.feisu.greendao.db.gen.RadioEntityDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Author : Gupingping
 * Date : 2019/1/18
 * QQ : 464955343
 */
@Entity
public class RadioEntity implements Serializable {

    public static final long serialVersionUID = 8792734297834L;
    /**
     * description : "有一种快乐，在空中听见，Music Radio旋律在蔓延......感动让你听见——我要我的音乐"，音乐之声是全华语市场最专业流行音乐频率，中国知名类型化流行音乐频率，覆盖全国，打破以往音乐广播板块型播出形态.
     * id : 388
     * popularity : 1200
     * thumbs : {"large_thumb":"http://pic.qingting.fm/2015/0810/20150810233411680.jpg!800","medium_thumb":"http://pic.qingting.fm/2015/0810/20150810233411680.jpg!400","small_thumb":"http://pic.qingting.fm/2015/0810/20150810233411680.jpg!200"}
     * title : CNR音乐之声
     * update_time : 2016-03-25 11:46:39
     */
    @Id
    private long id;
    private String title;
    private String description;
    private String small_thumb;
    private long popularity;
    private String update_time;
    private String large_thumb;
    private String medium_thumb;
    private long parentId;//所属分类di
    private boolean collected = false;
    private boolean heard = false;
    private long heardTime = System.currentTimeMillis();
    @ToOne(joinProperty = "parentId")// parentId作为外键与CategoryBean中的主键（也就是id）相连
    private CategoryBean categoryBean;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1322406364)
    private transient RadioEntityDao myDao;
    @Generated(hash = 1148756535)
    private transient Long categoryBean__resolvedKey;

    @Generated(hash = 1550893979)
    public RadioEntity(long id, String title, String description, String small_thumb, long popularity, String update_time, String large_thumb, String medium_thumb, long parentId, boolean collected, boolean heard, long heardTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.small_thumb = small_thumb;
        this.popularity = popularity;
        this.update_time = update_time;
        this.large_thumb = large_thumb;
        this.medium_thumb = medium_thumb;
        this.parentId = parentId;
        this.collected = collected;
        this.heard = heard;
        this.heardTime = heardTime;
    }

    @Generated(hash = 1376256628)
    public RadioEntity() {
    }

    public RadioEntity(long id, String title, String description, String small_thumb) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.small_thumb = small_thumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public long getParentId() {
        return this.parentId;
    }


    public void setParentId(long parentId) {
        this.parentId = parentId;
    }


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 259454644)
    public CategoryBean getCategoryBean() {
        long __key = this.parentId;
        if (categoryBean__resolvedKey == null || !categoryBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryBeanDao targetDao = daoSession.getCategoryBeanDao();
            CategoryBean categoryBeanNew = targetDao.load(__key);
            synchronized (this) {
                categoryBean = categoryBeanNew;
                categoryBean__resolvedKey = __key;
            }
        }
        return categoryBean;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 102146798)
    public void setCategoryBean(@NotNull CategoryBean categoryBean) {
        if (categoryBean == null) {
            throw new DaoException("To-one property 'parentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.categoryBean = categoryBean;
            parentId = categoryBean.getChannelId();
            categoryBean__resolvedKey = parentId;
        }
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }


    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }


    public String getLarge_thumb() {
        return this.large_thumb;
    }


    public void setLarge_thumb(String large_thumb) {
        this.large_thumb = large_thumb;
    }


    public String getMedium_thumb() {
        return this.medium_thumb;
    }


    public void setMedium_thumb(String medium_thumb) {
        this.medium_thumb = medium_thumb;
    }


    public String getSmall_thumb() {
        return this.small_thumb;
    }


    public void setSmall_thumb(String small_thumb) {
        this.small_thumb = small_thumb;
    }

    public boolean getCollected() {
        return this.collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean getHeard() {
        return this.heard;
    }

    public void setHeard(boolean heard) {
        this.heard = heard;
    }

    public long getHeardTime() {
        return this.heardTime;
    }

    public void setHeardTime(long heardTime) {
        this.heardTime = heardTime;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1976939713)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRadioEntityDao() : null;
    }
}
