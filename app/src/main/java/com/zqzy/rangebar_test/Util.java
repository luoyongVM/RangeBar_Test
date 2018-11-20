package com.zqzy.rangebar_test;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by wschun on 2016/9/30.
 */

public class Util {
    private static final String TAG ="Util" ;

    public static void printCursor(Cursor cursor){
        if (cursor==null)
            return;

        Log.i(TAG, "printCursor: 条目个数"+cursor.getCount());


        while (cursor.moveToNext()){
            Log.i(TAG, "==============================================");
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String value=cursor.getString(i);
                String name=cursor.getColumnName(i);
                Log.i(TAG, "printCursor: name="+name+";value="+value);
            }
        }

    }

    // 格式化文件名：dida.mp3 --> dida
   public static String formatName(String name) {
       int i = name.indexOf(".");
       String newName = name.substring(0, i);
       return newName;
   }


    public static String getSystemversion(){
        return android.os.Build.VERSION.RELEASE;
    }
    public static String getPhoneModel(){
        return android.os.Build.MODEL;
    }

    public static String formatDuration(long duration) {
        int HOUR = 60 * 60 * 1000;//xiaosh
        int MINUTE = 60 * 1000;//分钟
        int SECOND = 1000;//秒
        //计算小时
        int hour = (int) (duration / HOUR);
        long remain = duration % HOUR;
        //计算分钟
        int minute = (int) (remain / MINUTE);
        remain = remain % MINUTE;
        //计算秒
        int second = (int) (remain / SECOND);

        if (hour==0){
            return  String.format("%02d:%02d",minute,second);
        }else {
            return  String.format("%02d:%02d:%02d",hour,minute,second);
        }
    }

    /**
     *
     * @param picH 图片的高度
     * @param picW 图片的宽度
     * @param context
     * @return 计算出来的图片控件宽高，x 为宽度，y 为高度
     */
    public static Point computeImgSize(int picW, int picH, Context context){
        int imgW = getScreenWidth(context);
        int imgH = picH * imgW / picW;
        return new Point(imgW,imgH);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        return width;
    }
    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 格式化时间。00:01 或者 01:02:03
     * @param time
     * @return
     */
    public static String formatTime(int time){
//        time / 60 / 60 /1000
        // 使用 Calendar 获取小时数
        Calendar calendar = Calendar.getInstance();
        calendar.clear(); // 清空原有数据
        calendar.add(Calendar.MILLISECOND,time);
        int hour = calendar.get(Calendar.HOUR);
//        LogUtils.e(TAG,"Util.formatTime,hour="+hour);

        if (hour > 1){
            return (String) DateFormat.format("hh:mm:ss",calendar);
        }else{
            return (String) DateFormat.format("mm:ss",calendar);
        }
    }
}
