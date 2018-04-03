package com.ericjohnson.favoritemovie.utils;

/**
 * Created by EricJohnson on 3/3/2018.
 */

public class DateUtil {

    public static String getReadableDate(String date) {
        String[] splitDate = date.split("-");
        String[] month = new String[]{"January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"};
        String monthName = month[Integer.valueOf(splitDate[1]) - 1];
        return splitDate[2] + " " + monthName + " " + splitDate[0];
    }
}
