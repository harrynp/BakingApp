package com.github.harrynp.tasty.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.adapters.RecipeSpinnerAdapter;
import com.github.harrynp.tasty.data.database.RecipeDatabaseHelper;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.databinding.RecipeIngredientsWidgetConfigureBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * The configuration screen for the {@link RecipeIngredientsWidget RecipeIngredientsWidget} AppWidget.
 */
public class RecipeIngredientsWidgetConfigureActivity extends Activity implements RecipeSpinnerAdapter.RecipeSpinnerAdapterOnClickHandler,
        Observer{

    private static final String PREFS_NAME = "com.github.harrynp.tasty.widget.RecipeIngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_RECIPE_NAME_KEY = "recipe_name_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecipeIngredientsWidgetConfigureBinding mBinding;
    RecipeDatabaseHelper recipeDatabaseHelper;
    private List<Recipe> recipeList;
    RecipeSpinnerAdapter recipeSpinnerAdapter;

    public RecipeIngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveIdPref(Context context, int appWidgetId, long recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + PREF_RECIPE_NAME_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static long loadIdPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Long idValue = prefs.getLong(PREF_PREFIX_KEY + appWidgetId, 0);
        return idValue;
    }

    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String recipeName = prefs.getString(PREF_PREFIX_KEY + PREF_RECIPE_NAME_KEY + appWidgetId, null);
        if (recipeName != null) {
            return recipeName;
        } else {
            return context.getString(R.string.recipe_widget_title);
        }
    }

    static void deleteIdPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + PREF_RECIPE_NAME_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.

        mBinding = DataBindingUtil.setContentView(this, R.layout.recipe_ingredients_widget_configure);

        recipeSpinnerAdapter = new RecipeSpinnerAdapter(this, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.rvRecipeChoices.setLayoutManager(layoutManager);
        mBinding.rvRecipeChoices.setAdapter(recipeSpinnerAdapter);
        recipeDatabaseHelper = new RecipeDatabaseHelper(this);
        recipeDatabaseHelper.getAllRecipes(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        final Context context = RecipeIngredientsWidgetConfigureActivity.this;

        // When the view is clicked, store the id locally
        saveIdPref(context, mAppWidgetId, recipe.getId());
        saveTitlePref(context, mAppWidgetId, recipe.getName());
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeIngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {
        recipeList = (ArrayList<Recipe>) o;
        if (recipeList == null || recipeList.size() == 0){
            finish();
            return;
        }
        recipeSpinnerAdapter.clear();
        for (Recipe recipe : recipeList){
            recipeSpinnerAdapter.addRecipe(recipe);
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

}

