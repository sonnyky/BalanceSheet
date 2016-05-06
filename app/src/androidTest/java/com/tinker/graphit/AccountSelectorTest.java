package com.tinker.graphit;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
/**
 * Created by sonny.kurniawan on 2016/05/04.
 */
public class AccountSelectorTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInitAccountSelector() throws Exception {
        onView(withId(R.id.select_account_btn)).perform(click());
        onView(withText(R.string.main_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testBuildAccountSelectorDialog() throws Exception {
        onData(hasToString(startsWith("blitzkrieg.burner@gmail.com"))).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckTableParameter() throws Exception {

    }

    @Test
    public void testGetChartName() throws Exception {

    }
}