package com.ichi2yiji.anki.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/25.
 */

public class SettingsBean implements Serializable {


    /**
     * AboutAika : 艾卡记忆是艾卡(北京)科技有限公司开发的一款高效辅助记忆软件.
     * SystemVoice : false
     * answerButtonSize : 100
     * automaticSyncMode : false
     * backupMax : 8
     * browserEditorFont : 0
     * cardZoom : 100
     * centerVertically : false
     * convertFenText : false
     * dayOffset : 20
     * deckreader : true
     * decktests : true
     * defaultFont : 0
     * fullscreenMode : 0
     * homePageStyle : 0
     * imageZoom : 100
     * keepScreenOn : false
     * language : 0
     * learnCutoff : 20
     * minimumCardsDueForNotification : 0
     * newSpread : 0
     * overrideFontBehavior : 0
     * relativeCardBrowserFontSize : 100
     * reportErrorMode : 2
     * showEstimates : false
     * syncFetchesMedia : true
     * themeIndex : 1
     * timeLimit : 0
     * tts : false
     * useCurrent : 0
     * widgetBlink : false
     * widgetVibrate : false
     */

    /** 关于 **/
    private String AboutAika = "艾卡记忆是艾卡(北京)科技有限公司开发的一款高效辅助记忆软件.";
    /** 声音 **/
    private boolean SystemVoice = false;
    /** 显示答案按钮大小 **/
    private int answerButtonSize = 100;
    /** 自动同步 **/
    private boolean automaticSyncMode = false;
    /** 最大备份数量 **/
    private int backupMax = 8;
    /** 浏览器和编辑器字体 **/
    private int browserEditorFont = 0;
    /** 卡片缩放 **/
    private int cardZoom = 100;
    /** 居中对齐 **/
    private boolean centerVertically = false;
    /** 启用象棋国际插件 **/
    private boolean convertFenText = false;
    /** 下一天开始时间 **/
    private int dayOffset = 20;
    /** 启用阅读制卡插件 **/
    private boolean deckreader = true;
    /** 启用模拟考试制卡插件 **/
    private boolean decktests = true;
    /** 默认字体 **/
    private int defaultFont = 0;
    /** 全屏显示 **/
    private int fullscreenMode = 0;
    /**
     * 主界面功能
     * 0 显示全部功能
     * 1 牌组 + 阅读
     * 2 牌组 + 模考
     * 3 我的课程
     */
    private int homePageStyle = 0;


    /**
     * 复习界面评论按钮样式
     * 0 常规 （图片+文字）
     * 1 时间+文字
     * 2 时间
     */
    private int answerButtonType =0;
    /**
     * 版本号
     */
    private String version = "";

    /** 图片缩放 **/
    private int imageZoom = 100;
    /** 保持屏幕常亮 **/
    private boolean keepScreenOn = false;
    /** 语言 **/
    private int language = 0;
    /** 提前学习的限制 **/
    private int learnCutoff = 20;
    /** 通知类型 **/
    private int minimumCardsDueForNotification = 0;
    /** 新卡片位置 **/
    private int newSpread = 0;
    /** 默认字体适用范围 **/
    private int overrideFontBehavior = 0;
    /** 卡片浏览器字体缩放 **/
    private int relativeCardBrowserFontSize = 100;
    /** 错误报告 **/
    private int reportErrorMode = 2;
    /** 显示按扭时间 **/
    private boolean showEstimates = false;
    /** 获取媒体文件 **/
    private boolean syncFetchesMedia = true;
    /** 主题标志位   0:灰色主题    1:蓝色主题 **/
    private int themeIndex = 1;
    /** 时间盒时间限制 **/
    private int timeLimit = 0;
    /** 文本转语音 **/
    private boolean tts = false;
    /** 新牌组  **/
    private int useCurrent = 0;
    /** 呼吸灯 **/
    private boolean widgetBlink = false;
    /** 震动 **/
    private boolean widgetVibrate = false;

    public String getAboutAika() {
        return AboutAika;
    }

    public void setAboutAika(String aboutAika) {
        AboutAika = aboutAika;
    }

    public int getAnswerButtonSize() {
        return answerButtonSize;
    }

    public void setAnswerButtonSize(int answerButtonSize) {
        this.answerButtonSize = answerButtonSize;
    }

    public boolean isAutomaticSyncMode() {
        return automaticSyncMode;
    }

    public void setAutomaticSyncMode(boolean automaticSyncMode) {
        this.automaticSyncMode = automaticSyncMode;
    }

    public int getBackupMax() {
        return backupMax;
    }

    public void setBackupMax(int backupMax) {
        this.backupMax = backupMax;
    }

    public int getBrowserEditorFont() {
        return browserEditorFont;
    }

    public void setBrowserEditorFont(int browserEditorFont) {
        this.browserEditorFont = browserEditorFont;
    }

    public int getCardZoom() {
        return cardZoom;
    }

    public void setCardZoom(int cardZoom) {
        this.cardZoom = cardZoom;
    }

    public boolean isCenterVertically() {
        return centerVertically;
    }

    public void setCenterVertically(boolean centerVertically) {
        this.centerVertically = centerVertically;
    }

    public boolean isConvertFenText() {
        return convertFenText;
    }

    public void setConvertFenText(boolean convertFenText) {
        this.convertFenText = convertFenText;
    }

    public int getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(int dayOffset) {
        this.dayOffset = dayOffset;
    }

    public boolean isDeckreader() {
        return deckreader;
    }

    public void setDeckreader(boolean deckreader) {
        this.deckreader = deckreader;
    }

    public boolean isDecktests() {
        return decktests;
    }

    public void setDecktests(boolean decktests) {
        this.decktests = decktests;
    }

    public int getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(int defaultFont) {
        this.defaultFont = defaultFont;
    }

    public int getFullscreenMode() {
        return fullscreenMode;
    }

    public void setFullscreenMode(int fullscreenMode) {
        this.fullscreenMode = fullscreenMode;
    }

    public int getHomePageStyle() {
        return homePageStyle;
    }

    public void setHomePageStyle(int homePageStyle) {
        this.homePageStyle = homePageStyle;
    }

    public int getImageZoom() {
        return imageZoom;
    }

    public void setImageZoom(int imageZoom) {
        this.imageZoom = imageZoom;
    }

    public boolean isKeepScreenOn() {
        return keepScreenOn;
    }

    public void setKeepScreenOn(boolean keepScreenOn) {
        this.keepScreenOn = keepScreenOn;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getLearnCutoff() {
        return learnCutoff;
    }

    public void setLearnCutoff(int learnCutoff) {
        this.learnCutoff = learnCutoff;
    }

    public int getMinimumCardsDueForNotification() {
        return minimumCardsDueForNotification;
    }

    public void setMinimumCardsDueForNotification(int minimumCardsDueForNotification) {
        this.minimumCardsDueForNotification = minimumCardsDueForNotification;
    }

    public int getNewSpread() {
        return newSpread;
    }

    public void setNewSpread(int newSpread) {
        this.newSpread = newSpread;
    }

    public int getOverrideFontBehavior() {
        return overrideFontBehavior;
    }

    public void setOverrideFontBehavior(int overrideFontBehavior) {
        this.overrideFontBehavior = overrideFontBehavior;
    }

    public int getRelativeCardBrowserFontSize() {
        return relativeCardBrowserFontSize;
    }

    public void setRelativeCardBrowserFontSize(int relativeCardBrowserFontSize) {
        this.relativeCardBrowserFontSize = relativeCardBrowserFontSize;
    }

    public int getReportErrorMode() {
        return reportErrorMode;
    }

    public void setReportErrorMode(int reportErrorMode) {
        this.reportErrorMode = reportErrorMode;
    }

    public boolean isShowEstimates() {
        return showEstimates;
    }

    public void setShowEstimates(boolean showEstimates) {
        this.showEstimates = showEstimates;
    }

    public boolean isSyncFetchesMedia() {
        return syncFetchesMedia;
    }

    public void setSyncFetchesMedia(boolean syncFetchesMedia) {
        this.syncFetchesMedia = syncFetchesMedia;
    }

    public boolean isSystemVoice() {
        return SystemVoice;
    }

    public void setSystemVoice(boolean systemVoice) {
        SystemVoice = systemVoice;
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isTts() {
        return tts;
    }

    public void setTts(boolean tts) {
        this.tts = tts;
    }

    public int getUseCurrent() {
        return useCurrent;
    }

    public void setUseCurrent(int useCurrent) {
        this.useCurrent = useCurrent;
    }

    public boolean isWidgetBlink() {
        return widgetBlink;
    }

    public void setWidgetBlink(boolean widgetBlink) {
        this.widgetBlink = widgetBlink;
    }

    public boolean isWidgetVibrate() {
        return widgetVibrate;
    }

    public void setWidgetVibrate(boolean widgetVibrate) {
        this.widgetVibrate = widgetVibrate;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public int getAnswerButtonType() {
        return answerButtonType;
    }

    public void setAnswerButtonType(int answerButtonType) {
        this.answerButtonType = answerButtonType;
    }

}
