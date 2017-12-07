package com.github.harrynp.tasty.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.harrynp.tasty.R;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.ListItemStepBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 12/4/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsAdapterViewHolder> {
    private final Context mContext;
    private final StepsAdapterOnClickHandler mClickHandler;
    private ListItemStepBinding mBinding;
    private List<Step> mStepList;

    public void clear(){
        int size = mStepList.size();
        mStepList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addStep(Step step){
        mStepList.add(step);
        notifyDataSetChanged();
    }

    public void setViewed(int adpaterPosition){
        notifyItemChanged(adpaterPosition);
    }

    public StepsAdapter(Context context, StepsAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
        mStepList = new ArrayList<>();
    }

    @Override
    public StepsAdapter.StepsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.list_item_step , parent, false);
        return new StepsAdapterViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsAdapterViewHolder holder, int position) {
        Step step = mStepList.get(position);
        if (step != null){
            if (position == mStepList.size() -1){
                holder.bottomConnector.setVisibility(View.GONE);
            }
            if (!step.getStepViewed()){
                holder.stepDot.setImageResource(R.drawable.ic_hollow_circle);
            } else {
                holder.stepDot.setImageResource(R.drawable.ic_filled_circle);
            }
            holder.stepView.setText(step.getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) return 0;
        return mStepList.size();
    }

    public interface StepsAdapterOnClickHandler {
        void onClick(Step step, int adapterPosition);
    }


    public class StepsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView stepDot;
        TextView stepView;
        View bottomConnector;


        public StepsAdapterViewHolder(View itemView) {
            super(itemView);
            stepDot = mBinding.stepDot;
            stepView = mBinding.tvStep;
            bottomConnector = mBinding.bottomConnector;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mStepList.get(adapterPosition), adapterPosition);
        }
    }
}
