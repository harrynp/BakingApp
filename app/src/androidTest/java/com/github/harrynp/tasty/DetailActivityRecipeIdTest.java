package com.github.harrynp.tasty;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.harrynp.tasty.widget.RecipeIngredientsWidgetConfigureActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;


/**
 * Created by harry on 12/9/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityRecipeIdTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(
            DetailActivity.class, true, false);


    @Before
    public void before(){
        Intent intent = new Intent();
        intent.putExtra("RECIPE_ID", 0);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void detailActivityWithRecipeIdTest() {
        onView(withId(R.id.tabs)).check(matches(isDisplayed()));
        onView(withId(R.id.detail_container)).check(matches(isDisplayed()));
    }

    @After
    public void finish() {
        mActivityTestRule.finishActivity();
    }

}
