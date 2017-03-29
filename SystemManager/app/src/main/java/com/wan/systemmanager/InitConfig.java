package com.wan.systemmanager;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 万文杰 on 2016/6/23.
 */
public class InitConfig extends Application{
    static String baseUrl="http://10.10.146.156:8080/WebSysem/";//10.1.45.242  10.2.19.180
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Xutils
        x.Ext.init(this);
    }
}
