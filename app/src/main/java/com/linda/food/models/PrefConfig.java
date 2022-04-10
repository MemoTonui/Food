package com.linda.food.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefConfig {
    private static final String LIST_KEY = "list_key";
    public static void writeListInPref(Context context, List<Food> foods){
        Gson gson = new Gson();
        String jsonString = gson.toJson(foods);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static  void deleteInPref(Context context, Food food){
        Gson gson = new Gson();
        String jsonString = gson.toJson(food);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(LIST_KEY);
        editor.commit();

    }
    public static  void ClearInPref(Context context, List<Food> food){
        Gson gson = new Gson();
        String jsonString = gson.toJson(food);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(LIST_KEY);
        editor.commit();

    }


    public  static List<Food> readListFromPref(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = sharedPreferences.getString(LIST_KEY,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Food>>() {}.getType();

        List <Food> foods = gson.fromJson(jsonString, type);
        return foods;
    }
}
