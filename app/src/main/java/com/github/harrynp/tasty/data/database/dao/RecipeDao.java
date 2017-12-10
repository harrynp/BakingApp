package com.github.harrynp.tasty.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.github.harrynp.tasty.data.database.RecipesContract;
import com.github.harrynp.tasty.data.pojo.Recipe;

import java.util.List;

/**
 * Created by harry on 12/3/2017.
 */

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM " + RecipesContract.RecipeEntry.TABLE_NAME)
    Cursor selectAllRecipes();

    @Query("SELECT * FROM " + RecipesContract.RecipeEntry.TABLE_NAME
            + " WHERE " + RecipesContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor selectRecipeById(long id);

    @Query("SELECT * FROM " + RecipesContract.RecipeEntry.TABLE_NAME
            + " WHERE " + RecipesContract.RecipeEntry.COLUMN_ID + " = :id")
    Recipe[] getRecipeById(long id);

    @Query("SELECT COUNT(*) FROM " + RecipesContract.RecipeEntry.TABLE_NAME)
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRecipes(Recipe... recipes);

    @Query("DELETE FROM " + RecipesContract.RecipeEntry.TABLE_NAME
            + " WHERE " + RecipesContract.RecipeEntry.COLUMN_ID + " = :id")
    int deleteRecipeById(long id);

    @Query("DELETE FROM " + RecipesContract.RecipeEntry.TABLE_NAME)
    void deleteAllRecipes();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateRecipe(Recipe recipe);
}
