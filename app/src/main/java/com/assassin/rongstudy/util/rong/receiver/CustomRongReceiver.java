package com.assassin.rongstudy.util.rong.receiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/5/12 08:34
 * @Version: 1.0
 * @Description: 为了接收推送消息，您需要自定义一个继承自 PushMessageReceiver 类的 BroadcastReceiver
 * (必须实现,否则会收不到推送消息)，实现其中的 onNotificationMessageArrived，onNotificationMessageClicked
 * 然后把该 receiver 注册到 AndroidManifest.xml 文件中。
 */

public class CustomRongReceiver extends PushMessageReceiver {
    // onNotificationMessageArrived 用来接收服务器发来的通知栏消息(消息到达客户端时触发)，默认return false，
    // 通知消息会以融云 SDK 的默认形式展现。如果需要自定义通知栏的展示，在这里实现自己的通知栏展现代码，同时 return true 即可。
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        //得到传过来的类型？？？
        pushNotificationMessage.getConversationType();

        return false;
    }
//onNotificationMessageClicked 是在用户点击通知栏消息时触发 (注意:如果自定义了通知栏的展现，则不会触发)，
// 默认 return false 。如果需要自定义点击通知时的跳转，return true 即可
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
