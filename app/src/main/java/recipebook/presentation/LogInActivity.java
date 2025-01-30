package recipebook.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import comp3350.yumyumclub.R;
import recipebook.application.Main;
import recipebook.business.AccessUser;

public class LogInActivity extends Activity
{
    // Declare interacting UI objects
    Button newUser;
    TextInputEditText inputAccountBox;
    TextInputEditText inputPasswordBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        copyDatabaseToDevice();
        setContentView(R.layout.activity_login);

        // Assign UI objects
        inputAccountBox = findViewById(R.id.account_box);
        inputPasswordBox = findViewById(R.id.password_box);
        newUser = findViewById(R.id.createAccount_bt);
    }

    // Log in button - Move to main screen if match / mismatch notification
    public void LoginOnClick(View view)
    {
        // Check match using logic layer
        if(AccessUser.getInstance().logIn(inputAccountBox.getText().toString(), inputPasswordBox.getText().toString()))
        {
            // Move to browse activity
            Intent mainIntent= new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(mainIntent);
        }
        else
        {
            // Message if User Password is incorrect
            WrongInputDialog();
        }
    }

    // notification popup - log in mismatch
    private void WrongInputDialog()
    {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Oops!")
                .setMessage("Incorrect Email/Password! Please try again")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        inputPasswordBox.setText("");
                    }
                })
                .show();
    }

    // New User button - Move to Sign-up Activity
    public void NewUserOnClick(View view)
    {
        Intent createAccountIntent= new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivityForResult(createAccountIntent, 123);
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

    // Clear text field on return to log-in Activity
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            inputAccountBox.setText(null);
            inputPasswordBox.setText(null);
        }
        else if (resultCode == Activity.RESULT_CANCELED && requestCode == 123)
        {
            inputPasswordBox.setText(null);
        }
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Main.setDBPathName(dataDirectory.toString() + "/" + Main.getDBPathName());

        } catch (final IOException ioe) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("warning");
            alertDialog.setMessage("Unable to access application data: " + ioe.getMessage());

            alertDialog.show();
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }
}
