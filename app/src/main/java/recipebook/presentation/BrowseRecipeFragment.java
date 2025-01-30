package recipebook.presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

import comp3350.yumyumclub.R;

public class BrowseRecipeFragment extends Fragment
{


    // UI element object
    private EditText searchEditText;
    private SearchBar searchBar;
    private CardView searchCardView;
    private ImageButton searchBtn;
    private ImageButton textClearBtn;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView scrollView;
    private LinearLayoutManager categoryLinearLayoutManager;
    private BrowseCategoryAdapter browseCategoryAdapter;
    private CircularProgressIndicator loadingIndicator;
    private RecyclerView categoryRecyclerView;

    // constant
    final int FADE_IN_DURATION = 400;
    private static final int CLICK_THRESHOLD = 10;
    private float startX;
    private float startY;


    // Constructor
    public BrowseRecipeFragment(){ }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        // Assign UI objects
        searchBar = view.findViewById(R.id.start_search_bar);
        coordinatorLayout = view.findViewById(R.id.browse_layout);
        searchCardView = view.findViewById(R.id.search_card_view);
        textClearBtn= view.findViewById(R.id.clear_bt);
        searchBtn = view.findViewById(R.id.search_bt);
        searchEditText = view.findViewById(R.id.search_edit_text);
        categoryRecyclerView = view.findViewById(R.id.browse_main);
        scrollView = view.findViewById(R.id.outter_scroll);
        loadingIndicator = view.findViewById(R.id.browse_loading_indicator);

        loadingIndicator.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);


        /// Listener ///

        // Search Button click
        searchBtn.setOnClickListener(v -> enterSearch());

        // Search bar, will expand on touch
        searchBar.setOnClickListener(v -> expandSearch());

        // X button; Clear search bat text input
        textClearBtn.setOnClickListener(v -> searchEditText.setText(""));

        // Search Button click
        searchBtn.setOnClickListener(v -> enterSearch());

        // When pressing search/enter in keyboard (SearchView MenuItem)
        searchEditText.setOnKeyListener((v, keyCode, event) ->
        {
            if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) && event.getAction() == KeyEvent.ACTION_UP && !searchEditText.getText().toString().isEmpty())
            {
                enterSearch();
            }
            return false;
        });


        return view;
    }// end onCreateView



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Setup browse recycler with separate thread
        categoryRecyclerView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                browseCategoryAdapter = new BrowseCategoryAdapter();
                categoryRecyclerView.setAdapter(browseCategoryAdapter);
                categoryLinearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
                categoryRecyclerView.setLayoutManager(categoryLinearLayoutManager);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update UI elements
                        loadingIndicator.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        fadeIn(categoryRecyclerView);

                    }
                });
            }
        }).start();

        // Hide both keyboard and expanded search bar when tapping outside.
        categoryRecyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    @SuppressLint("ClickableViewAccessibility") InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (Math.abs(endX - startX) < CLICK_THRESHOLD && Math.abs(endY - startY) < CLICK_THRESHOLD) {
                        // Check if the touch event is outside the specific view
                        if (isTouchOutsideView(searchCardView, event)) {
                            // Close the view (or perform any other action)
                            collapseSearch();
                        }
                    }
                    return true;
            }
            return false;
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener related
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Expand the search Bar
    private void expandSearch(){
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setStartView(searchBar);
        transform.setEndView(searchCardView);
        transform.addTarget(searchCardView);
        transform.setPathMotion(new MaterialArcMotion());
        transform.setScrimColor(Color.TRANSPARENT);
        TransitionManager.beginDelayedTransition(coordinatorLayout, transform);
        searchBar.setVisibility(View.GONE);
        searchCardView.setVisibility(View.VISIBLE);

        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        searchEditText.setSelection(searchEditText.getText().length());

    }

    // Collapse search bar
    private void collapseSearch(){

        if(searchCardView.getVisibility() == View.VISIBLE){
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

            MaterialContainerTransform transform = new MaterialContainerTransform();
            transform.setStartView(searchCardView);
            transform.setEndView(searchBar);
            transform.addTarget(searchBar);
            transform.setPathMotion(new MaterialArcMotion());
            transform.setScrimColor(Color.TRANSPARENT);

            TransitionManager.beginDelayedTransition(coordinatorLayout, transform);
            searchCardView.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

        }
    }

    // Move to search result fragment with entered keyword.
    private void enterSearch(){
        if(searchEditText.getText().length() > 0)
        {
            NavController navController = Navigation.findNavController(requireView());
            BrowseRecipeFragmentDirections.SendKeyword action =
                    BrowseRecipeFragmentDirections.sendKeyword("");
            action.setKeyword(searchEditText.getText().toString());
            navController.navigate(action);

            collapseSearch();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    // Helper methods
    ///////////////////////////////////////////////////////////////////////////////////////

    // Check if touch is in or out of specifc view
    private boolean isTouchOutsideView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        return !(event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom);
    }

    // Fade-in animation
    private void fadeIn(View view){
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(FADE_IN_DURATION)
                .setListener(null);
    }




    // Fragment Resume & Pause override ///////////////////////////////////////////////////////////

    // Empty search bar message on return.
    @Override
    public void onResume() {
        super.onResume();
        searchEditText.setText(null);
    }
}
