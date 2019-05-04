package de.clubber_stuttgart.clubber.business_logic;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.clubber_stuttgart.clubber.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTestWalkThrough {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest2() {
            //open datePicker Dialog by clicking on editText
            ViewInteraction editText = onView(
                    allOf(withId(R.id.datePicker), withText(is("Datum auswählen"))
                    ));
            editText.perform(click());

            //select a date - May 03, 2019
            onView(
                    withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2019, 12, 8));

            //click ok - set date
            ViewInteraction button = onView(
                    allOf(withId(android.R.id.button1), withText("OK")
                    ));
            button.perform(scrollTo(), click());

            //check if the selected date matches the one shown on the editText
            ViewInteraction editText2 = onView(
                    allOf(withId(R.id.datePicker), withText("Dec 08, 2019")
                    ));
            editText2.check(matches(withText("Dec 08, 2019")));

            //click on button to display filtered Events
            ViewInteraction button2 = onView(
                    allOf(withId(R.id.eventBtnWithDate)
                    ));
            button2.perform(click());

            //check if the Events are filtered correctly - filtered by selected date
            ViewInteraction textView = onView(
                    allOf(withId(R.id.date), withText(is("2019-12-08"))
                    ));
            textView.check(matches(withText("2019-12-08")));

            //click on the event tab in the navigation bar
            ViewInteraction bottomNavigationItemView = onView(
                    allOf(withId(R.id.nav_events), withContentDescription("Events")));
            bottomNavigationItemView.perform(click());

            //click on the club tab in the navigation bar
            ViewInteraction bottomNavigationItemView2 = onView(
                    allOf(withId(R.id.nav_location), withContentDescription("Clubs")));
            bottomNavigationItemView2.perform(click());

            //click on the event tab in the navigation bar and reload it by pulling it down
            ViewInteraction bottomNavigationItemView3 = onView(
                    allOf(withId(R.id.nav_events), withContentDescription("Events")));
            bottomNavigationItemView3.perform(click());

    }

}
