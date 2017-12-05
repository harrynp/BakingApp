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
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.databinding.FragmentIngredientBinding;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by harry on 12/4/2017.
 */

public class IngredientsFragment extends Fragment {

    private FragmentIngredientBinding mBinding;
    private IngredientsAdapter ingredientsAdapter;
    private ArrayList<Ingredient> ingredients;
    public static String INGREDIENTS_EXTRA = "INGREDIENTS_EXTRA";
    private static String SCROLLBAR_POSITION_KEY = "SCROLLBAR_POSITION_KEY";

    public IngredientsFragment(){}

    public static IngredientsFragment newInstance(ArrayList<Ingredient> ingredientArrayList) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        ArrayList<Parcelable> ingredientParcelableArrayList = new ArrayList<>();
        if (ingredientArrayList != null){
            for (Ingredient ingredient : ingredientArrayList){
                ingredientParcelableArrayList.add(Parcels.wrap(ingredient));
            }
        }
        args.putParcelableArrayList(INGREDIENTS_EXTRA, ingredientParcelableArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredient, container, false);
        ingredientsAdapter = new IngredientsAdapter(getContext());
        ingredients = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvIngredients.setLayoutManager(layoutManager);
        mBinding.rvIngredients.setAdapter(ingredientsAdapter);
        for (Parcelable parcelable : getArguments().getParcelableArrayList(INGREDIENTS_EXTRA)){
            Ingredient ingredient = Parcels.unwrap(parcelable);
            ingredients.add(ingredient);
            ingredientsAdapter.addIngredient(ingredient);
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
}
