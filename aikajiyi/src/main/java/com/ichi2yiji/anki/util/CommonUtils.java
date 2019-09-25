package com.ichi2yiji.anki.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ekar01 on 2017/6/29.
 */

public class CommonUtils {
    /**
     * 获取本机ip地址
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    //android.util.TypedValue
    /**  complex unit: Value is raw pixels. */
    public static final int COMPLEX_UNIT_PX = 0;
    /**  complex unit: Value is Device Independent Pixels. */
    public static final int COMPLEX_UNIT_DIP = 1;
    /**  complex unit: Value is a scaled pixel. */
    public static final int COMPLEX_UNIT_SP = 2;
    /**  complex unit: Value is in points. */
    public static final int COMPLEX_UNIT_PT = 3;
    /**  complex unit: Value is in inches. */
    public static final int COMPLEX_UNIT_IN = 4;
    /**  complex unit: Value is in millimeters. */
    public static final int COMPLEX_UNIT_MM = 5;

    /**
     * Converts an unpacked complex data value holding a dimension to its final
     * floating point value. The two parameters unit and value are as in dimension.
     *
     * @param unit The unit to convert from.
     * @param value The value to apply the unit to.
     * @param metrics Current display metrics to use in the conversion --
     *                supplies display density and scaling information.
     *
     * @return The complex floating point value multiplied by the appropriate
     * metrics depending on its unit.
     */
    public static float applyDimension(int unit, float value,
                                       DisplayMetrics metrics)
    {
        switch (unit) {
            case COMPLEX_UNIT_PX: //原始像素点
                return value;
            case COMPLEX_UNIT_DIP: //设备独立点
                return value * metrics.density;
            case COMPLEX_UNIT_SP: //缩放像素点
                return value * metrics.scaledDensity;
            case COMPLEX_UNIT_PT: //英磅
                return value * metrics.xdpi * (1.0f/72);
            case COMPLEX_UNIT_IN: //英寸
                return value * metrics.xdpi;
            case COMPLEX_UNIT_MM: //毫米
                return value * metrics.xdpi * (1.0f/25.4f);
        }
        return 0;
    }


    // 验证是否为昵称
    public static boolean isHoneyname(String honeyname) {
        if (honeyname == null || honeyname.length() == 0) {
            return false;
        }
        int len = 0;
        char[] honeychar = honeyname.toCharArray();
        for (int i = 0; i < honeychar.length; i++) {
            if (isChinese(honeychar[i])) {
                len += 2;
            } else {
                len += 1;
            }
        }
        if (len < 4 || len > 18) {
            //"正确的昵称应该为\n1、4-18个字符\n2、2-6个汉字\n3、不能是邮箱和手机号"
            return false;
        }
        return true;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    // 判断是否为手机号
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");//需新增号段181等
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    // 判断格式是否为email
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
