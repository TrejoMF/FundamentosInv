package com.multimarca.tae.voceadorestae.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by erick on 1/9/16. Multimarca
 */
public class Functions {

    public static String FOLIO(long folio) {

        return Global.APPID + String.format("%07d", folio);
    }

    public static String getDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
    public static String CleanDateString(String Date) {

        return Date.replace("T", " ").replaceAll("\\.([^/]*)$", "");
    }

    public static long daysBetweenDates(String DateI, String DateF) {


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date dateI = simpleDateFormat.parse(DateI);
            Date dateF = simpleDateFormat.parse(DateF);


            long diff = dateF.getTime() - dateI.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000);
            int diffInDays = (int) ((dateF.getTime() - dateI.getTime()) / (1000 * 60 * 60 * 24));
            return (long) diffInDays;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public static String FormatMoney(String value) {
        try {

            String number = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(value));
            return "$ " + number;
        } catch (Exception e) {
            Log.e("ErrorConvert", "Error en la conversion", e);
            return value;
        }
    }
}
