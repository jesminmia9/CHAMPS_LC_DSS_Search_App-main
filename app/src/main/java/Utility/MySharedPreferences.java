package Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import Common.ProjectSetting;


/**
 * Created by SadiqMdAsif on 29/10/2016.
 */

public class MySharedPreferences {

    public static final String PREFS_NAME = ProjectSetting.ProjectName;
    public static final String PREFS_KEY = "AOP_PREFS_String";

    public MySharedPreferences() {
        super();
    }

    public void save(Context context, String key, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(key, text); //3

        editor.apply(); //4
    }

    public static String getValue(Context context, String key) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, "NULL");
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();
    }

    public void removeValue(Context context, String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.apply();
    }

    public void saveEvent(Context context, String key, SurvMemberData data) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(key, json);

        editor.apply();
    }

    public SurvMemberData getValueEvent(Context context, String key) {
        SharedPreferences settings;
        String json;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        json = settings.getString(key, "");

        if (json.isEmpty()) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(json, SurvMemberData.class);
        }
    }
}