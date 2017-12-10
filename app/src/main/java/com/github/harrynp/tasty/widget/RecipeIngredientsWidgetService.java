package com.github.harrynp.tasty.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.harrynp.tasty.IngredientsFragment;
import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.StepsFragment;
import com.github.harrynp.tasty.data.database.RecipesDatabase;
import com.github.harrynp.tasty.data.database.dao.RecipeDao;
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.data.pojo.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by harry on 12/8/2017.
 */

public class RecipeIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeIngredientsRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class RecipeIngredientsRemoteViewsFactory implements  RemoteViewsService.RemoteViewsFactory{
    private Context mContext;
    private long recipeId;
    Recipe recipe;

    public RecipeIngredientsRemoteViewsFactory(Context context, Intent intent){
        mContext = context;
        recipeId = intent.getLongExtra("RECIPE_ID", -1);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if (recipeId != -1) {
            RecipeDao recipeDao = RecipesDatabase.getRecipesDatabase(mContext).recipeDao();
            Recipe[] recipes = recipeDao.getRecipeById(recipeId);
            if (recipes != null || recipes.length != 0){
                recipe = recipes[0];
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipe.getIngredients() == null) return 0;
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        if (recipe.getIngredients() == null || recipe.getIngredients().size() == 0) {
            return null;
        }
        Ingredient ingredient = recipe.getIngredients().get(i);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.list_item_ingredient_widget);
        views.setTextViewText(R.id.tv_ingredient_widget, ingredient.getIngredient());
        views.setTextViewText(R.id.tv_quantity_widget, ingredient.getQuantity() + " " + ingredient.getMeasure());
        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}