package com.ankireader.util;


import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.Map;


public class ZXUtils {

    /**
     * 发送get请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Get(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 发送post请求
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, Map<String, String> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }


    /**
     * 上传文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件(zc自定义)
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile2(String url, Map<String, String> map, String filepath, Callback.CommonCallback<T> callback) {
        RequestParams params = new RequestParams(url);
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件(zc自定义)
     *
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFileWithProgress(String url, String filepath, Callback.ProgressCallback<T> callback) {
        RequestParams params = new RequestParams(url);

        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }


    public class JsonResponseParser implements ResponseParser {
        //检查服务器返回的响应头信息
        @Override
        public void checkResponse(UriRequest request) throws Throwable {

        }
        /**
         * 转换result为resultType类型的对象
         *
         * @param resultType  返回值类型(可能带有泛型信息)
         * @param resultClass 返回值类型
         * @param result      字符串数据
         * @return
         * @throws Throwable
         */
        @Override
        public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
            return new Gson().fromJson(result, resultClass);
        }
    }


    public class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType> {

        @Override
        public void onSuccess(ResultType result) {
            //可以根据公司的需求进行统一的请求成功的逻辑处理
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            //可以根据公司的需求进行统一的请求网络失败的逻辑处理
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {

        }


    }
}
