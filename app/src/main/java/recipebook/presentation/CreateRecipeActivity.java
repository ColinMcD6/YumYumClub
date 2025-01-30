package recipebook.presentation;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.business.AccessUser;
import recipebook.business.CreateRecipeInterface;

public class CreateRecipeActivity extends Activity
{
    // Define UI objects
    TextInputEditText addRecipeTitleBox;
    TextInputEditText addTagsBox;
    TextInputEditText addIngredientsBox;
    TextInputEditText addStepsBox;

    MaterialButton postBtn;
    MaterialButton cancelBtn;

    CreateRecipeInterface createRecipeObj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        createRecipeObj = Services.getCreateRecipe();

        //Assign UI objects
        addRecipeTitleBox = findViewById(R.id.addRecipeTitle_box);
        addTagsBox = findViewById(R.id.addTags_box);
        addIngredientsBox = findViewById(R.id.addIngredients_box);
        addStepsBox = findViewById(R.id.addSteps_box);
        postBtn = findViewById(R.id.post_bt);
        cancelBtn = findViewById(R.id.cancel_post_bt);

        postBtn.setOnClickListener(v -> {
            postOnClick();
        });

        cancelBtn.setOnClickListener(v -> {
            goBackOnClick();
        });
    }

    // Post button - inputted title, tags, ingredients, steps, photo url sent to logic layer
    public void postOnClick()
    {
        String title = addRecipeTitleBox.getText().toString();
        String tags = addTagsBox.getText().toString();
        String ingredients = addIngredientsBox.getText().toString();
        String steps = addStepsBox.getText().toString();

        List<String> tagsList = Arrays.asList(tags.split(","));
        List<String> ingredientsList = Arrays.asList(ingredients.split(","));
        List<String> stepsList = Arrays.asList(steps.split(","));


        String creator = AccessUser.getInstance().getCurrentUser().getUserName();

        String returnMessage = createRecipeObj.validInput(title, trimList(stepsList), trimList(ingredientsList), trimList(tagsList));

        // Logic layer - adding recipe to DB

        if (title.length() > 500 )
        {
            PostEmptyDialog("Recipe title too long. Must be 500 characters or less.");
        }
        else if ( steps.length() > 5000 )
        {
            PostEmptyDialog("Steps field too long. Must be 5000 characters or less.");
        }
        else if ( ingredients.length() > 1000 )
        {
            PostEmptyDialog("Ingredients field too long. Must be 1000 characters or less.");
        }
        else if ( tags.length() > 100 )
        {
            PostEmptyDialog("Tags field too long. Must be 100 characters or less.");
        }
        else if(!returnMessage.equals("Success!"))
        {
            PostEmptyDialog(returnMessage);
        }
        else
        {
            createRecipeObj.createRecipe(title, trimList(stepsList), trimList(ingredientsList), trimList(tagsList), creator, "cooking_pot");
            PostCompleteDialog();
        }
    }

    private List<String> trimList(List<String> strings)
    {
        List<String> trimmedList = new ArrayList<String>();
        for (String s: strings)
        {
            trimmedList.add(s.trim());
        }

        return trimmedList;
    }

    // Notification message - press ok to finish creating account
    private void PostCompleteDialog()
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Posted! \uD83E\uDD73")
                .setMessage("Press OK and enter your newly created recipe!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Call Logic layer to add recipe

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .show();
    }

    // Notification message for if inputs are left blank
    private void PostEmptyDialog(String errorMessage)
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Oops! \uD83D\uDE22")
                .setMessage(errorMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    // Go back button - Cancel activity
    public void goBackOnClick()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    // Unfocus text input when touching elsewhere
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent)
    {
        View focusView = getCurrentFocus();
        if (focusView != null)
        {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) motionEvent.getX(), y = (int) motionEvent.getY();
            if (!rect.contains(x, y))
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }
}
