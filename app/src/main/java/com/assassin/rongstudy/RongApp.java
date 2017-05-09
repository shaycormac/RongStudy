package com.assassin.rongstudy;

import android.app.Application;
import android.content.Context;

import com.assassin.rongstudy.util.VolleyLog;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.store.PersistentCookieStore;

import java.util.logging.Level;

import io.rong.imkit.RongIM;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/5/9 14:03
 * @Version:
 * @Description:
 */

public class RongApp extends Application 
{

    private static RongApp mInstance;
    private static Context sContext;
    @Override
    public void onCreate() 
    {
        super.onCreate();
        mInstance = this;
        sContext = getApplicationContext();
        init();
        //数据库调式
        Stetho.initializeWithDefaults(this);
        //集成融云
        RongIM.init(this);
        
    }


    private void init() {

        //init okgo
        try {
            OkGo.init(this);
            OkGo.getInstance()
                    .debug(VolleyLog.TAG, Level.INFO, true)//打印内部异常,Okhttp Tag
                    .setConnectTimeout(10000)//超时
                    .setReadTimeOut(10000)
                    .setWriteTimeOut(10000)
                    .setRetryCount(3)//重连次数
                    .setCacheMode(CacheMode.NO_CACHE)//默认无缓存
                    .setCookieStore(new PersistentCookieStore());
            //添加拦截器 网络请求监测（正式上线可以注释掉）
            OkGo.getInstance().getOkHttpClientBuilder().addNetworkInterceptor(new StethoInterceptor());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static RongApp getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return sContext;
    }
}
