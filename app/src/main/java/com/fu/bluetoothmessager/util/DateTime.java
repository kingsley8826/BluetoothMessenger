package com.fu.bluetoothmessager.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tuan-FPT on 31/10/2017.
 */

public class DateTime {

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(c.getTime());
    }
}
