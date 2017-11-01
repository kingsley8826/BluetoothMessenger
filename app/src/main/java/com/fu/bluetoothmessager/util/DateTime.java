package com.fu.bluetoothmessager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateTime {

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }
}
