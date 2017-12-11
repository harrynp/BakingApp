package com.github.harrynp.tasty;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.harrynp.tasty.data.pojo.Step;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parceler.Parcels;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by harry on 12/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityTestRule = new ActivityTestRule<>(
            StepDetailActivity.class, true, false);

    Step step;

    @Before
    public void before(){
        step = new Step(0, "Recipe Introduction", "Recipe Introduction", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", "");
        Intent intent = new Intent();
        intent.putExtra(StepDetailFragment.STEP_EXTRA, Parcels.wrap(step));
        intent.putExtra(StepDetailFragment.PLAY_EXTRA, false);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void fragmentDisplayed() {
        onView(withId(R.id.step_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void exoPlayerDisplayed() {
        onView(withId(R.id.exo_player)).check(matches(isDisplayed()));
    }

    @Test
    public void stepDetailsTextViewShowsDescription(){
        onView(withId(R.id.tv_step_details)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_step_details)).check(matches(withText(step.getDescription())));
    }

    @After
    public void finish() {
        mActivityTestRule.finishActivity();
    }
}
