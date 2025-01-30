package recipebook.presentation;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static androidx.test.espresso.assertion.ViewAssertions.matches;


import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
public class DeleteShoppingRecipeTest {

    public static String RECIPE_NAME1 = "Minestrone soup";
    public static String RECIPE_NAME2 = "Basic Vanilla Cake";


    @Rule
    public ActivityScenarioRule<LogInActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LogInActivity.class);

    @Before
    public void setUp() throws IOException {
        // Login to the app
        LogInOut.login();

        // Move to Shopping List tab
        ViewInteraction navigationRailItemView = onView(
                allOf(withId(R.id.shopping_list_navigation_bar), withContentDescription("ShoppingList"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                3),
                        isDisplayed()));
        navigationRailItemView.perform(click());

        Sleep.sleep(5);
    }

    @Test
    public void deleteShoppingRecipeTest() {

        Sleep.sleep(5);

        //
        // Activate delete button
        //
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.edit_btn), withText("Edit"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton2.perform(click());

        // Check if delete button is activated
        onView(allOf(withId(R.id.shopping_list_card), CustomMatchers.withTextInChildView(RECIPE_NAME1, R.id.recipe_text)))
                .check(matches(hasDescendant(allOf(withId(R.id.delete_btn), isDisplayed()))));

        onView(allOf(withId(R.id.shopping_list_card), CustomMatchers.withTextInChildView(RECIPE_NAME2, R.id.recipe_text)))
                .check(matches(hasDescendant(allOf(withId(R.id.delete_btn), isDisplayed()))));

        Sleep.sleep(5);

        //
        // Delete RECIPE1 and RECIPE2 from shopping list
        //
        onView(withId(R.id.shopping_list_recycler))
                .perform(RecyclerViewActions.actionOnItem(
                        allOf(CustomMatchers.withTextInChildView(RECIPE_NAME1, R.id.recipe_text)),
                        CustomMatchers.clickChildViewWithId(R.id.delete_btn)));

        onView(withId(R.id.shopping_list_recycler))
                .perform(RecyclerViewActions.actionOnItem(
                        allOf(CustomMatchers.withTextInChildView(RECIPE_NAME2, R.id.recipe_text)),
                        CustomMatchers.clickChildViewWithId(R.id.delete_btn)));

        // Check if RECIPE1 and RECIPE2 are deleted
        onView(allOf(withId(R.id.shopping_list_card), CustomMatchers.withTextInChildView(RECIPE_NAME1, R.id.recipe_text)))
                .check(doesNotExist());

        onView(allOf(withId(R.id.shopping_list_card), CustomMatchers.withTextInChildView(RECIPE_NAME2, R.id.recipe_text)))
                .check(doesNotExist());

        Sleep.sleep(5);

        //
        // Save edited shopping list
        //
        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.edit_btn), withText("Save"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        Sleep.sleep(5);

        //Check if delete button is gone
        onView(withId(R.id.shopping_list_recycler)).check(matches(CustomMatchers.isButtonNotVisible(R.id.delete_btn)));
    }

    @After
    public void logOut(){
        LogInOut.logout();
    }
}
