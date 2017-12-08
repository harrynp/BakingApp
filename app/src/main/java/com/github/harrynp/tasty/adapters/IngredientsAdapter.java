package com.github.harrynp.tasty.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.databinding.ListItemIngredientBinding;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 12/4/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private Context mContext;
    private List<Ingredient> mIngredientList;
    private ListItemIngredientBinding mBinding;

    public void clear(){
        int size = mIngredientList.size();
        mIngredientList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addIngredient(Ingredient ingredient){
        mIngredientList.add(ingredient);
        notifyDataSetChanged();
    }

    public IngredientsAdapter(Context context){
        mContext = context;
        mIngredientList = new ArrayList<>();
    }

    @Override
    public IngredientsAdapter.IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_ingredient, parent, false);
        return new IngredientsAdapterViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsAdapterViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);
        if (ingredient != null){
            if (position % 2 != 0) {
                holder.ingredientLayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            }
        }
        String name = ingredient.getIngredient();
        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        holder.ingredientView.setText(capitalizedName);
        holder.quantityView.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder{
        final ConstraintLayout ingredientLayout;
        final TextView ingredientView;
        final TextView quantityView;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);
            ingredientLayout = mBinding.clIngredient;
            ingredientView = mBinding.tvIngredient;
            quantityView = mBinding.tvQuantity;
        }
    }
}
