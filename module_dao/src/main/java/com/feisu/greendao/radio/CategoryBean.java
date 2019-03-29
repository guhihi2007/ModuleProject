package com.feisu.greendao.radio;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import com.feisu.greendao.db.gen.DaoSession;
import com.feisu.greendao.db.gen.RadioEntityDao;
import com.feisu.greendao.db.gen.CategoryBeanDao;

/**
 * Author : Gupingping
 * Date : 2019/1/18
 * QQ : 464955343
 */
@Entity
public class CategoryBean implements Parcelable{

    /**
     * id : 409
     * title : 国家台
     */

    @Id(autoincrement = false)
    private long channelId;
    private String title;
    @ToMany(joinProperties = {@JoinProperty(name = "channelId", referencedName = "parentId")})
    private List<RadioEntity> radioList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1346806554)
    private transient CategoryBeanDao myDao;
    private int resourceId;
    @Generated(hash = 1870435730)
    public CategoryBean() {
    }

    protected CategoryBean(Parcel in) {
        channelId = in.readLong();
        title = in.readString();
    }

    @Generated(hash = 1628716405)
    public CategoryBean(long channelId, String title, int resourceId) {
        this.channelId = channelId;
        this.title = title;
        this.resourceId = resourceId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(channelId);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
        @Override
        public CategoryBean createFromParcel(Parcel in) {
            return new CategoryBean(in);
        }

        @Override
        public CategoryBean[] newArray(int size) {
            return new CategoryBean[size];
        }
    };

    public long getChannelId() {
        return this.channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2075400988)
    public List<RadioEntity> getRadioList() {
        if (radioList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RadioEntityDao targetDao = daoSession.getRadioEntityDao();
            List<RadioEntity> radioListNew = targetDao
                    ._queryCategoryBean_RadioList(channelId);
            synchronized (this) {
                if (radioList == null) {
                    radioList = radioListNew;
                }
            }
        }
        return radioList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1714571974)
    public synchronized void resetRadioList() {
        radioList = null;
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

    public int getResourceId() {
        return this.resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 850963201)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryBeanDao() : null;
    }






}
