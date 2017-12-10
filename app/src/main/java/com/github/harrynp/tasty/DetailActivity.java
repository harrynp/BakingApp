package com.github.harrynp.tasty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.github.harrynp.tasty.data.database.RecipeDatabaseHelper;
import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.ActivityDetailBinding;

import org.parceler.Parcels;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding mBinding;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private long recipeId;
    private static final String INGREDIENTS_STATE = "INGREDIENTS_STATE";
    private static final String STEPS_STATE = "STEPS_STATE";
    RecipeDatabaseHelper recipeDatabaseHelper;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(mBinding.detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = mBinding.detailContainer;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = mBinding.tabs;

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        if (savedInstanceState != null){
            if (savedInstanceState.containsKey(INGREDIENTS_STATE)){
                ingredients = new ArrayList<>();
                ArrayList<Parcelable> parcelables = savedInstanceState.getParcelableArrayList(INGREDIENTS_STATE);
                for (Parcelable parcelable : parcelables) {
                    ingredients.add((Ingredient) Parcels.unwrap(parcelable));
                }
            }
            if (savedInstanceState.containsKey(STEPS_STATE)){
                steps = new ArrayList<>();
                ArrayList<Parcelable> parcelables = savedInstanceState.getParcelableArrayList(STEPS_STATE);
                for (Parcelable parcelable : parcelables) {
                    steps.add((Step) Parcels.unwrap(parcelable));
                }
            }
        } else {
            Intent detailIntent = getIntent();
            if (detailIntent != null) {
                if (detailIntent.hasExtra("RECIPE_ID")) {
                    recipeId = detailIntent.getLongExtra("RECIPE_ID", -1);
                } else {
                    if (detailIntent.hasExtra(IngredientsFragment.INGREDIENTS_EXTRA)) {
                        ingredients = new ArrayList<>();
                        ArrayList<Parcelable> parcelables = detailIntent.getParcelableArrayListExtra(IngredientsFragment.INGREDIENTS_EXTRA);
                        for (Parcelable parcelable : parcelables) {
                            ingredients.add((Ingredient) Parcels.unwrap(parcelable));
                        }
                    }
                    if (detailIntent.hasExtra(StepsFragment.STEPS_EXTRA)) {
                        steps = new ArrayList<>();
                        ArrayList<Parcelable> parcelables = detailIntent.getParcelableArrayListExtra(StepsFragment.STEPS_EXTRA);
                        for (Parcelable parcelable : parcelables) {
                            steps.add((Step) Parcels.unwrap(parcelable));
                        }
                    }
                    recipeId = -1;
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (ingredients != null){
            ArrayList<Parcelable> ingredientParcelableList = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingredientParcelableList.add(Parcels.wrap(ingredient));
            }
            outState.putParcelableArrayList(INGREDIENTS_STATE, ingredientParcelableList);
        }
        if (steps != null){
            ArrayList<Parcelable> stepParcelableList =new ArrayList<>();
            for (Step step : steps) {
                stepParcelableList.add(Parcels.wrap(step));
            }
            outState.putParcelableArrayList(STEPS_STATE, stepParcelableList);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                return IngredientsFragment.newInstance(ingredients, recipeId);
            } else {
                return StepsFragment.newInstance(steps, recipeId);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
