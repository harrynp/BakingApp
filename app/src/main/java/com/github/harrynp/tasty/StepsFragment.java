package com.github.harrynp.tasty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.harrynp.tasty.adapters.StepsAdapter;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.FragmentStepBinding;

import org.parceler.Parcels;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by harry on 12/4/2017.
 */

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler{
    private StepsAdapter stepsAdapter;
    private FragmentStepBinding mBinding;
//    private ArrayList<Step> steps;
    public static String STEPS_EXTRA = "STEPS_EXTRA";
    private static String SCROLLBAR_POSITION_KEY = "SCROLLBAR_POSITION_KEY";

    public StepsFragment(){}

    public static StepsFragment newInstance(ArrayList<Step> stepArrayList) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        ArrayList<Parcelable> stepParcelableArrayList = new ArrayList<>();
        if (stepArrayList != null){
            for (Step step : stepArrayList){
                stepParcelableArrayList.add(Parcels.wrap(step));
            }
        }
        args.putParcelableArrayList(STEPS_EXTRA, stepParcelableArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);
        stepsAdapter = new StepsAdapter(getContext(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBinding.rvSteps.setLayoutManager(layoutManager);
        mBinding.rvSteps.setAdapter(stepsAdapter);
//        steps = new ArrayList<>();
        for (Parcelable parcelable : getArguments().getParcelableArrayList(STEPS_EXTRA)) {
            Step step = Parcels.unwrap(parcelable);
//            steps.add(step);
            stepsAdapter.addStep(step);
        }
        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(SCROLLBAR_POSITION_KEY)){
                mBinding.rvSteps.scrollToPosition(savedInstanceState.getInt(SCROLLBAR_POSITION_KEY));
            }
        }
        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int position = ((LinearLayoutManager) mBinding.rvSteps.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(SCROLLBAR_POSITION_KEY, position);
    }

    @Override
    public void onClick(Step step, int adapterPosition) {
        Intent stepDetailIntent = new Intent(getActivity(), StepDetailActivity.class);
        stepDetailIntent.putExtra(StepDetailFragment.STEP_EXTRA, Parcels.wrap(step));
        step.setStepViewed(true);
        stepsAdapter.setViewed(adapterPosition);
        startActivity(stepDetailIntent);
    }
}
