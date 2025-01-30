package recipebook.presentation;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import comp3350.yumyumclub.R;
import recipebook.business.AccessUser;

public class CreateAccountActivity extends Activity
{
    // Define UI objects
    TextInputEditText addEmailBox;
    TextInputEditText addNameBox;
    TextInputEditText addPasswordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Assign UI objects
        addEmailBox = findViewById(R.id.addEmail_box);
        addNameBox = findViewById(R.id.addName_box);
        addPasswordBox = findViewById(R.id.addPassword_box);
    }

    // Sign-up button - inputted email, name, password sent to logic layer
    public void SignUpOnClick(View view)
    {
        String email = addEmailBox.getText().toString();
        String name = addNameBox.getText().toString();
        String password = addPasswordBox.getText().toString();

        // Logic layer - adding user to DB
        if(AccessUser.getInstance().checkEmpty(name,password,email)|| name.contains(" ") || email.contains(" ") || !email.contains("@"))
        {
            SignUpEmptyDialog();
        }
        else if (!AccessUser.getInstance().checkStringLength(name, password, email))
        {
            SignUpStringTooLargeDialog();
        }
        else if(AccessUser.getInstance().checkUser(name,password,email))
        {
            SignUpInvalidDialog();
        }
        else
        {
            SignUpCompleteDialog(name, password, email);
        }
    }

    // Notification message - press ok to finish creating account
    private void SignUpCompleteDialog(String name, String password, String email)
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Almost There! \uD83E\uDD73")
                .setMessage("Press OK and enter your newly created user name and password!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Call Logic layer to add user
                        AccessUser.getInstance().addUser(name,password,email);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .show();
    }

    // Notification message - when user name already exist in db
    private void SignUpInvalidDialog()
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Oops! \uD83D\uDE22")
                .setMessage("User already exists!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        addEmailBox.setText(null);
                        addNameBox.setText(null);
                        addPasswordBox.setText(null);
                    }
                })
                .show();
    }
    private void SignUpStringTooLargeDialog()
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Oops! \uD83D\uDE22")
                .setMessage("Username, email, and password must each be 50 characters or less.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addEmailBox.setText(null);
                        addNameBox.setText(null);
                        addPasswordBox.setText(null);
                    }
                })
                .show();
    }

   // Notification message for if inputs are left blank
   private void SignUpEmptyDialog()
   {
       new MaterialAlertDialogBuilder(this)
               .setTitle("Oops! \uD83D\uDE22")
               .setMessage("Please fill in all fields correctly! Make sure your email is valid and your username contains no spaces.")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       addEmailBox.setText(null);
                       addNameBox.setText(null);
                       addPasswordBox.setText(null);
                   }
               })
               .show();
   }


    // Go back button - Cancel Sign-up activity
    public void GoBackOnClick(View view)
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
