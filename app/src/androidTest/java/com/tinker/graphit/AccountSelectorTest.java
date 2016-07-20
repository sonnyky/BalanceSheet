package com.tinker.graphit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dalvik.annotation.TestTargetClass;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;

import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * Created by sonny.kurniawan on 2016/05/04.
 */
@RunWith(AndroidJUnit4.class)
public class AccountSelectorTest extends InstrumentationTestCase{
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.tinker.graphit";
    private static final int LAUNCH_TIMEOUT = 5000;
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    UiDevice mDevice;

    @Before
    public void setUp() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();

        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testDisplayChartInfoDialogFromFloatingButton() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        activityTestRule.getActivity();
        onView(withId(R.id.add_chart_button)).perform(click());
    }

    @Test
    public void testChooseUserAccount() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        activityTestRule.getActivity();
        onView(withId(R.id.add_chart_button)).perform(click());
        onView(withSpinnerText("sonny.kurniawan.yap@gmail.com")).perform(click());
    }

    @Test
    public void testCreateChartInfoAndShowGraph() throws Exception {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        activityTestRule.getActivity();
        onView(withId(R.id.add_chart_button)).perform(click());
        onView(withId(R.id.table_name_input_field)).perform(typeText("Ausgaben"));
        onView(withId(R.id.sheet_name_input_field))
                .perform(typeText("Finance sheet"));
        onView(withId(R.id.data_row_number_input_field))
                .perform(ViewActions.closeSoftKeyboard())
                .perform(typeText("2"));

        onView(withId(R.id.axis_col_number_input_field))
                .perform(ViewActions.closeSoftKeyboard())
                .perform(typeText("1"));

        onView(withId(R.id.data_col_number_input_field))
                .perform(ViewActions.closeSoftKeyboard())
                .perform(typeText("3"));

        onView(withId(R.id.show_chart_button))
                .perform(ViewActions.closeSoftKeyboard())
                .perform(click());
    }
    @Test
    public void createChartItemAndLaunchFromHome(){
        try {
            testCreateChartInfoAndShowGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mDevice.pressBack();
        onView(withText("Ausgaben")).perform(click());
    }




}