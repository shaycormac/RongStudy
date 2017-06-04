package com.assassin.rongstudy.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
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
                //todo 重新请求融云的token接口，获取数据

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

    /**
     * 从会话列表中移除某一会话（退出聊天群组由服务器操作）
     */
    public static void removeGroupConversation(String groupId) {
        if (TextUtils.isEmpty(groupId))
            return;
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            // 从会话列表中移除某一会话，但是不删除会话内的消息。
            RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {

                @Override
                public void onSuccess(Boolean aBoolean)
                {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) 
                {
                }
            });
        }

    }

    /**
     * 登出
     */
    public static void logout() {
        if (RongIM.getInstance() != null)
            RongIM.getInstance().logout();
    }

    /**
     * 启动会话界面
     *
     * @param context
     * @param targetUserId
     * @param title
     * @param stranger     是否是陌生人
     */
    public static void startPrivateChat(@NonNull Context context, String targetUserId, String title, boolean stranger) {
        if ( TextUtils.isEmpty(targetUserId))
            return;
        if (RongIM.getInstance() == null)
            return;

        final Uri.Builder builder = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", targetUserId)
                .appendQueryParameter("title", title);

        if (stranger)
            builder.appendQueryParameter("stranger", "true");

        context.startActivity(new Intent("android.intent.action.VIEW", builder.build()));
    }

    /**
     * 启动会话界面
     *
     * @param context
     * @param targetUserId
     * @param title
     */
    public static void startPrivateChat(@NonNull Context context, String targetUserId, String title) {
        startPrivateChat(context, targetUserId, title, false);
    }

    /**
     * 聊天室
     *
     * @param context
     * @param targetId 聊天室 Id。
     * @param title    聊天的标题，如果传入空值，则默认显示会话的名称。
     */
    public static void startChatroom(@NonNull Context context, String targetId, String title) {
        if (RongIM.getInstance() != null && !TextUtils.isEmpty(targetId))
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.CHATROOM, targetId, title);

    }
    
    /**
     * 启动客服聊天界面。
     *
     * @param context
     * @param targetId 客服 Id。
     * @param title    客服标题。
     */
    public static void startPublicService(@NonNull Context context, String targetId, String title) {
        if (TextUtils.isEmpty(targetId)) {
            Toast.makeText(context, "暂时没有客服", Toast.LENGTH_SHORT).show();
            return;
        }
        if (RongIM.getInstance() != null)
            // RongIM.getInstance().startConversation(context, Conversation.ConversationType.PUBLIC_SERVICE, targetId, title);
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.APP_PUBLIC_SERVICE, targetId, title);
    }


    public static void startGroup(@NonNull Context context, String targetId, String title) {
        if (TextUtils.isEmpty(targetId)) {
            Toast.makeText(context, "没有本群信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (RongIM.getInstance() != null)
            // RongIM.getInstance().startConversation(context, Conversation.ConversationType.PUBLIC_SERVICE, targetId, title);
            RongIM.getInstance().startConversation(context, Conversation.ConversationType.GROUP, targetId, title);
    }

}
