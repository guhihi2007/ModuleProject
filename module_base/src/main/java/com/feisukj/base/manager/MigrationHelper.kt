package com.feisukj.base.manager

import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.feisukj.base.util.LogUtils
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.database.StandardDatabase
import org.greenrobot.greendao.internal.DaoConfig
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * 数据库升级帮助类，升级调用 {@link #migrate(SQLiteDatabase, Class[])} or {@link #migrate(Database, Class[])}
 * 如果在旧表新增字段是int long 类型，会升级失败,没有修复这个问题，网上有解决办法
 */
object MigrationHelper {

    private val SQLITE_MASTER = "sqlite_master"
    private val SQLITE_TEMP_MASTER = "sqlite_temp_master"

    fun migrate(db: SQLiteDatabase, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        LogUtils.e("*** The Old Database Version: ${db.version}")
        val database = StandardDatabase(db)
        migrate(database, *daoClasses)
    }

    fun migrate(database: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        LogUtils.e("*** Generate temp table start...")
        generateTempTables(database, *daoClasses)
        LogUtils.e("*** Generate temp table complete...")

        dropAllTables(database, true, *daoClasses)
        createAllTables(database, false, *daoClasses)

        LogUtils.e("*** Restore data start...")
        restoreData(database, *daoClasses)
        LogUtils.e("*** Restore data complete ...")
    }

    private fun generateTempTables(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            var tempTableName: String? = null

            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            if (!isTableExists(db, false, tableName)) {
                LogUtils.e("*** New Table $tableName")
                continue
            }
            try {
                tempTableName = "${daoConfig.tablename}_TEMP"
                val dropTableStringBuilder = StringBuilder()
                dropTableStringBuilder.append("DROP TABLE IF EXISTS ").append(tempTableName).append(";")
                db.execSQL(dropTableStringBuilder.toString())

                val insertTableStringBuilder = StringBuilder()
                insertTableStringBuilder.append("CREATE TEMPORARY TABLE ").append(tempTableName)
                insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";")
                db.execSQL(insertTableStringBuilder.toString())
                LogUtils.e( "*** Table $tableName \n ---Columns--> ${getColumnsStr(daoConfig)}")
                LogUtils.e(  "*** Generate temp table $tempTableName")
            } catch (e: SQLException) {
                LogUtils.e("*** Failed to generate temp table $tempTableName")
            }

        }
    }

    private fun isTableExists(db: Database?, isTemp: Boolean, tableName: String?): Boolean {
        if (db == null || tableName.isNullOrEmpty()) {
            return false
        }
        val dbName = if (isTemp) SQLITE_TEMP_MASTER else SQLITE_MASTER
        val sql = "SELECT COUNT(*) FROM $dbName WHERE type = ? AND name = ?"
        var cursor: Cursor? = null
        var count = 0
        try {
            cursor = db.rawQuery(sql, arrayOf("table", tableName))
            if (cursor?.moveToFirst() == false) {
                return false
            }
            count = cursor?.getInt(0) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return count > 0
    }


    private fun getColumnsStr(daoConfig: DaoConfig?): String {
        if (daoConfig == null) {
            return "no columns"
        }
        val builder = StringBuilder()
        for (i in daoConfig.allColumns.indices) {
            builder.append(daoConfig.allColumns[i])
            builder.append(",")
        }
        if (builder.length > 0) {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }


    private fun dropAllTables(db: Database, ifExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        reflectMethod(db, "dropTable", ifExists, *daoClasses)
        LogUtils.e("*** Drop all table...")
    }

    private fun createAllTables(db: Database, ifNotExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        reflectMethod(db, "createTable", ifNotExists, *daoClasses)
        LogUtils.e("*** Create all table...")
    }

    /**
     * dao class already define the sql exec method, so just invoke it
     */
    private fun reflectMethod(db: Database, methodName: String, isExists: Boolean, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        if (daoClasses.isEmpty()) {
            return
        }
        try {
            for (cls in daoClasses) {
                val method = cls.getDeclaredMethod(methodName, Database::class.java, Boolean::class.javaPrimitiveType)
                method.invoke(null, db, isExists)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    private fun restoreData(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            val tempTableName = "${daoConfig.tablename}_TEMP"

            if (!isTableExists(db, true, tempTableName)) {
                continue
            }

            try {
                // get all columns from tempTable, take careful to use the columns list
                val columns = getColumns(db, tempTableName)
                val properties = ArrayList<String>(columns.size)
                for (j in daoConfig.properties.indices) {
                    val columnName = daoConfig.properties[j].columnName
                    if (columns.contains(columnName)) {
                        properties.add(columnName)
                    }
                }
                if (properties.size > 0) {
                    val columnSQL = TextUtils.join(",", properties)

                    val insertTableStringBuilder = StringBuilder()
                    insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (")
                    insertTableStringBuilder.append(columnSQL)
                    insertTableStringBuilder.append(") SELECT ")
                    insertTableStringBuilder.append(columnSQL)
                    insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";")
                    db.execSQL(insertTableStringBuilder.toString())
                    LogUtils.e("*** Restore data to $tableName")
                }
                val dropTableStringBuilder = StringBuilder()
                dropTableStringBuilder.append("DROP TABLE ").append(tempTableName)
                db.execSQL(dropTableStringBuilder.toString())
                LogUtils.e("*** Drop temp table $tempTableName")
            } catch (e: SQLException) {
                LogUtils.e( "*** Failed to restore data from temp table $tempTableName ,e==${e.message}")
            }

        }
    }

    private fun getColumns(db: Database, tableName: String): MutableList<String> {
        var columns: MutableList<String>? = null
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName limit 0", null)
            if (null != cursor && cursor.columnCount > 0) {
                columns = Arrays.asList(*cursor.columnNames)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
                cursor?.close()
            if (null == columns)
                columns = ArrayList<String>()
        }
        return columns
    }
}