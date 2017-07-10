package com.multimarca.tae.voceadorestae.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by erick on 12/27/15. Multimarca
 */
public class Global {


    public static String URL = "http://apitae.eventamovil.mx/";
    public static String URL_D = "http://www.devapicoadsy.eventamovil.mx/";
    //    public static String URL_D = "http://192.168.1.226:8080/";
    public static String mPreference = "Preference";
    public static String WS  = "Local_9094";
    public static String WS_D  = "Local_9091";
    public static String PLATAFORM  = "19";

    static SharedPreferences preferences;

    public static String TOKEN(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("token","");

    }
    public static void EdTOKEN(Context ctx, String token) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", token);
        editor.apply();
    }


    public static String USUARIO(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("usuario","");

    }
    public static void EdUSUARIO(Context ctx, String user) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usuario", user);
        editor.apply();
    }

    public static String NAME(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("name","");

    }
    public static void EdNAME(Context ctx, String name) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public static String PAPI(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("papi","");

    }
    public static void EdPAPI(Context ctx, String papi) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("papi", papi);
        editor.apply();
    }

    public static String TIPOP(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("tipop","");

    }
    public static void EdTIPOP(Context ctx, String tipop) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tipop", tipop);
        editor.apply();
    }
    public static String PASS(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getString("pass","");

    }
    public static void EdPASS(Context ctx, String pass) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pass", pass);
        editor.apply();
    }

    public static Boolean LOGGED(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getBoolean("LOGGED", false);

    }
    public static void EdLOGGED(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("LOGGED", !LOGGED(ctx));
        editor.apply();
    }

    public static Boolean ACTUALIZAR(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getBoolean("actualizar",true);

    }
    public static int VCARRIERS(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        return preferences.getInt("vcarrier", 10);

    }
    public static void EdVCARRIERS(Context ctx, int version) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(  "vcarrier", version);
        editor.apply();
    }

    public static void EdACTUALIZAR(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("actualizar", !ACTUALIZAR(ctx));
        editor.apply();
    }

    public static void EdTrueACTUALIZAR(Context ctx) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("actualizar", false);
        editor.apply();

    }
    public static void EdVACTUALIZAR(Context ctx, Boolean update) {
        preferences = ctx.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("actualizar", update);
        editor.apply();

    }


    public static int APPID = 701;

    public static class Types {
        public static int SERVICIO = 0;
        public static int TAE =  1;
    }


    //Play Service Token
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


}
