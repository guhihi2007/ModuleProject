package com.feisukj.base.util

import android.util.Log
import com.feisukj.base.BuildConfig


object LogUtils {
    /**
     * 日志输出时的TAG
     */
    private val mTag = "Module"
    /**
     * 日志输出级别NONE
     */
    val LEVEL_OFF = 0
    /**
     * 日志输出级别 ALL
     */
    val LEVEL_ALL = 7

    /**
     * 日志输出级别V
     */
    val LEVEL_VERBOSE = 1
    /**
     * 日志输出级别D
     */
    val LEVEL_DEBUG = 2
    /**
     * 日志输出级别I
     */
    val LEVEL_INFO = 3
    /**
     * 日志输出级别W
     */
    val LEVEL_WARN = 4
    /**
     * 日志输出级别E
     */
    val LEVEL_ERROR = 5
    /**
     * 日志输出级别S,自定义定义的一个级别
     */
    val LEVEL_SYSTEM = 6

    /**
     * 是否允许输出log
     */
    var mDebuggable = if (BuildConfig.DEBUG) LEVEL_ALL else LEVEL_OFF //线上环境改为LEVEL_OFF

    /**
     * 用于记时的变量
     */
    private var mTimestamp: Long = 0
    /**
     * 写文件的锁对象
     */
    private val mLogLock = Any()

    /**---------------日志输出,已固定TAG  begin--------------- */
    /**
     * 以级别为 d 的形式输出LOG
     */
    fun v(msg: String) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(mTag, msg)
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(msg: String) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(mTag, msg)
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(msg: String) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(mTag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(msg: String) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出Throwable
     */
    fun w(tr: Throwable) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag, "", tr)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG信息和Throwable
     */
    fun w(msg: String?, tr: Throwable) {
        if (mDebuggable >= LEVEL_WARN && null != msg) {
            Log.w(mTag, msg, tr)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, msg)
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println,稍微格式化了一下
     */
    fun sf(msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            println("----------$msg----------")
        }
    }

    /**
     * 以级别为 s 的形式输出LOG,主要是为了System.out.println
     */
    fun s(msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            println(msg)
        }
    }

    /**
     * 以级别为 e 的形式输出Throwable
     */
    fun e(tr: Throwable) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag, "", tr)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG信息和Throwable
     */
    fun e(msg: String?, tr: Throwable) {
        if (mDebuggable >= LEVEL_ERROR && null != msg) {
            Log.e(mTag, msg, tr)
        }
    }

    /**---------------日志输出,已固定TAG  end--------------- */

    /**---------------日志输出,未固定TAG  begin--------------- */
    /**
     * 以级别为 d 的形式输出LOG
     */
    fun v(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_VERBOSE) {
            Log.v(mTag + tag, msg)
        }
    }

    /**
     * 以级别为 d 的形式输出LOG
     */
    fun d(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_DEBUG) {
            Log.d(mTag + tag, msg)
        }
    }

    /**
     * 以级别为 i 的形式输出LOG
     */
    fun i(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_INFO) {
            Log.i(mTag + tag, msg)
        }
    }

    /**
     * 以级别为 w 的形式输出LOG
     */
    fun w(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_WARN) {
            Log.w(mTag + tag, msg)
        }
    }

    /**
     * 以级别为 e 的形式输出LOG
     */
    fun e(tag: String, msg: String) {
        if (mDebuggable >= LEVEL_ERROR) {
            Log.e(mTag + tag, msg)
        }
    }

    fun <T> printList(list: List<T>?) {
        if (list == null || list.size < 1) {
            return
        }
        val size = list.size
        i("---begin---")
        for (i in 0 until size) {
            i(i.toString() + ":" + list[i].toString())
        }
        i("---end---")
    }

    fun <T> printArray(array: Array<T>?) {
        if (array == null || array.size < 1) {
            return
        }
        val length = array.size
        i("---begin---")
        for (i in 0 until length) {
            i(i.toString() + ":" + array[i].toString())
        }
        i("---end---")
    }
}
/**---------------日志输出,未固定TAG  end--------------- */
/**
 * 把Log存储到文件中
 *
 * @param log  需要存储的日志
 * @param path 存储路径
 */
