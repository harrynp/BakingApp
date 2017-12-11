package com.github.harrynp.tasty;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.harrynp.tasty.IdlingResource.SimpleIdlingResource;
import com.github.harrynp.tasty.adapters.RecipeAdapter;
import com.github.harrynp.tasty.data.database.RecipeDatabaseHelper;
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.FragmentMainBinding;
import com.github.harrynp.tasty.network.RecipeApiClient;
import com.github.harrynp.tasty.network.RecipeApiListener;
import com.github.harrynp.tasty.utils.GridAutofitLayoutManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler,
        RecipeApiListener, Observer{

    private FragmentMainBinding mBinding;
    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> recipes;
    private RecipeDatabaseHelper recipeDatabaseHelper;
    private RecipeApiClient recipeApiClient;
    private static final String SAVED_RECIPES_KEY = "saved_recipes";
    private static final String SCROLLBAR_POSITION_KEY = "scrollbar_position";
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @NonNull
    public IdlingResource getIdlingResource(){
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        recipeAdapter = new RecipeAdapter(getContext(), this);
        recipeDatabaseHelper = new RecipeDatabaseHelper(getContext());
        recipeApiClient = new RecipeApiClient();
        recipes = new ArrayList<>();
        getIdlingResource();
        mBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                updateRecipes();
            }
        });
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(getContext(), 800);
        mBinding.rvRecipes.setLayoutManager(layoutManager);
        mBinding.rvRecipes.setAdapter(recipeAdapter);
        mBinding.rvRecipes.setHasFixedSize(false);
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SAVED_RECIPES_KEY)){
                ArrayList<Parcelable> recipeParcelableArrayList = savedInstanceState.getParcelableArrayList(SAVED_RECIPES_KEY);
                for (Parcelable parcelable : recipeParcelableArrayList){
                    Recipe recipe = Parcels.unwrap(parcelable);
                    recipes.add(recipe);
                    recipeAdapter.addRecipe(recipe);
                }
            }
            if (savedInstanceState.containsKey(SCROLLBAR_POSITION_KEY)){
                int position = savedInstanceState.getInt(SCROLLBAR_POSITION_KEY);
                mBinding.rvRecipes.getLayoutManager().scrollToPosition(position);
            }
        } else {
            mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
            recipeDatabaseHelper.getAllRecipes(this);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Parcelable> recipeParcelableArrayList = new ArrayList<>();
        if (recipes != null){
           for (Recipe recipe : recipes){
               recipeParcelableArrayList.add(Parcels.wrap(recipe));
           }
        }
        int position = ((GridLayoutManager) mBinding.rvRecipes.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putParcelableArrayList(SAVED_RECIPES_KEY, recipeParcelableArrayList);
        outState.putInt(SCROLLBAR_POSITION_KEY, position);
    }

    private void showGridView(){
        mBinding.tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mBinding.rvRecipes.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        mBinding.rvRecipes.setVisibility(View.INVISIBLE);
        mBinding.tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void updateRecipes(){
        if (mIdlingResource != null){
            mIdlingResource.setIdleState(false);
        }
        if (isOnline()) {
            mBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
            recipeApiClient.getRecipes(this);
        } else {
            mBinding.swiperefresh.setRefreshing(false);
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            showErrorMessage();
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        ArrayList<Parcelable> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()){
            ingredients.add(Parcels.wrap(ingredient));
        }
        ArrayList<Parcelable> steps = new ArrayList<>();
        for (Step step : recipe.getSteps()){
            steps.add(Parcels.wrap(step));
        }
        detailIntent.putExtra(IngredientsFragment.INGREDIENTS_EXTRA, ingredients);
        detailIntent.putExtra(StepsFragment.STEPS_EXTRA, steps);
        startActivity(detailIntent);
    }

    @Override
    public void onSuccess(ArrayList result) {
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.swiperefresh.setRefreshing(false);
        recipes.clear();
        recipeAdapter.clear();
        recipes.addAll(result);
        for (Recipe recipe : recipes){
            recipeAdapter.addRecipe(recipe);
        }
        recipeDatabaseHelper.insertRecipes(recipes, new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

        });
        showGridView();
    }

    @Override
    public void onFailure(Throwable throwable) {
        mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        mBinding.swiperefresh.setRefreshing(false);
        showErrorMessage();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {
        if (mIdlingResource != null){
            mIdlingResource.setIdleState(true);
        }
        ArrayList<Recipe> recipeArrayList = (ArrayList<Recipe>) o;
        if (recipeArrayList == null || recipeArrayList.size() == 0){
            recipeApiClient.getRecipes(this);
        } else {
            recipes.clear();
            recipeAdapter.clear();
            recipes.addAll(recipeArrayList);
            for (Recipe recipe : recipes) {
                recipeAdapter.addRecipe(recipe);
            }
            mBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
            mBinding.swiperefresh.setRefreshing(false);
            showGridView();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        recipeApiClient.getRecipes(this);
    }

    @Override
    public void onComplete() {

    }

}
