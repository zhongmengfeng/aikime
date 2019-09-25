package com.ichi2yiji.anki.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;

import org.json.JSONArray;

/**
 * Created by Administrator on 2016/12/28.
 */

public class OSSUtil {

    // 运行前需要配置以下字段为有效的值
    private static final String endpoint = "http://oss-cn-qingdao.aliyuncs.com";//获取牌组信息的节点
    private static final String accessKeyId = "8zpEuzFFgLw2ao9I";
    private static final String accessKeySecret = "3O6AgBvfyaS6Zlc1BpPY5nWs4cdcfH";

    private static final String testBucket = "deckstest";//云服务上的Bucket名


    //获取OSS服务器的牌组信息，用于艾卡分享的数据预加载
    public static void getDeckInfo(Context context){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        final OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        AyncListObjects(context, oss);
    }

    // 异步罗列Bucket中文件
    public static void AyncListObjects(final Context context, OSS oss) {
        // 创建罗列请求
        ListObjectsRequest listObjects = new ListObjectsRequest(testBucket);
        // 设置成功、失败回调，发送异步罗列请求
        OSSAsyncTask task = oss.asyncListObjects(listObjects, new OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>() {
            @Override
            public void onSuccess(ListObjectsRequest request, ListObjectsResult result) {
                Log.e("AyncListObjects", "Success!");
                JSONArray jaOutside = new JSONArray();
                for (int i = 0; i < result.getObjectSummaries().size(); i++) {
                    Log.e("AyncListObjects", "object: " + result.getObjectSummaries().get(i).getKey() + " "
                            + result.getObjectSummaries().get(i).getETag() + " "
                            + result.getObjectSummaries().get(i).getLastModified());
//                    Log.e("AyncListObjects", result.getPrefix());

                    //去掉第一个元素 EnglishWord/ "D41D8CD98F00B204E9800998ECF8427E" Wed Aug 03 12:46:00 GMT+08:00 2016
                    if(i != 0){
                        String filename = result.getObjectSummaries().get(i).getKey().replace("EnglishWord/","");
//                        String filepath = endpoint + "/" +testBucket + "/" + result.getObjectSummaries().get(i).getKey();
                        String filepath ="http://deckstest.oss-cn-qingdao.aliyuncs.com" + "/" + result.getObjectSummaries().get(i).getKey();
                        JSONArray jaInside = new JSONArray();
                        jaInside.put(filename);
                        jaInside.put(filepath);
                        jaOutside.put(jaInside);//二维JSON数组
                    }

                }
                String json = jaOutside.toString().replace("\\","");
                Log.e("jaOutside",">>>>>>>>" + jaOutside.toString());
                Log.e("json from oss",">>>>>>>>" + json);
                //将JSON数组保存到SharedPreferences
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("DECKINFOFROMOSS",json);
                editor.commit();
            }

            @Override
            public void onFailure(ListObjectsRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        task.waitUntilFinished();
    }

}
