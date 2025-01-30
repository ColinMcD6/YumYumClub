package recipebook.presentation;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import comp3350.yumyumclub.R;

import com.google.android.material.navigationrail.NavigationRailView;

public class MainActivity extends AppCompatActivity{

    // UI element
    private NavigationRailView bottomNavigationView;

    NavHostFragment navHostFragment;
    NavController navController;

    private boolean fragmentFlag = true;

     @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Remove top bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        navController = navHostFragment.getNavController();


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }



    // Unfocus text input when touching elsewhere
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent)
    {
        View focusView = getCurrentFocus();
        if (focusView != null && fragmentFlag)
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