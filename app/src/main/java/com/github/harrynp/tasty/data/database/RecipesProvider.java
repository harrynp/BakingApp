package com.github.harrynp.tasty.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.harrynp.tasty.data.database.dao.RecipeDao;
import com.github.harrynp.tasty.data.pojo.Recipe;

import java.util.List;

/**
 * Created by harry on 12/2/2017.
 */

public class RecipesProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static final int RECIPES = 100;
    public static final int RECIPES_WITH_ID = 101;
    private static Converters converters = new Converters();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(RecipesContract.CONTENT_AUTHORITY, RecipesContract.PATH_RECIPES + "/#", RECIPES_WITH_ID);
        return uriMatcher;
    }

    private RecipeDao recipeDao;
    private Context mContext;

    private static Recipe buildRecipeFromContentValues(ContentValues contentValues){
        Recipe recipe = new Recipe();
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_ID)){
            recipe.setId(contentValues.getAsLong(RecipesContract.RecipeEntry.COLUMN_ID));
        }
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_NAME)){
            recipe.setName(contentValues.getAsString(RecipesContract.RecipeEntry.COLUMN_NAME));
        }
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_INGREDIENTS)){
            recipe.setIngredients(converters.fromIngredientString(contentValues.getAsString(RecipesContract.RecipeEntry.COLUMN_INGREDIENTS)));
        }
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_STEPS)){
            recipe.setSteps(converters.fromStepString(contentValues.getAsString(RecipesContract.RecipeEntry.COLUMN_STEPS)));
        }
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_SERVINGS)){
            recipe.setServings(contentValues.getAsInteger(RecipesContract.RecipeEntry.COLUMN_SERVINGS));
        }
        if (contentValues.containsKey(RecipesContract.RecipeEntry.COLUMN_IMAGE)){
            recipe.setImage(contentValues.getAsString(RecipesContract.RecipeEntry.COLUMN_IMAGE));
        }
        return recipe;
    }


    @Override
    public boolean onCreate() {
        mContext = getContext();
        if (mContext == null){
            return false;
        }
        recipeDao = RecipesDatabase.getRecipesDatabase(mContext).recipeDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case RECIPES:
                retCursor = recipeDao.selectAllRecipes();
                break;
            case RECIPES_WITH_ID:
                retCursor = recipeDao.selectRecipeById(ContentUris.parseId(uri));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(mContext.getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                return RecipesContract.RecipeEntry.CONTENT_TYPE;
            case RECIPES_WITH_ID:
                return RecipesContract.RecipeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case RECIPES:
                Recipe recipe = buildRecipeFromContentValues(contentValues);
                long recipeId = recipeDao.insertRecipe(recipe);
                if (recipeId > 0){
                    returnUri = ContentUris.withAppendedId(RecipesContract.RecipeEntry.CONTENT_URI, recipeId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int match = sUriMatcher.match(uri);
        List<Long> recipeIds;
        switch (match) {
            case RECIPES:
                Recipe [] recipes = new Recipe[values.length];
                for (int i = 0; i < values.length; ++i) {
                    recipes[i] = buildRecipeFromContentValues(values[i]);
                }
                recipeIds = recipeDao.insertRecipes(recipes);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return recipeIds.size();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        int recipesDeleted;
        switch (match) {
            case RECIPES:
                recipesDeleted = recipeDao.count();
                recipeDao.deleteAllRecipes();
                if (recipeDao.count() != 0){
                    throw new SQLException("Failed to delete recipes " + uri);
                }
                break;
            case RECIPES_WITH_ID:
                long id = ContentUris.parseId(uri);
                recipesDeleted = recipeDao.deleteRecipeById(id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (recipesDeleted != 0){
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return recipesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case RECIPES:
                Recipe recipe = buildRecipeFromContentValues(contentValues);
                rowsUpdated = recipeDao.updateRecipe(recipe);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0){
            mContext.getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
