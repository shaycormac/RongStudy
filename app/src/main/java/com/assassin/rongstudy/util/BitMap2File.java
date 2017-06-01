package com.assassin.rongstudy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: Shay-Patrick-Cormac
 * @Email: fang47881@126.com
 * @Ltd: GoldMantis
 * @Date: 2017/6/1 17:16
 * @Version:
 * @Description:
 */

public class BitMap2File
{
    public static String getFilePath(Context context,String userName)
    {
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context,android.R.color.black));
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(100,100,90,paint);
        //打开文件夹
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "hehe.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
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
}
