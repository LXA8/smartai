package com.tcy.smartai.service.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

   public static String getCurrentHourTime(Date time){
       //增加一个小时
       Long lastTime = time.getTime();
       String strDateFormat = "yyyyMMddHH";
       SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
       String lastTimeStr =  sdf.format(lastTime);
       return lastTimeStr;
   }

    public static String getCurrentDayTime(Date time){
        //增加一个小时
        Long lastTime = time.getTime();
        String strDateFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String lastTimeStr =  sdf.format(lastTime);
        return lastTimeStr;
    }

   public static String getPreHourTime(){
       Calendar calendar = Calendar.getInstance();
       calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
       String strDateFormat = "yyyyMMddHH";
       SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
       String preTimeStr =  sdf.format(calendar.getTime());
       return preTimeStr;
   }

    public static String getHourTime(Date time){
        //增加一个小时
        Long lastTime = time.getTime();
        String strDateFormat = "HH";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String lastTimeStr =  sdf.format(lastTime);
        return lastTimeStr;
    }

   public static Date strToDate(String timeStr) throws ParseException {
       String strDateFormat = "yyyyMMddHHmmss";
       SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        Date result = sdf.parse(timeStr);
       return result;

   }

    public static void main(String args[]) throws ParseException {
//        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
//        Date lastDate =  sdf.parse("2023-02-28 23:59:20");
//        String result = getCurrentHourTime(lastDate);
////        System.out.print(result.substring(8,10));
//
//        String time = "2023022501351315";
//        SimpleDateFormat sdf11 = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date timeStmpDate = sdf11.parse(time.substring(0,14));
//        System.out.print(timeStmpDate);

        String hour = "01";
        int hourInt = Integer.valueOf(hour);
        System.out.print(hourInt);
    }


}
