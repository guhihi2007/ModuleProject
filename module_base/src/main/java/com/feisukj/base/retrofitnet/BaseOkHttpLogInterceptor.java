package com.feisukj.base.retrofitnet;

import com.feisukj.base.util.LogUtils;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/** okhttp日志拦截器 */
public class BaseOkHttpLogInterceptor implements Interceptor {

    String TAG = BaseOkHttpLogInterceptor.class.getSimpleName()+":   ";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();//未操作前的请求
        long startTime = System.currentTimeMillis();
        //请求前添加头部
        Request request = original.newBuilder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "package_name") // 不能转UTF-8
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Connection","keep-alive")
                .addHeader("Accept","*/*")
                .addHeader("Accept-Language","zh-Hans-CN;q=1")
                .build();
        okhttp3.Response response = chain.proceed(request);//发起请求
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();//打印之后流关闭了
        LogUtils.INSTANCE.e(TAG + "\n");
        LogUtils.INSTANCE.e(TAG+"----------Start----------------");
        LogUtils.INSTANCE.e(TAG+"request:"+request.toString());
        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            LogUtils.INSTANCE.e(TAG+headers.name(i) + ": " + headers.value(i));
        }

        if (request.method().equals("POST")){
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                LogUtils.INSTANCE.e(TAG+"| RequestParams:{"+sb.toString()+"}");
            }
        }

        LogUtils.INSTANCE.e(TAG+"response:"+content);
        LogUtils.INSTANCE.e(TAG + "\n");
        LogUtils.INSTANCE.e(TAG+"----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
