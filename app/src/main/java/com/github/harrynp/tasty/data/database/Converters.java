package com.github.harrynp.tasty.data.database;

import android.arch.persistence.room.TypeConverter;

import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by harry on 12/3/2017.
 */

public class Converters {
    @TypeConverter
    public static ArrayList<Ingredient> fromIngredientString(String value) {
        Type listType = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromIngredientArrayList(ArrayList<Ingredient> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Step> fromStepString(String value) {
        Type listType = new TypeToken<ArrayList<Step>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String fromStepArrayList(ArrayList<Step> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}