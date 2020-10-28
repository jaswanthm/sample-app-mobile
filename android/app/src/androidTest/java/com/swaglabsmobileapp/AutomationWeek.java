package com.swaglabsmobileapp;


import android.util.Log;
import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.wix.detox.espresso.DetoxViewActions.click;
import static com.wix.detox.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AutomationWeek {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    public static ViewAction clickXY(final int x, final int y) {
        return new GeneralClickAction(
                Tap.LONG,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        Log.e("Coordinates:", String.valueOf(screenX) + "," + String.valueOf(screenY));

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    public static ViewAction dragAndDrop() {
        return new GeneralSwipeAction(
                Swipe.SLOW,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + 10;
                        final float screenY = screenPos[1] + 10;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0];
                        final float screenY = screenPos[1];
                        float[] coordinates = {0 + 10, 356 - 10};

                        return coordinates;
                    }
                },
                Press.PINPOINT
        );
    }

    @Test
    public void testAdvancedMobileChallenge() {
        try {
            Thread.sleep(3000);

            onView(withContentDescription("test-Login")).perform(swipeUp());

            Thread.sleep(1000);

            onView(withText("standard_user")).perform(click());
            onView(withText("LOGIN")).perform(click());


            onView(withContentDescription("test-Cart drop zone")).perform(clickXY(0, 0));

            Thread.sleep(1000);

            onView(withIndex(withContentDescription("test-Drag Handle"), 0)).perform(dragAndDrop());

            Thread.sleep(1000);

            onView(withIndex(withContentDescription("test-Drag Handle"), 0)).perform(dragAndDrop());

            Thread.sleep(1000);

            onView(withContentDescription("test-PRODUCTS")).perform(swipeUp());

            onView(withIndex(withContentDescription("test-Drag Handle"), 0)).perform(dragAndDrop());

            Thread.sleep(1000);

            onView(withIndex(withContentDescription("test-Drag Handle"), 0)).perform(dragAndDrop());

            Thread.sleep(1000);

            onView(withContentDescription("test-Cart")).perform(click());
            onView(withContentDescription("test-Cart Content")).perform(swipeUp());
            onView(withText("CHECKOUT")).perform(click());

            Thread.sleep(1000);

            onView(withContentDescription("test-First Name")).perform(typeText("Jaswanth"));
            onView(withContentDescription("test-Last Name")).perform(typeText("Manigundan"));
            onView(withContentDescription("test-Zip/Postal Code")).perform(typeText("3032"));
            onView(withContentDescription("test-Zip/Postal Code")).perform(closeSoftKeyboard());

            onView(withText("CONTINUE")).perform(click());

            onView(withContentDescription("test-CHECKOUT: OVERVIEW")).perform(swipeUp());
            onView(withText("FINISH")).perform(click());

            onView(withText("BACK HOME")).check(matches(isDisplayed()));

            Thread.sleep(2000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}