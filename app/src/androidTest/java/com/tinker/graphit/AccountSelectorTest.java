package com.tinker.graphit;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;

import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;



/**
 * Created by sonny.kurniawan on 2016/05/04.
 */
public class AccountSelectorTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    UiDevice mDevice;

    @Before
    public void setUp() throws Exception {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    }

    @Test
    public void testInitAccountSelector() throws Exception {
        onView(withId(R.id.select_account_btn)).perform(click());
        onView(withText(R.string.main_dialog_title)).check(matches(isDisplayed()));
    }

    @Test
    public void testBuildAccountSelectorDialog() throws Exception {
        //onData(allOf(is(instanceOf(Dialog.class)), is("blitzkrieg.burner@gmail.com"))).perform(click());
        //getRootView(activityTestRule.getActivity(), "blitzkrieg.burner@gmail.com").perform(click());
        /*
        UiObject dialogList = mDevice
                .findObject(new UiSelector().textStartsWith("blitzkrieg"));
        dialogList.click();
        */

        //activityTestRule.getActivity().getWindowManager().
        //onView(withText(R.string.test_account)).inRoot(isDialog()).check(matches(isDisplayed()));
        //onData(allOf(is(instanceOf(String.class)), is("blitzkrieg.burner@gmail.com"))).inRoot(isDialog()).perform(click());
    }

    @Test
    public void testCheckTableParameter() throws Exception {
    }

    @Test
    public void testGetChartName() throws Exception {

    }

    @NonNull
    public static ViewInteraction getRootView(@NonNull Activity activity, @NonNull String text) {
        return onView(withText(text)).inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))));
    }


}