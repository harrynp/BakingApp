package com.github.harrynp.tasty;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
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


import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Step;
import com.github.harrynp.tasty.databinding.ActivityDetailBinding;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity implements DetailFragment.OnFragmentInteractionListener{
    ActivityDetailBinding mBinding;
    private List<Parcelable> ingredients;
    private List<Parcelable> steps;
    private long recipeId;

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
            recipeId = detailIntent.getLongExtra("RECIPE_ID", -1);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.ll_two_pane_layout) != null){
            mTwoPane = true;
            if (savedInstanceState == null){
                DetailFragment detailFragment = DetailFragment.newInstance(ingredients, steps, recipeId);
                fragmentManager.beginTransaction()
                        .add(R.id.two_pane_detail_container, detailFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                DetailFragment detailFragment = DetailFragment.newInstance(ingredients, steps, recipeId);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
