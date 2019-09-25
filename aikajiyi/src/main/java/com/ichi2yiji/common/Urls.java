package com.ichi2yiji.common;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Urls {

    /**
     * true 测试服务    false 正式服务
     */
    private static boolean isOfficeNet = false;
    // 基本域名
    public static  String BASE_DOMAIN ;

    static{
        //修改基本域名
        if(isOfficeNet){
            //http://www.ankichina.net/Home/Application/
            BASE_DOMAIN = "http://192.168.1.13/ankioss/index.php/";//内网
//            BASE_DOMAIN = "http://192.168.1.13/ankioss/index.php/";//内网
        }else {
            BASE_DOMAIN = "http://www.ankichina.net/";//外网
        }
    }

    /** 注册 - 获取短信验证码 **/
    public static String URL_APP_GET_CODE = BASE_DOMAIN + "Home/Application/appGetRegistCode/";

    /** 注册 **/
    public static String URL_APP_REGIST = BASE_DOMAIN + "Home/Application/appRegist/";

    /** 注册 - 激活账号 **/
    public static String URL_APP_GET_CLIENT_ID = BASE_DOMAIN + "Home/Application/getClient_id/";

    /** 登录 **/
    public static String URL_APP_LOGIN = BASE_DOMAIN + "Home/Application/appLogin/";

    /**  第三方用户登录调用后台接口   如果code 值为 2000，则为注册的新用户**/
    public static String URL_APP_THIDLOGO = BASE_DOMAIN + "Home/Application/appThirdRegist/";

    /**
     * 6.6 新添加接口
     */
    public static String URL_APP_THIRDLOGIN_REGISTERINFO = BASE_DOMAIN + "Home/Application/thirdlogin_registerinfo/";

    /** 更新用户信息接口,只更新学历和邀请码--修改6.8**/
    public static String URL_APP_UPDATE_MSG = BASE_DOMAIN + "Home/Application/appUpdateUserInfo/";

    public static String URL_APP_LOGO3 = BASE_DOMAIN + "Home/Application/logo3/";
    /**
     * 6.6 新添加接口
     */
    public static String URL_APP_THIRDLOGIN_LOGIN = BASE_DOMAIN + "Home/Application/thirdlogin_login/";



    /** 获取用户信息 **/
    public static String URL_APP_MEMBER_MSG = BASE_DOMAIN + "Home/Application/getUserInfo/";


    /** 共享模考初始化 **/
    public static String URL_APP_TESTS = BASE_DOMAIN + "Home/Application/appTests/";
    /** 共享牌组初始化 **/
    public static String URL_APP_PICKER = BASE_DOMAIN + "Home/Application/appDecks/";

    /** 下载统计 - 牌组 **/
    public static String URL_DOWN_PICKER = BASE_DOMAIN + "Home/Application/downDecks/";
    /** 下载统计 - 阅读 **/
    public static String URL_DOWN_READER = BASE_DOMAIN + "Home/Application/downTexts/";
    /** 下载统计 - 模考 **/
    public static String URL_DOWN_TESTS = BASE_DOMAIN + "Home/Application/downTests/";


    /** 牌组搜索 **/
    public static String URL_SEARCH_PICKER = BASE_DOMAIN + "Home/Application/searchDecks/";
    /** 点击牌组关键词搜索 **/
    public static String URL_KEYWORD_SEARCH_PICKER = BASE_DOMAIN + "Home/Application/dianDecks/";

    /** 共享文章数据 **/
    public static String URL_APP_TEXTS = BASE_DOMAIN + "/Home/Application/appTexts";


    /**
     * 百度翻译
     **/
    public static String URL_TRANSLATE = BASE_DOMAIN + "/Home/Application/translate/";

    public static String URL_BAIDU_DIACTIONARY = "http://dict.baidu.com/s?wd=%s";


    /** 邀请码关注班级 **/
    public static final String URL_APP_YAO_GUAN_ZHU = BASE_DOMAIN + "/Home/Application/yaoGuanzhu/";



    /////6.8接口更新
    /**
     * 重设密码
     */
    public static final String APP_RESET_PASSWD = BASE_DOMAIN + "/Home/Application/appResetPasswd/";

    /**
     * 重设密码--验证码
     */
    public static final String APP_GET_RESET_PWD_CODE = BASE_DOMAIN + "/Home/Application/appGetResetPwdCode/";

    //aikaactivity
    /**
     * 从后台请求关注班级页面的数据
     */
    public static final String CLASS_LIST = BASE_DOMAIN + "classList/";
    /**
     * 从后台请求分发作业页面的数据
     */
    public static final String MESSAGE_LIST = BASE_DOMAIN + "messageList/";

    /**
     * 从后台获取订单信息接口
     */
    public static final String PAY_FOR_CLASS = BASE_DOMAIN + "/Home/Application/payForClass/";


    public static final String UPDATE_ORDERMSG = BASE_DOMAIN + "/Home/Application/updateOrderMsg/";

}
