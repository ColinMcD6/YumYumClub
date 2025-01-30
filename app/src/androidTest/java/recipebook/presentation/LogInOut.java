package recipebook.presentation;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.ViewInteraction;

import comp3350.yumyumclub.R;

public class LogInOut {
    public static String USERNAME = "Seyi";
    public static String PASSWORD = "123";

    public static void login()
    {
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.account_box),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withId(R.id.account_box_layout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText(USERNAME), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.password_box),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withId(R.id.password_box_layout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText(PASSWORD), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.logIn_bt), withText("Log In"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withId(R.id.main),
                                        3),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        // Wait to browse page to load
        Sleep.sleep(5);
    }

    public static void logout()
    {
        ViewInteraction navigationRailItemView2 = onView(
                allOf(withId(R.id.account_navigation_bar), withContentDescription("Account"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                4),
                        isDisplayed()));
        navigationRailItemView2.perform(click());

        Sleep.sleep(5);

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.logOut_bt), withText("Log out"),
                        CustomMatchers.childAtPosition(
                                CustomMatchers.childAtPosition(
                                        withClassName(is("android.widget.TableRow")),
                                        2),
                                0),
                        isDisplayed()));
        materialButton6.perform(click());
    }
}
