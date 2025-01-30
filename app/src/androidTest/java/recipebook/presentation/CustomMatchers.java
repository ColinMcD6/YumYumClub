package recipebook.presentation;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {

    public static Matcher<View> withRating(final float expectedStars) {
        return new BoundedMatcher<View, RatingBar>(RatingBar.class) {
            @Override
            protected boolean matchesSafely(RatingBar ratingBar) {
                return ratingBar.getRating() == expectedStars;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with stars: " + expectedStars);
            }
        };
    }

    public static Matcher<View> childAtPosition(
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

    public static Matcher<View> withTextInChildView(final String text, final int targetViewId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("RecyclerView item with text: " + text);
            }

            @Override
            public boolean matchesSafely(View view) {
                TextView targetView = view.findViewById(targetViewId);
                return targetView != null && targetView.getText().toString().equals(text);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(androidx.test.espresso.UiController uiController, View view) {
                View childView = view.findViewById(id);
                if (childView != null) {
                    childView.performClick();
                }
            }
        };
    }

    public static Matcher<View> isButtonNotVisible(final int buttonId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Button with id " + buttonId + " is not visible");
            }

            @Override
            protected boolean matchesSafely(View item) {
                View button = item.findViewById(buttonId);
                return button.getVisibility() == View.GONE;
            }
        };
    }
}
