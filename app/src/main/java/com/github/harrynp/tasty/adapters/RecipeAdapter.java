package com.github.harrynp.tasty.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.GridItemRecipeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 12/2/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final Context mContext;
    private final RecipeAdapterOnClickHandler mClickHandler;
    private GridItemRecipeBinding mBinding;
    private List<Recipe> mRecipeList;

    public void clear(){
        int size = mRecipeList.size();
        mRecipeList.clear();
        notifyItemRangeRemoved(0, size);
    }


    public void addRecipe(Recipe recipe){
        mRecipeList.add(recipe);
        notifyDataSetChanged();
    }

    public RecipeAdapter(Context context, RecipeAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mRecipeList = new ArrayList<>();
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.grid_item_recipe, parent, false);
        return new RecipeAdapterViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        if (recipe != null){
            ArrayList<Step> steps = recipe.getSteps();
            holder.titleView.setText(recipe.getName());
            holder.servingsView.setText(Integer.toString(recipe.getServings()));
            Glide.with(mContext)
                    .load(steps.get(steps.size() -1).getVideoURL())
                    .apply(new RequestOptions()
                    .fallback(R.drawable.image_not_found))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.thumbnailView);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) return 0;
        return mRecipeList.size();
    }

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView thumbnailView;
        final TextView titleView;
        final TextView servingsView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            thumbnailView = mBinding.ivRecipeThumbnail;
            titleView = mBinding.tvTitle;
            servingsView = mBinding.tvServing;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mRecipeList.get(adapterPosition));
        }
    }
}
