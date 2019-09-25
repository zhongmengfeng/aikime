# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Samsung Android 4.2 bug workaround
# https://code.google.com/p/android/issues/detail?id=78377
#-keepattributes **
#-keep class !android.support.v7.view.menu.**,!android.support.design.internal.NavigationMenu,!android.support.design.internal.NavigationMenuPresenter,!android.support.design.internal.NavigationSubMenu,** {*;}
#-dontpreverify
#-dontoptimize
#-dontshrink
#-dontwarn **
#-dontnote **
#
#
#
#
###记录生成的日志数据,gradle build时在本项目根目录输出##
##apk 包内所有 class 的内部结构
#-dump proguard/class_files.txt
##未混淆的类和成员
#-printseeds proguard/seeds.txt
##列出从 apk 中删除的代码
#-printusage proguard/unused.txt
##混淆前后的映射
#-printmapping proguard/mapping.txt
#########记录生成的日志数据，gradle build时 在本项目根目录输出-end######
#
#
##-------------------------------------------定制化区域----------------------------------------------
##---------------------------------1.实体类---------------------------------
##-keep class com.ankichina.ankiapp.** { *; }
##-keep class com.ankichina.ankiapp.receiver.** { *; }
##-keep class com.ankireader.** { *;}
##-keep class com.ankireader.Adapter.** { *;}
##-keep class com.ankireader.model.** { *;}
##-keep class com.ankireader.util.** { *;}
#-keep class  com.ichi2yiji.anki.features.ready.bean.** {*;}
#-keep class  com.ichi2yiji.anki.bean.** {*;}
#
#-keep public class com.ichi2yiji.anki.**{
#    public void set*(***);
#    public *** get*();
#    public *** is*();
#}
#-keep public class com.ichi2yiji.libanki.**{
#    public void set*(***);
#    public *** get*();
#    public *** is*();
#}
#-keep public class com.ankireader.**{
#    public void set*(***);
#    public *** get*();
#    public *** is*();
#}
#
#-keep class com.ichi2yiji.anim.** { *; }
#-keep class com.ichi2yiji.async.** { *; }
#-keep class com.ichi2yiji.compat.** { *; }
#-keep class com.ichi2yiji.filters.** { *; }
#-keep class com.ichi2yiji.preferences.** { *; }
#-keep class com.ichi2yiji.themes.** { *; }
#-keep class com.ichi2yiji.ui.** { *; }
#-keep class com.ichi2yiji.upgrade.** { *; }
#-keep class com.ichi2yiji.utils.** { *; }
#-keep class com.ichi2yiji.widget.** { *; }
#
#-keep class com.ichi2yiji.libanki.** { *; }
#
##-keepclasseswithmembernames class com.ichi2yiji.anki.DictionaryPage{*;}#zzzzzzzzzzz????
##-keepclassmembers class com.ichi2yiji.anki.DictionaryPage{*;}
##-keep class com.ichi2yiji.anki.DictionaryPage{ *; }
##-keepnames class com.ichi2yiji.anki.DictionaryPage$* {
##     public <fields>;
##     public <methods>;
## }
#
#
#-keep class com.wildplot.android.** { *; }
#
#-keep class org.apache.commons.httpclient.contrib.ssl.** { *; }
#
#
#
##-------------------------------------------------------------------------
#
##---------------------------------2.第三方包-------------------------------
##如果引用了v4或者v7包
#-dontwarn android.support.**
#-dontwarn android.support.v4.**
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.support.v4.app.Fragment
#-dontwarn android.support.v7.**
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.app.** { *; }
#-keep public class * extends android.support.v7.**
#-keep public class * extends android.support.v7.app.Fragment
#
#-keep class com.google.gson.** { *; }
#-keep class com.google.gson.stream.** { *; }
#
#-dontwarn com.mikepenz.**
#
##艾卡记忆主工程
#-dontwarn com.afollestad.materialdialogs.**
#-keep class com.afollestad.materialdialogs.**{*;}
#
#-dontwarn com.getbase.**
#-keep class com.getbase.**{*;}
#
#-dontwarn org.acra.**
#-keep class org.acra.** {*;}
#
#-dontwarn timber.**
#-keep class timber.** {*;}
#
#-dontwarn android.support.multidex.**
#-keep class android.support.multidex.** {*;}
#
#-dontwarn cn.wanghaomiao.xpath.**
#-keep class cn.wanghaomiao.xpath.** {*;}
#
#-dontwarn com.bigkoo.svprogresshud.**
#-keep class com.bigkoo.svprogresshud.** {*;}
#
#-dontwarn info.hoang8f.android.segmented.**
#-keep class info.hoang8f.android.segmented.** {*;}
#
#
#
#-keep class org.apache.**{*;}                                               #过滤commons-httpclient-3.1.jar
#
##xUtils
#-dontwarn com.lidroid.xutils.**                                             #去掉警告
#-keep class com.lidroid.xutils.**{*;}                                       #过滤xUtils-2.6.14.jar
#-keep class * extends java.lang.annotation.Annotation{*;}                   #这是xUtils文档中提到的过滤掉注解
#-keep class com.lidroid.** { *; }
#
#
#-dontwarn org.apache.**
#-dontwarn net.sf.**
#
##阿里云
#-keep class com.alibaba.sdk.android.oss.** { *; }
#-dontwarn okio.**
#-dontwarn org.apache.commons.codec.binary.**
#
#
##个推
#-dontwarn com.igexin.**
#-keep class com.igexin.** { *; }
#-keep class org.json.** { *; }
#
##友盟
#-dontshrink
#-dontwarn com.google.android.maps.**
#-dontwarn android.webkit.WebView
#-dontwarn com.umeng.**
#-dontwarn com.tencent.weibo.sdk.**
#-dontwarn com.facebook.**
#-keep public class javax.**
#-keep public class android.webkit.**
#-dontwarn android.support.v4.**
#-keep enum com.facebook.**
#-keepattributes Exceptions,InnerClasses,Signature
#
#-keep public interface com.facebook.**
#-keep public interface com.tencent.**
#-keep public interface com.umeng.socialize.**
#-keep public interface com.umeng.socialize.sensor.**
#-keep public interface com.umeng.scrshot.**
#
#-keep public class com.umeng.socialize.* {*;}
#
#
#-keep class com.facebook.**
#-keep class com.facebook.** { *; }
#-keep class com.umeng.scrshot.**
#-keep public class com.tencent.** {*;}
#-keep class com.umeng.socialize.sensor.**
#-keep class com.umeng.socialize.handler.**
#-keep class com.umeng.socialize.handler.*
#-keep class com.umeng.weixin.handler.**
#-keep class com.umeng.weixin.handler.*
#-keep class com.umeng.qq.handler.**
#-keep class com.umeng.qq.handler.*
#-keep class UMMoreHandler{*;}
#-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
#-keep class com.tencent.mm.sdk.modelmsg.** implements   com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
#-keep class im.yixin.sdk.api.YXMessage {*;}
#-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
#-keep class com.tencent.mm.sdk.** { *;}
#-dontwarn twitter4j.**
#-keep class twitter4j.** { *; }
#
#-keep class com.tencent.** {*;}
#-dontwarn com.tencent.**
#-keep public class com.umeng.com.umeng.soexample.R$*{
#    public static final int *;
#}
#-keep public class com.linkedin.android.mobilesdk.R$*{
#    public static final int *;
#    }
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class com.tencent.open.TDialog$*
#-keep class com.tencent.open.TDialog$* {*;}
#-keep class com.tencent.open.PKDialog
#-keep class com.tencent.open.PKDialog {*;}
#-keep class com.tencent.open.PKDialog$*
#-keep class com.tencent.open.PKDialog$* {*;}
#
#-keep class com.sina.** {*;}
#-dontwarn com.sina.**
#-keep class  com.alipay.share.sdk.** {
#   *;
#}
#-keepnames class * implements android.os.Parcelable {
#    public static final ** CREATOR;
#}
#
#-keep class com.linkedin.** { *; }
#
#
#-keep class com.chaojiyiji.**{ *;}
#
#
##友盟统计
#-keepclassmembers class * {
#   public <init> (org.json.JSONObject);
#}
#-keep public class com.chaojiyiji.geometerplus.zlibrary.ui.android.R$*{
#    public static final int *;
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
##-------------------------------------------------------------------------
#
##---------------------------------3.与js互相调用的类------------------------
#-keepclassmembers class com.anlireader.MainActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckTestReal$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckTest$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownloadTests$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckReader$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownloadBooks$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckPicker$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownLoadDecks_webview$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownLoadDecksFromAnki$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.FilteredDeckActivity$MyObject{
#    <methods>;
#}
#
##登录注册个人中心
#-keepclassmembers class com.ichi2yiji.anki.LoginActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.RegisterActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.RetrievePasswordActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.PersonalCenterActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.StatisticsAika$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.AikaDistributeActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.AikaAttentClassActivity$MyObject{
#    <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DictionaryPage$MyObject{
#   <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.MainActivity2$MyObject{
#   <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.OnLineHelpActivity$MyObject{
#   <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.SettingActivity$MyObject{
#   <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckOptionsAika$MyObject{
#   <methods>;
#}
#-keepclassmembers class com.ichi2yiji.anki.CustomStudyActivity$MyObject{
#   <methods>;
#}
#
#
#
#
#
##可能有JS交互的类
#-keep class com.ichi2yiji.anki.AbstractFlashcardViewer{ *; }
#
#
#-keepclassmembers class com.ichi2yiji.anki.DeckTestReal$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckTest$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownloadTests$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckReader$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownloadBooks$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DeckPicker$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.DownLoadDecks_webview$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.FilteredDeckActivity$MyObject{
#    *;
#}
#-keepclassmembers class com.ichi2yiji.anki.SettingActivity$MyObject{
#    *;
#}
#
##-------------------------------------------------------------------------
#
##---------------------------------4.反射相关的类和方法-----------------------
#
#
#
##----------------------------------------------------------------------------
##---------------------------------------------------------------------------------------------------
#
##-------------------------------------------基本不用动区域--------------------------------------------
##---------------------------------基本指令区----------------------------------
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontskipnonpubliclibraryclassmembers
#-dontpreverify
#-verbose
#-printmapping proguardMapping.txt
#-optimizations !code/simplification/cast,!field/*,!class/merging/*
#-keepattributes *Annotation*,InnerClasses
#-keepattributes Signature
#-keepattributes SourceFile,LineNumberTable
##----------------------------------------------------------------------------
#
##---------------------------------默认保留区---------------------------------
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class * extends android.view.View
#-keep public class com.android.vending.licensing.ILicensingService
#-keep class android.support.** {*;}
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclassmembers class * extends android.app.Activity{
#    public void *(android.view.View);
#}
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keep public class * extends android.view.View{
#    *** get*();
#    void set*(***);
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#-keep class **.R$* {
# *;
#}
#-keepclassmembers class * {
#    void *(**On*Event);
#}
##----------------------------------------------------------------------------
#
##---------------------------------webview------------------------------------
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, java.lang.String);
#}
##----------------------------------------------------------------------------
##---------------------------------------------------------------------------------------------------