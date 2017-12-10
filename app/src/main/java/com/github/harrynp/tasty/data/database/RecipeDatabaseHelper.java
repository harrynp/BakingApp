package com.github.harrynp.tasty.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.github.harrynp.tasty.data.pojo.Recipe;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by harry on 12/4/2017.
 */

public final class RecipeDatabaseHelper {
    private Context mContext;
    private static Converters converters = new Converters();

    public RecipeDatabaseHelper(Context context){
        mContext = context;
    }

    public void getRecipe(final long id, Observer<Recipe> observer){
        Observable.create(new ObservableOnSubscribe<Recipe>() {
            @Override
            public void subscribe(ObservableEmitter<Recipe> e) throws Exception {
                Uri contentUriWithId = RecipesContract.RecipeEntry.CONTENT_URI;
                contentUriWithId = contentUriWithId.buildUpon().appendPath(Long.toString(id)).build();
                Cursor cursor = mContext.getContentResolver()
                        .query(contentUriWithId,
                                null,
                                null,
                                null,
                                RecipesContract.RecipeEntry.COLUMN_NAME + " DESC");
                Recipe recipe;
                if(cursor != null){
                    cursor.moveToNext();
                    recipe = buildRecipeFromCursor(cursor);
                    cursor.close();
                    e.onNext(recipe);
                } else {
                    e.onError(new NullPointerException("Recipe cursor is null."));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getAllRecipes(Observer<ArrayList<Recipe>> observer){
        Observable.create(new ObservableOnSubscribe<ArrayList<Recipe>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<Recipe>> e) throws Exception {
                Cursor cursor = mContext.getContentResolver()
                        .query(RecipesContract.RecipeEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                RecipesContract.RecipeEntry.COLUMN_NAME + " DESC");
                ArrayList<Recipe> recipes;
                if(cursor != null){
                    recipes = buildRecipesFromCursor(cursor);
                    cursor.close();
                    e.onNext(recipes);
                } else {
                    e.onError(new NullPointerException("Recipe cursor is null."));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void insertRecipes(@NonNull final ArrayList<Recipe> recipes, Observer<Integer> observer){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                ContentValues[] contentValues = new ContentValues[recipes.size()];
                for (int i = 0; i < recipes.size(); ++i) {
                    contentValues[i] = buildContentValuesFromRecipe(recipes.get(i));
                }
                int recipesAdded = mContext.getContentResolver().bulkInsert(RecipesContract.RecipeEntry.CONTENT_URI, contentValues);
                if (recipesAdded != 0){
                    e.onNext(recipesAdded);
                } else {
                    e.onError(new NullPointerException("Failed to insert"));
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @NonNull
    private ArrayList<Recipe> buildRecipesFromCursor(Cursor cursor){
        ArrayList<Recipe> recipes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Recipe recipe = buildRecipeFromCursor(cursor);
            recipes.add(recipe);
        }
        return recipes;
    }

    @NonNull
    private Recipe buildRecipeFromCursor(Cursor cursor){
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_ID)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_NAME)));
        recipe.setIngredients(converters.fromIngredientString(cursor.getString(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_INGREDIENTS))));
        recipe.setSteps(converters.fromStepString(cursor.getString(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_STEPS))));
        recipe.setServings(cursor.getInt(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_SERVINGS)));
        recipe.setImage(cursor.getString(cursor.getColumnIndex(RecipesContract.RecipeEntry.COLUMN_IMAGE)));
        return recipe;
    }

    @NonNull
    private ContentValues buildContentValuesFromRecipe(Recipe recipe){
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_ID, recipe.getId());
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_NAME, recipe.getName());
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_INGREDIENTS, converters.fromIngredientArrayList(recipe.getIngredients()));
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_STEPS, converters.fromStepArrayList(recipe.getSteps()));
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        contentValues.put(RecipesContract.RecipeEntry.COLUMN_IMAGE, recipe.getImage());
        return contentValues;
    }
}
