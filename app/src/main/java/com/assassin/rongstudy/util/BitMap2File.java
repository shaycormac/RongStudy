package com.assassin.rongstudy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.assassin.rongstudy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/6/1 17:16
 * @Version:
 * @Description:
 */

public enum BitMap2File
{
    INSTANCE;
    private Paint paint;
    Bitmap bitmap;
    Canvas canvas;
    BitMap2File() {
        paint = new Paint();
        bitmap = Bitmap.createBitmap(WIDTH, WIDTH, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    /**
     * UrlImageViewHelper 存放图片的默认文件夹
     */
    
    
    private static final String HOME_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "GM_rong_icon";
    public static final int WIDTH = 100;

    /**
     * 存放图片的默认文件夹
     *
     * @return
     */
    public  String getHomeDirectory() {
        File dir = new File(HOME_DIR);
        if (!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
        return HOME_DIR;
    }
    
    /**
     * 根据userId生成对应的缩略图，唯一，在文件夹GM_rong_icon下面
     * @param context
     * @param userName
     * @return
     */
    public  String getFilePath(Context context,String userId,String userName)
    {
        //先判断有无？？
        File file = new File(getHomeDirectory(), userId+".jpg");
        if (file.exists() && file.isFile())
            return file.getPath();
        String showUserName=getIconName(userName);
        //设置宽高比均为50dp
        paint.setAntiAlias(true);
        paint.setColor(getRandomColor(context));
       /* Bitmap bitmap = Bitmap.createBitmap(WIDTH, WIDTH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);*/
        int r = WIDTH/2;
        canvas.drawCircle(r,r,r,paint);
        paint.setColor(ContextCompat.getColor(context,android.R.color.white));
        
       paint.setTextSize(DensityUtil.sp2px(context,16f));
       paint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
       paint.getTextBounds(showUserName, 0, showUserName.length(), bounds);
        Paint.FontMetricsInt fontMetrics =paint.getFontMetricsInt();
        int baseline = (WIDTH - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(showUserName,WIDTH / 2 - bounds.width() / 2, baseline,paint);
        //打开文件夹
        try {
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            Log.d("已经完后才能", file.getPath());
            return file.getPath();
        } catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;

    }

    public boolean isStorgeInCache(String userId) 
    {
        
        return false;
    }



    public static int[] contactLogBgList = new int[]{R.color.contact_deep_blue, R.color.contact_deep_green,
            R.color.contact_deep_orange, R.color.contact_deep_red, R.color.contact_light_blue, R.color.contact_light_green,
            R.color.contact_light_grey, R.color.contact_light_orange};


    /**
     * 生成随机的颜色
     *
     * @return
     */
    public static Random random = new Random();
    public static int getRandomColor(Context context)
    {
        return ContextCompat.getColor(context, contactLogBgList[random.nextInt(contactLogBgList.length)]);
    }

    /**
     * 根据名字返回后面的两位字符
     * @param name
     * @return
     */
    public static String getIconName(String name)
    {
        if (TextUtils.isEmpty(name))
            return "无名";
        int length = name.length();
        if (length==1 || length==2)
            return name;
        else
        {
            return name.substring(1, 3);
        }
    }
}
