package com.feisukj.base.retrofitnet

import com.feisukj.base.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
class HttpUtils private constructor() {
    private val retrofitForToken: Retrofit
    private val retrofitForAD: Retrofit

    init {
        val logging = LoggingInterceptor(Logger())
        logging.level = LoggingInterceptor.Level.BODY
//        val interceptor = Interceptor { chain ->
//            val request = chain.request().newBuilder().addHeader("QT-Access-Token", token).build()
//            chain.proceed(request)
//        }
        val okHttpClientForToken = OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .connectTimeout(9, TimeUnit.SECONDS)
                .build()

        val okHttpClientForAD = OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .connectTimeout(9, TimeUnit.SECONDS)
                .build()
        retrofitForToken = Retrofit.Builder()
                .client(okHttpClientForToken)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.CONTENT_HOST)
                .build()
        retrofitForAD = Retrofit.Builder()
                .client(okHttpClientForAD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.AD_HOST)
                .build()

    }

    private object SingletonHolder {
        internal var INSTANCE = HttpUtils()
    }

    companion object {

        private val instance: HttpUtils
            get() = SingletonHolder.INSTANCE

        fun <T> setService(clazz: Class<T>): T {
            return HttpUtils.instance.retrofitForToken.create(clazz)
        }

        fun <T> setServiceWithToken(mToken: String, clazz: Class<T>): T {
            val logging = LoggingInterceptor(Logger())
            logging.level = LoggingInterceptor.Level.BODY

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder().addHeader("QT-Access-Token", mToken).build()
                chain.proceed(request)
            }

            val okHttpClientForData = OkHttpClient().newBuilder()
                    .addInterceptor(logging)
                    .addInterceptor(interceptor)
                    .connectTimeout(9, TimeUnit.SECONDS)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClientForData)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.CONTENT_HOST)
                    .build().create(clazz)
        }

        fun <T> setServiceForADConfig(clazz: Class<T>): T {
            return HttpUtils.instance.retrofitForAD.create(clazz)
        }
    }

}
