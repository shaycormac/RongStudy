package com.assassin.rongstudy.util.rong.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/5/10 10:06
 * @Version: 1.0
 * @Description: 融云好友添加成功 ,继承MessageContent
 */
@MessageTag(value = "RC:AgreeReq")
public class AgreedFriendMessage extends MessageContent {
    private String friendId;
    private String message;

    public AgreedFriendMessage(String friendId, String message) {
        this.friendId = friendId;
        this.message = message;
    }

    public AgreedFriendMessage(byte[] data)
    {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            setFriendId(jsonObj.getString("friendId"));
            setMessage(jsonObj.getString("message"));
            if(jsonObj.has("user")){
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    /**
     * 构造函数。
     *
     * @param in 初始化传入的 Parcel。
     */
    public AgreedFriendMessage(Parcel in) {
        setFriendId(ParcelUtils.readFromParcel(in));
        setMessage(ParcelUtils.readFromParcel(in));
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    @Override
    public byte[] encode()
    {
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("friendId", friendId);
            jsonObj.put("message", message);

            if(getJSONUserInfo() != null)
                jsonObj.putOpt("user",getJSONUserInfo());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<AgreedFriendMessage> CREATOR = new Creator<AgreedFriendMessage>() {

        @Override
        public AgreedFriendMessage createFromParcel(Parcel source) {
            return new AgreedFriendMessage(source);
        }

        @Override
        public AgreedFriendMessage[] newArray(int size) {
            return new AgreedFriendMessage[size];
        }
    };
    

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, message);
        ParcelUtils.writeToParcel(dest, friendId);
        ParcelUtils.writeToParcel(dest, getUserInfo());
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
