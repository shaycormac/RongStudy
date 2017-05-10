package com.assassin.rongstudy.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/4/26 16:03
 * @Version:1.0
 * @Description: toast单利模式。
 */

public enum ToastUtil 
{
    INSTANCE;
    private Toast mToast;

    public  void toastBottom(Context context, String message) 
    {
      showToast(context,message,Toast.LENGTH_SHORT);
    }

    public  void ToastLongMessage(Context context, String message)
    {
        showToast(context,message,Toast.LENGTH_LONG);
    }

    public  void ToastFailureMessage(Context context)
    {
        showToast(context,"提交失败，请重试!",Toast.LENGTH_SHORT);
    }

    public void cancelToast()
    {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    public  void ToastTopMsg(Context context, String message)
    {
        if (context == null || TextUtils.isEmpty(message)) 
        {
            return;
        }

        if (mToast==null)
        {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            mToast.show();
        }else
        {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
            mToast.show();
        }
    }

    
    private  void showToast(Context context,String message,int duration)
    {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }
        if (mToast==null)
        {
            mToast = Toast.makeText(context, message, duration);
        }else
        {
            mToast.setText(message);
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
