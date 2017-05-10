package com.assassin.rongstudy.util.rong;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.assassin.rongstudy.util.RongUtil;
import com.assassin.rongstudy.util.ToastUtil;
import com.assassin.rongstudy.util.VolleyLog;
import com.assassin.rongstudy.util.rong.message.AgreedFriendMessage;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static android.content.ContentValues.TAG;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/5/10 09:39
 * @Version: 1.0
 * @Description:
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */

//自定义通知消息没有继承，后续
public  class RongEvent implements RongIMClient.OnReceiveMessageListener,RongIM.OnSendMessageListener
    ,RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.ConversationBehaviorListener,
        RongIMClient.ConnectionStatusListener, RongIM.LocationProvider, RongIM.ConversationListBehaviorListener
    ,RongIM.GroupUserInfoProvider
{

    private static RongEvent rongEvent;

    private Context context;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (rongEvent == null) {

            synchronized (RongEvent.class) {

                if (rongEvent == null) {
                    rongEvent = new RongEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongEvent(Context context) {
        this.context = context;
        initDefaultListener();
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() 
    {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        // RongIM.setLocationProvider(this);//设置地理位置提供者,
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setGroupUserInfoProvider(this, true);
        //消息体内是否有 userinfo 这个属性
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        // todo 自定义 push 通知。（新版本重新集成，待续）
      //   RongIM.getInstance().getRongIMClient().setOnReceivePushMessageListener(this);
        
    }

    /**
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {

        RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);//设置连接状态监听器。

//      todo 自定义，需要扩展  扩展功能自定义
       /* InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                // new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
                // new VoIPInputProvider(RongContext.getInstance()),// 语音通话
                new ContactsProvider(RongContext.getInstance()),//通讯录
        };

        InputProvider.ExtendProvider[] provider2 = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                // new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
                new ContactsProvider(RongContext.getInstance()),//通讯录
        };

        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider2);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider2);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider2);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider2);*/
    }
    
    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(final Message message, int left)
    {
        // TODO 可以添加消息计数功能
        MessageContent messageContent = message.getContent();
        //需要自己处理

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            
            VolleyLog.d("onReceived-TextMessage:getExtra-----%s",textMessage.getExtra());
            VolleyLog.d("onReceived-TextMessage-----%s",textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            VolleyLog.d("onReceived-ImageMessage-----%s",imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            VolleyLog.d("onReceived-voiceMessage-----%s",voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            VolleyLog.d("onReceived-RichContentMessage-----%s",richContentMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
            VolleyLog.d("onReceived-informationNotificationMessage-----%s",informationNotificationMessage.getMessage());
           //todo  获取该用户的最新数据，并更新用户的信息（token，name,url之类）
            /*new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run() 
                {
                    new Api(rongUserInfoCB, mContext).getRongUserInfo(message.getSenderUserId());
                }
            });*/
        } else if (messageContent instanceof AgreedFriendMessage) {//好友添加成功消息
            AgreedFriendMessage agreedFriendMessage = (AgreedFriendMessage) messageContent;
            VolleyLog.d("onReceived-agreedFriendMessage:-----%s",agreedFriendMessage.getMessage());
            receiveAgreeSuccess(agreedFriendMessage);
        } else if (messageContent instanceof ContactNotificationMessage) {//好友添加消息
            ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
            VolleyLog.d("onReceived-ContactNotificationMessage:getExtra-----%s",contactContentMessage.getExtra());
            VolleyLog.d("onReceived-ContactNotificationMessage:+getmessage-----%s",contactContentMessage.getMessage().toString());
            Intent in = new Intent();
            //todo 好友添加成功，发送广播 action以后需要做
           // in.setAction(Params.INTENT_ACTION.ACTION_DMEO_RECEIVE_MESSAGE);
            in.putExtra("rongCloud", contactContentMessage);
            in.putExtra("has_message", true);
            context.sendBroadcast(in);
        } else if (messageContent instanceof DiscussionNotificationMessage) {//讨论组通知消息
            DiscussionNotificationMessage discussionNotificationMessage = (DiscussionNotificationMessage) messageContent;
            VolleyLog.d("onReceived-discussionNotificationMessage:getExtra-----%s", discussionNotificationMessage.getOperator());
            //讨论组的名字
            setDiscutionName(message.getTargetId());
        } else if (messageContent instanceof CommandMessage) {
            CommandMessage msg = (CommandMessage) messageContent;
            if ("quit".equals(msg.getName())) {
                try {
                    JSONObject jobj = new JSONObject(msg.getData());
                    // 获取聊天群组的ID
                    String groupId = jobj.getString("groupId");
                    RongUtil.removeGroupConversation(groupId);
                } catch (Exception e) {
                }
                return true;
            }
        } else {
            VolleyLog.d("onReceived-其他消息，自己来判断处理");
        }

        return false;
    }

    /**
     * 同意添加好友
     *
     * @param agreedFriendMessage
     */
    private void receiveAgreeSuccess(AgreedFriendMessage agreedFriendMessage) {
        
        /*Intent in = new Intent();
        in.setAction(MainActivity.ACTION_DMEO_AGREE_REQUEST);
        in.putExtra("AGREE_REQUEST", true);
        mContext.sendBroadcast(in);*/

    }

    private void setDiscutionName(String targetId) {

        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null)
        {
            RongIM.getInstance().getRongIMClient().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>() 
            {
                @Override
                public void onSuccess(Discussion discussion) 
                {
                    RongIM.getInstance().refreshDiscussionCache(discussion);
                    VolleyLog.d("------discussion.getName---:--%s",discussion.getName());
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) 
                {
                   VolleyLog.d("Rong setDiscutionName failed:--%s",e.getMessage());
                }
            });
        }
    }

    /**
     * 消息发送前监听器处理接口（是否发送成功可以从SentStatus属性获取）。
     *
     * @param message 发送的消息实例。
     * @return 处理后的消息实例。
     */
    @Override
    public Message onSend(Message message) 
    {
        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
        }

        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) 
    {
        if (message.getSentStatus() == Message.SentStatus.FAILED) {

            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {//不在聊天室

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {//不在讨论组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {//不在群组
                //todo  不在群组，调用后台接口，有服务器判断是否加入群里
               // new Api(null, PuApp.get()).nonGroupMembers(message.getTargetId());
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {//你在他的黑名单中
                ToastUtil.INSTANCE.toastBottom(context,"你在对方的黑名单中");
            }
        }
        return false;
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId)
    {
        if (TextUtils.isEmpty(userId))
            return null;
   //todo 获取用户的信息（可以设计从数据库中取，没有的话再请求网络）
       /* CacheDbHelper mCacheDbHelper = DBManager.getInstance().getCacheDbHelper();
        RongUserInfo mRongUserInfo = mCacheDbHelper.queryRongUserInfo(userId);
        if (mRongUserInfo == null) 
        {
            new Handler(Looper.getMainLooper()).post(new Runnable() 
            {
                @Override
                public void run() {
                    new Api(rongUserInfoCB, mContext).getRongUserInfo(userId);
                }
            });

            return null;
        } else
            return new UserInfo(mRongUserInfo.userId, mRongUserInfo.name, Uri.parse(mRongUserInfo.portraitUri));*/
        return null;
    }

    /**
     * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
     *
     * @param groupId 群组 Id.
     * @return 群组信息，（注：由开发者提供群组信息）。
     */
    @Override
    public Group getGroupInfo(String groupId) 
    {
        if (TextUtils.isEmpty(groupId))
            return null;
        //todo 获取群组的信息（可以设计从数据库中取，没有的话再请求网络）
      /*  CacheDbHelper mCacheDbHelper = DBManager.getInstance().getCacheDbHelper();
        RongGroupInfo group = mCacheDbHelper.queryRongGroupInfo(groupId);
        if (group == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    new Api(rongGroupInfoCB, mContext).getRongGroupInfo(groupId);
                }
            });

            return null;
        } else
            return new Group(group.groupId, group.groupName, Uri.parse(group.portraitUri));*/
        return null;
    }

    //用户的点击事件
    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo user)
    {
        if (user != null) {
            if (!conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE) && !conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) 
            {
                //todo 跳转到个人中心的页面
               /* Intent in = new Intent(context, OtherUserInfoActivity.class);
                in.putExtra(Params.INTENT_EXTRA.USER_ID, user.getUserId());
                context.startActivity(in);*/
            } else 
            {
               // RongIM.getInstance().startPublicServiceProfile(context, conversationType, user.getUserId());

            }
        }
        return false;
    }
   //暂时不做处理
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        VolleyLog.d("----onUserPortraitLongClick");
        return true;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(final Context context, View view, final Message message) 
    {
        Log.e(TAG, "----onMessageClick");

        //real-time location message begin
        if (message.getContent() instanceof RealTimeLocationStartMessage) 
        {
            RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(message.getConversationType(), message.getTargetId());

//            if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
//                startRealTimeLocation(context, message.getConversationType(), message.getTargetId());
//            } else
            if (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING) {


                final AlterDialogFragment alterDialogFragment = AlterDialogFragment.newInstance("", "加入位置共享", "取消", "加入");
                alterDialogFragment.setOnAlterDialogBtnListener(new AlterDialogFragment.AlterDialogBtnListener() {

                    @Override
                    public void onDialogPositiveClick(AlterDialogFragment dialog) {
                        RealTimeLocationConstant.RealTimeLocationStatus status = RongIMClient.getInstance().getRealTimeLocationCurrentState(message.getConversationType(), message.getTargetId());

                        if (status == null || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE) {
                            startRealTimeLocation(context, message.getConversationType(), message.getTargetId());
                        } else {
                            joinRealTimeLocation(context, message.getConversationType(), message.getTargetId());
                        }
                    }

                    @Override
                    public void onDialogNegativeClick(AlterDialogFragment dialog) {
                        alterDialogFragment.dismiss();
                    }
                });

                alterDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager());
            } else {

                if (status != null && (status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING || status == RealTimeLocationConstant.RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_CONNECTED)) {

                    /*Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
                    intent.putExtra("conversationType", message.getConversationType().getValue());
                    intent.putExtra("targetId", message.getTargetId());
                    context.startActivity(intent);*/
                }
            }
            return true;
        }

        //real-time location message end
        /**
         * demo 代码  替换成自己的代码。
         */
        if (message.getContent() instanceof LocationMessage) {
            /*Intent intent = new Intent(context, SOSOLocationActivity.class);
            intent.putExtra("location", message.getContent());
            context.startActivity(intent);*/
        } else if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
            Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
            Log.e(TAG, "----RichContentMessage-------");

        } else if (message.getContent() instanceof ImageMessage) {
            /*ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
            if (imageMessage.getThumUri() != null)
                intent.putExtra("thumbnail", imageMessage.getThumUri());
            context.startActivity(intent);*/

            ImageMessage imageMessage = (ImageMessage) message.getContent();
            //todo 放大图片浏览
           /* Intent intent = new Intent(context, ImageZoomActivity.class);
            intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, imageMessage.getLocalUri() == null ?
                    imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().toString());
            context.startActivity(intent);*/
        } else if (message.getContent() instanceof PublicServiceMultiRichContentMessage) {
            Log.e(TAG, "----PublicServiceMultiRichContentMessage-------");

        } else if (message.getContent() instanceof PublicServiceRichContentMessage) {
            Log.e(TAG, "----PublicServiceRichContentMessage-------");

        }
        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        VolleyLog.d("----onMessageLongClick");
        return false;
    }

    private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
        /*RongIMClient.getInstance().startRealTimeLocation(conversationType, targetId);

        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
        intent.putExtra("conversationType", conversationType.getValue());
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);*/
    }

    private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {
        /*RongIMClient.getInstance().joinRealTimeLocation(conversationType, targetId);

        Intent intent = new Intent(((FragmentActivity) context), RealTimeLocationActivity.class);
        intent.putExtra("conversationType", conversationType.getValue());
        intent.putExtra("targetId", targetId);
        context.startActivity(intent);*/
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) 
    {

        VolleyLog.d("ConnectionStatusListener : OnChanged: %s",status.toString());
        if (status.getValue() == ConnectionStatus.DISCONNECTED.getValue()) {
            /*RongUtils.connect(CalmlyBarSession.getUserTokenSession().rongyunToken, CalmlyBarSession.getUserTokenSession().uname,
                    CalmlyBarSession.getUserTokenSession().face);*/
        } else if (status.getValue() == ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.getValue()
                || status.getValue() == ConnectionStatus.TOKEN_INCORRECT.getValue()) {
            //todo  token失效，需要退出，回到登录页面
           // PuApp.get().logoutRong(PuApp.get());
        }
        
    }

    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param locationCallback 回调
     */
    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        /*DemoContext.getInstance().setLastLocationCallback(callback);
        //SOSO地图
        context.startActivity(new Intent(context, SOSOLocationActivity.class));*/
        
    }

   
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }
    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view, UIConversation conversation) 
    {
        MessageContent messageContent = conversation.getMessageContent();
        Log.e(TAG, "--------onConversationClick-------");
        if (messageContent instanceof TextMessage) {//文本消息

            TextMessage textMessage = (TextMessage) messageContent;
            textMessage.getExtra();
        } else if (messageContent instanceof ContactNotificationMessage) {
            Log.e(TAG, "---onConversationClick--ContactNotificationMessage-");

            // 暂不支持
            // context.startActivity(new Intent(context, NewFriendListActivity.class));

            return true;
        }

        if (conversation.getConversationType().equals(Conversation.ConversationType.DISCUSSION)) {
            Intent in = new Intent();
          //  in.setAction(Params.INTENT_ACTION.ACTION_DMEO_AT_ME);
            in.putExtra("DEMO_TARGETID", conversation.getConversationTargetId());
            in.putExtra("DEMO_PRESS", true);
            context.sendBroadcast(in);
        }

        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }
    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param uiConversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    //RongIM.GroupUserInfoProvider

    @Override
    public GroupUserInfo getGroupUserInfo(String groupId, String userId) 
    {
        if (userId.equals("47830"))
            return new GroupUserInfo(groupId, "47830", "张璐");
        else
            return null;
    }

    
}
