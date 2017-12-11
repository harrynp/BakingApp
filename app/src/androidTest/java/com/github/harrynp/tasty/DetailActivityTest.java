package com.github.harrynp.tasty;

import android.content.Intent;
import android.os.Parcelable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.harrynp.tasty.data.pojo.Ingredient;
import com.github.harrynp.tasty.data.pojo.Step;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by harry on 12/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(
            DetailActivity.class, true, false);
    ArrayList<Parcelable> ingredientArrayList;
    Ingredient ingredient;
    ArrayList<Parcelable> stepArrayList;
    Step step;

    @Before
    public void before(){
        ingredientArrayList = new ArrayList<>();
        stepArrayList = new ArrayList<>();
        ingredient = new Ingredient(2f, "CUP", "Graham Cracker crumbs");
        ingredientArrayList.add(Parcels.wrap(ingredient));
        step = new Step(0, "Recipe Introduction", "Recipe Introduction", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", "");
        stepArrayList.add(Parcels.wrap(step));
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(IngredientsFragment.INGREDIENTS_EXTRA, ingredientArrayList);
        intent.putParcelableArrayListExtra(StepsFragment.STEPS_EXTRA, stepArrayList);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void detailActivityTabsDisplayedTest() {
        onView(withId(R.id.tabs)).check(matches(isDisplayed()));
    }

    @Test
    public void detailActivityIngredientFragmentShownFirstTest(){
        onView(withId(R.id.fl_ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_ingredients)).check(matches(isDisplayed()));
    }

    @Test
    public void stepFragmentShown(){
        onView(withId(R.id.detail_viewpager_container)).perform(swipeLeft());
        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @After
    public void finish() {
        mActivityTestRule.finishActivity();
    }
}
