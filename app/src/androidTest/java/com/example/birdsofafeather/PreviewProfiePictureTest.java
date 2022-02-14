package com.example.birdsofafeather;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PreviewProfiePictureTest {

    private final String URL = "https://lh3.googleusercontent.com/pw/AM-JKLUoIzSu5ChRS_n1CiHkFfqBSIcdO5N6Uaa_qpWQt78Wc6Lnxx7uA_nQe0bACjfM8rwYppv18Z1M2VHZVAVNjKO7glVFFUK4tJjR6m6mxoCax59AzErQ8xr-nFVENaD6PdGRvuQ-zHSStZg4fX0sLNSZ=w1003-h1337-no?authuser=0";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void previewProfiePictureTest() {
        ViewInteraction gy = onView(
                allOf(withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.sign_in_button),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        gy.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.name_comfirm_button), withText("Confirm")));

        synchronized (this) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        materialButton.perform(click());



        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.photo_url),
                        isDisplayed()));
        appCompatEditText.perform(longClick());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.photo_url),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("https://lh3.googleusercontent.com/pw/AM-JKLUoIzSu5ChRS_n1CiHkFfqBSIcdO5N6Uaa_qpWQt78Wc6Lnxx7uA_nQe0bACjfM8rwYppv18Z1M2VHZVAVNjKO7glVFFUK4tJjR6m6mxoCax59AzErQ8xr-nFVENaD6PdGRvuQ-zHSStZg4fX0sLNSZ=w1003-h1337-no?authuser=0"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.photo_url), withText("https://lh3.googleusercontent.com/pw/AM-JKLUoIzSu5ChRS_n1CiHkFfqBSIcdO5N6Uaa_qpWQt78Wc6Lnxx7uA_nQe0bACjfM8rwYppv18Z1M2VHZVAVNjKO7glVFFUK4tJjR6m6mxoCax59AzErQ8xr-nFVENaD6PdGRvuQ-zHSStZg4fX0sLNSZ=w1003-h1337-no?authuser=0"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.confirm_pfp), withText("Preview"),
                        isDisplayed()));

        synchronized (this) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        materialButton2.perform(click());


        onView(withId(R.id.photo_url)).check(matches(withText(URL)));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
