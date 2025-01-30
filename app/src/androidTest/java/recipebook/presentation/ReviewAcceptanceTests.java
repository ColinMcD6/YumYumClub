package recipebook.presentation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import comp3350.yumyumclub.R;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReviewAcceptanceTests {

    public static int FIRST_RATING = 3;
    public static String FIRST_COMMENT = "Yummy";
    public static int SECOND_RATING = 4;
    public static String SECOND_COMMENT = "Exquisite";


    @Rule
    public ActivityScenarioRule<LogInActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Before
    public void setUp() throws IOException {
        // Login to the app
        LogInOut.login();

        // Click on the Basic Vanilla Cake recipe in the Baking category
        ViewInteraction recyclerView = onView(withContentDescription("Basic Vanilla CakeBaking"));
        recyclerView.perform(click());

        // Go to comments section
        onView(withId(R.id.comments_button))
                .perform(click());
    }

    @Test
    public void reviewRatingTest() {
        Sleep.sleep(5); // Give recycler view time to load

        //
        // Ensure that recycler view has loaded
        //
        onView(withId(R.id.review_layout))
                .check(matches(isDisplayed()));;

        //
        // Check to make sure the average rating and number of reviews doesn't show when there are no reviews
        //
        onView(withId(R.id.summary))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));


        //
        // Check to make sure that an empty review cannot be submitted
        //
        onView(withId(R.id.add_comment))
                .perform(click());

        // Check to make sure that an empty review cannot be submitted
        onView(withId(R.id.create_review_btn))
                .check(matches(not(isEnabled())));
        onView(withId(R.id.edit_review))
                .perform(replaceText(FIRST_COMMENT), closeSoftKeyboard());

        // Now that text has been entered the submit button should be enabled
        onView(withId(R.id.create_review_btn))
                .check(matches(isEnabled()));

        //
        // Enter a review and rating
        //
        onView(withId(R.id.review_rating))
                .perform(new SetStars(FIRST_RATING));

        onView(withId(R.id.create_review_btn))
                .perform(click());

        Sleep.sleep(5); // Give recycler view time to load

        //
        // Check input shows correctly on screen
        //

        // Check that username on review is correct
        String uniqueContentDescription = String.format("%s%s%d", LogInOut.USERNAME, FIRST_COMMENT, FIRST_RATING);
        onView(withContentDescription(String.format("USERNAME:%s", uniqueContentDescription)))
                .check(matches(withText(LogInOut.USERNAME)));

        // Check that text in review is correct
        onView(withContentDescription(String.format("COMMENT:%s", uniqueContentDescription)))
                .check(matches(withText(FIRST_COMMENT)));

        // Check that rating on review is correct
        onView(withContentDescription(String.format("RATING:%s", uniqueContentDescription)))
                .check(matches(CustomMatchers.withRating(FIRST_RATING)));

        // Check if Review average is correct
        onView(withId(R.id.avg_stars))
                .check(matches(CustomMatchers.withRating(3f)));

        onView(withId(R.id.avg_rating))
                .check(matches(withText("3.0 (1)")));

        //
        // Add second review
        //
        onView(withId(R.id.add_comment))
                .perform(click());

        onView(withId(R.id.edit_review))
                .perform(replaceText(SECOND_COMMENT), closeSoftKeyboard());

        onView(withId(R.id.review_rating))
                .perform(new SetStars(SECOND_RATING));

        onView(withId(R.id.create_review_btn))
                .perform(click());

        //
        // Check if review average updated correctly
        //
        onView(withId(R.id.avg_stars))
                .check(matches(CustomMatchers.withRating(3.5f)));

        onView(withId(R.id.avg_rating))
                .check(matches(withText("3.5 (2)")));
    }

    @After
    public void logOut(){

        // Go back to main page
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.back_to_recipe_button), withText("Back to recipe"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        materialButton3.perform(scrollTo(), click());

        Sleep.sleep(5);

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.button), withText("Back"),
                        CustomMatchers.childAtPosition(
                                allOf(withId(R.id.main),
                                        CustomMatchers.childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        Sleep.sleep(5);

        // Log out
        LogInOut.logout();
    }

}
