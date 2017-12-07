package com.github.harrynp.tasty;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.harrynp.tasty.databinding.ActivityStepDetailBinding;

public class StepDetailActivity extends AppCompatActivity {

    ActivityStepDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        setSupportActionBar(mBinding.stepDetailToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
