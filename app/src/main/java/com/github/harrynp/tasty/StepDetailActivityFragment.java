package com.github.harrynp.tasty;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.harrynp.tasty.databinding.FragmentStepDetailBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepDetailActivityFragment extends Fragment {
    FragmentStepDetailBinding mBinding;

    public StepDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        return mBinding.getRoot();
    }
}
