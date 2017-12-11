package com.github.harrynp.tasty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.ActivityDetailBinding;

import org.parceler.Parcels;

import java.util.List;


public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener, StepsFragment.OnStepClickListener{
    ActivityDetailBinding mBinding;
    private List<Parcelable> ingredients;
    private List<Parcelable> steps;
    private long recipeId;
    private String recipeName;

    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent detailIntent = getIntent();

        if (detailIntent != null){
            if (detailIntent.hasExtra(IngredientsFragment.INGREDIENTS_EXTRA)){
                ingredients = detailIntent.getParcelableArrayListExtra(IngredientsFragment.INGREDIENTS_EXTRA);
            }
            if (detailIntent.hasExtra(StepsFragment.STEPS_EXTRA)){
                steps = detailIntent.getParcelableArrayListExtra(StepsFragment.STEPS_EXTRA);
            }
            if (detailIntent.hasExtra("RECIPE_NAME")){
                recipeName = detailIntent.getStringExtra("RECIPE_NAME");
            }
            recipeId = detailIntent.getLongExtra("RECIPE_ID", -1);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.ll_two_pane_layout) != null){
            mTwoPane = true;
            if (savedInstanceState == null){
                DetailFragment detailFragment = DetailFragment.newInstance(ingredients, steps, recipeId, recipeName);
                fragmentManager.beginTransaction()
                        .add(R.id.two_pane_detail_container, detailFragment)
                        .commit();
                if (steps != null || steps.isEmpty()) {
                    StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance((Step) Parcels.unwrap(steps.get(0)), false);
                    fragmentManager.beginTransaction()
                            .add(R.id.two_pane_step_detail_container, stepDetailFragment)
                            .commit();
                }
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                DetailFragment detailFragment = DetailFragment.newInstance(ingredients, steps, recipeId, recipeName);
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, detailFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void OnStepClickListener(Step step, int adapterPosition) {
        if (mTwoPane){
            StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(step, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.two_pane_step_detail_container, stepDetailFragment)
                    .commit();
        } else {
            Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
            stepDetailIntent.putExtra(StepDetailFragment.STEP_EXTRA, Parcels.wrap(step));
            step.setStepViewed(true);
            startActivity(stepDetailIntent);
        }
    }
}
