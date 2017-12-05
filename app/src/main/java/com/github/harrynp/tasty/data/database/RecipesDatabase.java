package com.github.harrynp.tasty.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.github.harrynp.tasty.data.database.dao.RecipeDao;
import com.github.harrynp.tasty.data.pojo.Recipe;

/**
 * Created by harry on 12/2/2017.
 */

@Database(entities = {Recipe.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class RecipesDatabase extends RoomDatabase{
    private static RecipesDatabase INSTANCE;

    public abstract RecipeDao recipeDao();

    public static synchronized RecipesDatabase getRecipesDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecipesDatabase.class, "recipes")
                    .build();
        }
        return INSTANCE;
    }
}
