package com.github.harrynp.tasty;

import android.content.Intent;
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

import com.github.harrynp.tasty.adapters.StepsAdapter;
import com.github.harrynp.tasty.data.database.RecipeDatabaseHelper;
import com.github.harrynp.tasty.data.pojo.Recipe;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.FragmentStepBinding;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by harry on 12/4/2017.
 */

public class StepsFragment extends Fragment implements StepsAdapter.StepsAdapterOnClickHandler, Observer{
    private StepsAdapter stepsAdapter;
    private FragmentStepBinding mBinding;
//    private ArrayList<Step> steps;
    public static String STEPS_EXTRA = "STEPS_EXTRA";
    private static String SCROLLBAR_POSITION_KEY = "SCROLLBAR_POSITION_KEY";

    public StepsFragment(){}

    public static StepsFragment newInstance(List<Parcelable> stepsList, long recipeId) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS_EXTRA, (ArrayList<? extends Parcelable>) stepsList);
        args.putLong("RECIPE_ID", recipeId);
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
        Bundle args = getArguments();
        long recipeId = args.getLong("RECIPE_ID", -1);
        if (recipeId != -1) {
            RecipeDatabaseHelper recipeDatabaseHelper = new RecipeDatabaseHelper(getContext());
            recipeDatabaseHelper.getRecipe(recipeId, this);
        }
        if(args.containsKey(STEPS_EXTRA)) {
            for (Parcelable parcelable : args.getParcelableArrayList(STEPS_EXTRA)) {
                Step step = Parcels.unwrap(parcelable);
                //            steps.add(step);
                stepsAdapter.addStep(step);
            }
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

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {
        Recipe recipe = (Recipe) o;
        if (recipe != null){
            ArrayList<Step> steps = recipe.getSteps();
            for (Step ingredient : steps) {
                stepsAdapter.addStep(ingredient);
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
