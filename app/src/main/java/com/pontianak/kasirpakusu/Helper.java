package com.pontianak.kasirpakusu;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public SharedPreferences prefs;
    public SharedPreferences.Editor meditor;
    public Locale localeID = new Locale("in", "ID");
    public NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);


    public String session;
    public Helper(Context ctx){

        prefs=ctx.getSharedPreferences("ayampakusu",ctx.MODE_PRIVATE);
        meditor = prefs.edit();
        session=prefs.getString("Set-Cookie"," ");

    }
    public String cariwaktu(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String cariwaktusimpel(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public String WaktuOnlyHourAndTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String torupiah(int val) {
        return "Rp " + String.format("%,d", val).replace(',', '.');

    }

    public int meisinteger(String mystring) {
        try {
            return Integer.parseInt(mystring);

        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    public double meisdouble(String mystring) {
        try {
            return Double.parseDouble(mystring);

        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
}
