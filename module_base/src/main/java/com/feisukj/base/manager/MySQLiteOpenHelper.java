//package com.feisukj.base.manager;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//
//import org.greenrobot.greendao.database.Database;
//
//
///**
// * Created by Administrator on 2017/9/13.
// *
// * @des 数据库升级
// */
//public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
//    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
//        super(context, name, factory);
//    }
//
//    @Override
//    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        MigrationHelper.INSTANCE.migrate(db, CityDao.class,
//                CategoryBeanDao.class,
//                FMBeanDao.class,
//                RadioEntityDao.class,
//                SearchHistoryEntityDao.class);
//
//    }
//}
