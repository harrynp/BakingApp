package com.github.harrynp.tasty.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.databinding.ListItemRecipeSpinnerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 12/8/2017.
 */

public class RecipeSpinnerAdapter extends RecyclerView.Adapter<RecipeSpinnerAdapter.RecipeSpinnerAdapterViewHolder> {
    private Context mContext;
    private final RecipeSpinnerAdapterOnClickHandler mClickHandler;
    private List<Recipe> mRecipeList;
    private ListItemRecipeSpinnerBinding mBinding;

    public void clear(){
        int size = mRecipeList.size();
        mRecipeList.clear();
        notifyItemRangeRemoved(0, size);
    }


    public void addRecipe(Recipe recipe){
        mRecipeList.add(recipe);
        notifyDataSetChanged();
    }


    public RecipeSpinnerAdapter(Context context, RecipeSpinnerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mRecipeList = new ArrayList<>();
    }

    @Override
    public RecipeSpinnerAdapter.RecipeSpinnerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_recipe_spinner, parent, false);
        return new RecipeSpinnerAdapterViewHolder(mBinding.getRoot());    }

    @Override
    public void onBindViewHolder(RecipeSpinnerAdapter.RecipeSpinnerAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        if (recipe != null){
            holder.recipeSpinnerChoiceView.setText(recipe.getName());
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) return 0;
        return mRecipeList.size();
    }

    public interface RecipeSpinnerAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public class RecipeSpinnerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeSpinnerChoiceView;

        public RecipeSpinnerAdapterViewHolder(View itemView) {
            super(itemView);
            recipeSpinnerChoiceView = mBinding.tvRecipeSpinnerChoice;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipeList.get(adapterPosition));
        }
    }
}
