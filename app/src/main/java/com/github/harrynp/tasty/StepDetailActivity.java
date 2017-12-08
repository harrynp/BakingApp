package com.github.harrynp.tasty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.ActivityStepDetailBinding;

import org.parceler.Parcels;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    ActivityStepDetailBinding mBinding;
//    private ArrayList<Step> steps;
//    private int currentStepPosition;
    Step step;
    public static String STEP_EXTRA = "STEP_EXTRA";
//    public static String STEPS_EXTRA = "STEPS_EXTRA";
//    public static String CURRENT_STEP_POSITION = "CURRENT_STEP_POSITION";
    private StepDetailFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        setSupportActionBar(mBinding.stepDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        steps = new ArrayList<>();
        if (getIntent() != null) {
            if (getIntent().hasExtra(STEP_EXTRA)){
                step = Parcels.unwrap(getIntent().getParcelableExtra(STEP_EXTRA));
            }
//            if (getIntent().hasExtra(STEP_EXTRA) && getIntent().hasExtra(CURRENT_STEP_POSITION)){
//                for (Parcelable parcelable : getIntent().getParcelableArrayListExtra(STEPS_EXTRA)) {
//                    Step step = Parcels.unwrap(parcelable);
//                    steps.add(step);
//                }
//                currentStepPosition = getIntent().getIntExtra(CURRENT_STEP_POSITION, 0);
//            }
        }
        if (savedInstanceState != null) {
            fragment = (StepDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "stepDetailFragment");
        } else {
            fragment = StepDetailFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction().replace(R.id.step_fragment_container, fragment).commit();
//            fragment = StepDetailFragment.newInstance(steps.get(currentStepPosition));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "stepDetailFragment", fragment);
        }
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
