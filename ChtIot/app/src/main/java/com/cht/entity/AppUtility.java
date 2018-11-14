package com.cht.entity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AppUtility {
    //定義常數
    public static final String  HOSTED="https://iot.cht.com.tw/iot/v1";
    //讀取特定傳感器最後一筆資訊
    public static final String  BEDDHT="/device/5604483035/sensor/bedroom/rawdata";
    //範圍時間內的多筆溫溼度資料
    public static final String BEDDHTRANGE="/device/5604483035/sensor/bedroom/rawdata?start=%s&end=%s";
    public static final String  READAPIKEY="PKWVLF9JM7MOIEFS40";

    //產生對話盒
    public static AlertDialog createAlertDialog(Context context, String title, CharSequence message){
        //工廠建構一個對化盒物件
        AlertDialog dialog=new AlertDialog.Builder(context,android.R.style.Theme_Material_Light_Dialog)
                .setTitle(title)
                .setMessage(message)
                .create();
        return dialog;

    }
    //取出現在時間點的iso8601
    public static String getISO8601DateString(int hourInterval)
    {
        int interval=hourInterval*60*60*1000;
        //1.取出現在時間點 Calendar class 工廠模式 產生物件 設定時區
        Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);
        String msg=String.format("%d-%d-%dT%d:%d",year,month,day,hour,min);
        Log.i("時間",msg);
        long time=calendar.getTimeInMillis();
        Date curDate=calendar.getTime();
        Date date=new Date(time-interval);
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }
}
