package com.zjuwepension.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tool {

    public static String getDate(){
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(currentTime);
    }
}
