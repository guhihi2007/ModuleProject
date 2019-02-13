package com.feisukj.ad.bean

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
open class BaseNetBean {
    var code: Int = 0
    var message: String? = null
    override fun toString(): String {
        return "BaseNetBean(code=$code, message=$message)"
    }

}
