package com.github.harrynp.tasty;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.harrynp.tasty.databinding.FragmentDetailBinding;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    FragmentDetailBinding mBinding;
    private List<Parcelable> mIngredientsList;
    private List<Parcelable> mStepsList;
    private long recipeId;
    private static final String INGREDIENTS_STATE = "INGREDIENTS_STATE";
    private static final String STEPS_STATE = "STEPS_STATE";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredientsList Parameter 1.
     * @param stepsList Parameter 2.
     * @param recipeId Parameter 3
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(List<Parcelable> ingredientsList, List<Parcelable> stepsList, long recipeId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(IngredientsFragment.INGREDIENTS_EXTRA, (ArrayList<? extends Parcelable>) ingredientsList);
        args.putParcelableArrayList(StepsFragment.STEPS_EXTRA, (ArrayList<? extends Parcelable>) stepsList);
        args.putLong("RECIPE_ID", recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mBinding.detailToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = mBinding.detailViewpagerContainer;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = mBinding.tabs;

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(INGREDIENTS_STATE)){
                mIngredientsList = savedInstanceState.getParcelableArrayList(INGREDIENTS_STATE);
            }
            if (savedInstanceState.containsKey(STEPS_STATE)){
                mStepsList = savedInstanceState.getParcelableArrayList(STEPS_STATE);
            }
        } else {
            Bundle args = getArguments();
            if (args != null) {
                    recipeId = args.getLong("RECIPE_ID", -1);
                    if (args.containsKey(IngredientsFragment.INGREDIENTS_EXTRA)) {
                        mIngredientsList = args.getParcelableArrayList(IngredientsFragment.INGREDIENTS_EXTRA);
                    }
                    if (args.containsKey(StepsFragment.STEPS_EXTRA)) {
                        mStepsList = args.getParcelableArrayList(StepsFragment.STEPS_EXTRA);
                    }
                }
            }
        return mBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mIngredientsList != null){
            outState.putParcelableArrayList(INGREDIENTS_STATE, (ArrayList<? extends Parcelable>) mIngredientsList);
        }
        if (mStepsList != null){
            outState.putParcelableArrayList(STEPS_STATE, (ArrayList<? extends Parcelable>) mStepsList);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return IngredientsFragment.newInstance(mIngredientsList, recipeId);
            } else {
                return StepsFragment.newInstance(mStepsList, recipeId);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
