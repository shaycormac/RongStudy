package com.assassin.rongstudy.util;

import android.net.Uri;
import android.text.TextUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/5/9 17:19
 * @Version: 1.0
 * @Description: 融云测试
 */

public class RongUtil
{
    private static final String TAG = "RongUtils";
    public static void connectRong(String token, final String nickName, final String face)
    {
        if (TextUtils.isEmpty(token))
        {
            VolleyLog.d("没有得到token");
            return;
        }
        RongIM.connect(token, new RongIMClient.ConnectCallback() 
        {
            @Override
            public void onTokenIncorrect() 
            {
                VolleyLog.d("%s %s", TAG, "Connect Token 失效的状态处理，需要重新获取 Token");
                //todo 到登录页面，重新登录逻辑

            }

            @Override
            public void onSuccess(String userId)
            {
                VolleyLog.d("%s %s", TAG, "userId: " + userId);
                if (!TextUtils.isEmpty(nickName))
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(userId, nickName, Uri.parse(null == face ? "" : face)));
                //设置监听
               // RongIM.getInstance().setOth
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) 
            {
                VolleyLog.d("融云登录错误，代码为：%s %s", TAG, "errorCode: " + errorCode);
            }
        });
        
    }
}
