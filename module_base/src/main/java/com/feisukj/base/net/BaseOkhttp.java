package com.feisukj.base.net;

import com.feisukj.base.util.LogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/30 0030.
 * okhttp 请求
 */

public class BaseOkhttp {

    private static final String baseUrlHead = "";

    private static BaseOkhttp baseOkhttp;
    /**
     * Handler
     * okHttp post请求
     * handler 状态码 1失败0成功
     */
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();


    private BaseOkhttp() {
    }

    public static BaseOkhttp getInstance() {
        if (baseOkhttp == null) {
            baseOkhttp = new BaseOkhttp();
        }
        return baseOkhttp;
    }

    /**
     * get请求 json
     * @param urlPart
     * @param json
     * @param callback
     */
    public void getJson(String urlPart, String json, final RequestCallback callback){

        String requestUrl = String.format("%s%s?%s", baseUrlHead, urlPart, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .url(requestUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure("Not Found", null);
                }
            }
        });
    }

    /**
     * get请求 map
     *
     * @param urlPart
     * @param paramsMap
     * @param callback
     */
    public void getMap(String urlPart, Map<String, String> paramsMap, final RequestCallback callback){
        StringBuilder tempParams = new StringBuilder();
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                tempParams.append("&");
            }

            try {
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                LogUtils.INSTANCE.e("get请求参数拼接错误："+e.toString());
                return;
            }

            pos++;
        }
        String json = tempParams.toString();
        getJson(urlPart,json,callback);
    }

    /**
     * post请求 map为body
     *
     * @param url
     * @param map
     * @param
     */
    public void postMap(String url, Map<String, Object> map, final RequestCallback callback) {
        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }

        RequestBody body = builder.build();

        final Request request = new Request.Builder()
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure("Not Found", null);
                }
            }
        });

    }

    /**
     * post请求，json数据为body
     *
     * @param url
     * @param json
     * @param callback
     */
    public void postJson(String url, String json, final RequestCallback callback) {

        RequestBody body = RequestBody.create(JSON, json);
        final Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure("Not Found", null);
                }
            }
        });
    }

    /**
     * 获取广告配置  示例
     * */
    public void getAdConfig(Map<String,String> prams, RequestCallback callback){
        getMap(Api.YTK_ADCONFIG,prams,callback);
    }

    public interface RequestCallback {
        void onSuccess(String response);

        void onFailure(String msg, Exception e);
    }

}
