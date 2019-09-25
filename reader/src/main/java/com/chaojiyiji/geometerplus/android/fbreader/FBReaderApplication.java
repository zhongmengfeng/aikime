/*
 * Copyright (C) 2007-2015 FBReader.com.chaojiyiji Limited <contact@fbreader.com.chaojiyiji>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package com.chaojiyiji.geometerplus.android.fbreader;

import com.chaojiyiji.geometerplus.zlibrary.ui.android.BuildConfig;
import com.chaojiyiji.geometerplus.zlibrary.ui.android.library.ZLAndroidApplication;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.common.util.LogUtil;

public class FBReaderApplication extends ZLAndroidApplication {
    private static FBReaderApplication instance;

    public static FBReaderApplication getInstance(){
        return  instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Config.DEBUG = BuildConfig.DEBUG;
        LogUtil.e("onCreate: Config.DEBUG =  " + Config.DEBUG);
        UMShareAPI.get(this);
        // Config.REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";
        Config.isJumptoAppStore = true;
    }

    {
        //豆瓣RENREN平台目前只能在服务器端配置

        // AppID：wx29626f68ab6aee41 签名:f7a6e57fe017ac477a147c1edc33ca65

        // AppID：wx01754ee3c11b2f17 签名:2fd1a34705589693382d3d1f2837ac9d
        PlatformConfig.setWeixin("wx29626f68ab6aee41", "f7a6e57fe017ac477a147c1edc33ca65");
//        PlatformConfig.setWeixin("wx01754ee3c11b2f17", "2fd1a34705589693382d3d1f2837ac9d");

        PlatformConfig.setSinaWeibo("628633621", "3cce6718abeab0759ab6fbefe40e923f","http://sns.whalecloud.com/sina2/callback");

        PlatformConfig.setQQZone("1105783665", "8zMzCsCtLIffWUvC");

    }
}
