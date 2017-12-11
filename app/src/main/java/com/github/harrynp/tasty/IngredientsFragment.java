package com.github.harrynp.tasty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.harrynp.tasty.adapters.IngredientsAdapter;
import com.github.harrynp.tasty.data.database.RecipeDatabaseHelper;
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.databinding.FragmentIngredientBinding;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by harry on 12/4/2017.
 */

public class IngredientsFragment extends Fragment implements Observer{

    private FragmentIngredientBinding mBinding;
    private IngredientsAdapter ingredientsAdapter;
    public static String INGREDIENTS_EXTRA = "INGREDIENTS_EXTRA";
    private static String SCROLLBAR_POSITION_KEY = "SCROLLBAR_POSITION_KEY";

    public IngredientsFragment(){}

    public static IngredientsFragment newInstance(List<Parcelable> ingredientsList, long recipeId) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        if (ingredientsList != null) {
            args.putParcelableArrayList(INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) ingredientsList);
        }
        args.putLong("RECIPE_ID", recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient, container, false);
        ingredientsAdapter = new IngredientsAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvIngredients.setLayoutManager(layoutManager);
        mBinding.rvIngredients.setAdapter(ingredientsAdapter);
        Bundle args = getArguments();
        long recipeId = args.getLong("RECIPE_ID", -1);
        if (recipeId != -1) {
            RecipeDatabaseHelper recipeDatabaseHelper = new RecipeDatabaseHelper(getContext());
            recipeDatabaseHelper.getRecipe(recipeId, this);
        }
        if (args.containsKey(INGREDIENTS_EXTRA)) {
            for (Parcelable parcelable : args.getParcelableArrayList(INGREDIENTS_EXTRA)) {
                Ingredient ingredient = Parcels.unwrap(parcelable);
                ingredientsAdapter.addIngredient(ingredient);
            }
        }
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SCROLLBAR_POSITION_KEY)){
                mBinding.rvIngredients.smoothScrollToPosition(savedInstanceState.getInt(SCROLLBAR_POSITION_KEY));
            }
        }
        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = ((LinearLayoutManager) mBinding.rvIngredients.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(SCROLLBAR_POSITION_KEY, position);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {
        Recipe recipe = (Recipe) o;
        if (recipe != null){
            ArrayList<Ingredient> ingredients = recipe.getIngredients();
            for (Ingredient ingredient : ingredients) {
                ingredientsAdapter.addIngredient(ingredient);
            }
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
