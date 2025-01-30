package recipebook.presentation;

import android.view.View;
import android.widget.RatingBar;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;

public class SetStars implements ViewAction {
    private static int numStars;

    public SetStars(int numStars) {
        SetStars.numStars = numStars;
    }

    public Matcher<View> getConstraints() {
        return ViewMatchers.isAssignableFrom(RatingBar.class);
    }

    @Override
    public String getDescription() {
        return "Set number of stars on RatingBar";
    }

    @Override
    public void perform(UiController uiController, View view) {
        RatingBar ratingBar = (RatingBar) view;
        ratingBar.setRating(numStars);
    }
}
