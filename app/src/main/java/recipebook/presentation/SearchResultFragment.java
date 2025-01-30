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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.search.SearchBar;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;
import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.objects.Recipe;

public class SearchResultFragment extends Fragment {


    private LinearLayout resultLayout;
    private SearchBar destinationSearchBar;
    private RecyclerView searchResultRecyclerView;
    private ChipGroup chipGroup;
    private HorizontalScrollView tagScrollView;
    private ImageButton searchBtn;

    private  EditText searchEditText;
    private CardView searchCardView;
    private ImageButton textClearBtn;

    private static final int CLICK_THRESHOLD = 10;
    private float startX;
    private float startY;

    private String tempSaveKeyword;
    private boolean firstChipIsOn =  false;

    // constant
    final int FADE_IN_DURATION = 400;
    final String ALL_TAG = "All";


    CoordinatorLayout coordinatorLayout;
    public SearchResultFragment(){  }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);


        // Declare UI Elements
        destinationSearchBar = view.findViewById(R.id.destination_search_bar);
        searchResultRecyclerView = view.findViewById(R.id.search_result_recycler);
        chipGroup = view.findViewById(R.id.chipGroup);
        tagScrollView = view.findViewById(R.id.tag_filter_scroll);

        searchCardView = view.findViewById(R.id.search_card_view);
        searchEditText = view.findViewById(R.id.search_edit_text);
        textClearBtn = view.findViewById(R.id.clear_bt);
        searchBtn = view.findViewById(R.id.search_bt);

        coordinatorLayout = view.findViewById(R.id.result_layout);
        resultLayout = view.findViewById(R.id.search_result_layout);


        /// Listener ///

        // Purpose: UI action when pressing tags(chip) in search result
        chipGroup.setOnCheckedStateChangeListener((chipGroup, list) -> {
            setChipInteraction(chipGroup, list);
        });

        // Purpose: When pressing search/enter in keyboard (SearchView MenuItem)
        searchEditText.setOnKeyListener((v, keyCode, event) ->
        {
            if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) && event.getAction() == KeyEvent.ACTION_UP && !searchEditText.getText().toString().isEmpty())
            {
                // Start search
                startSearch(searchEditText.getText().toString());
            }
            return false;
        });

        // Search Button, start search
        searchBtn.setOnClickListener(v -> {
            startSearch(searchEditText.getText().toString());
        });

        // Expand the search bar
        destinationSearchBar.setOnClickListener(v -> {
            expandSearch();
        });

        // clear text input
        textClearBtn.setOnClickListener(v -> {
            searchEditText.setText("");
        });

        // go back to main browse
        destinationSearchBar.setNavigationOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.popBackStack();
        });

        //

        // Collapse the keyboard
        searchResultRecyclerView.setOnTouchListener((v, event) -> {
            searchEditText.setText(tempSaveKeyword);
            collapseSearch();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            return false;
        });
        tagScrollView.setOnTouchListener((v, event) -> {
            searchEditText.setText(tempSaveKeyword);
            collapseSearch();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            return false;
        });

        // searchbar interaction
        resultLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (Math.abs(endX - startX) < CLICK_THRESHOLD && Math.abs(endY - startY) < CLICK_THRESHOLD) {
                        // Check if the touch event is outside the specific view
                        if (isTouchOutsideView(searchCardView, event)) {
                            // Close the view (or perform any other action)
                            searchEditText.setText(tempSaveKeyword);
                            collapseSearch();
                        }
                    }
                    return true;
            }
            return false;
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set the keyword and search from browse fragment
        String keyword = SearchResultFragmentArgs.fromBundle(getArguments()).getKeyword();
        destinationSearchBar.setText(keyword);
        searchEditText.setText(keyword);
        tempSaveKeyword = keyword;
        startSearch(keyword);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Listener
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void expandSearch(){
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.setStartView(destinationSearchBar);
        transform.setEndView(searchCardView);
        transform.addTarget(searchCardView);
        transform.setPathMotion(new MaterialArcMotion());
        transform.setScrimColor(Color.TRANSPARENT);

        TransitionManager.beginDelayedTransition(coordinatorLayout, transform);
        destinationSearchBar.setVisibility(View.GONE);
        searchCardView.setVisibility(View.VISIBLE);

        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        searchEditText.setSelection(searchEditText.getText().length());
    }

    public void collapseSearch(){
        if(searchCardView.getVisibility() == View.VISIBLE)
        {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);

            MaterialContainerTransform transform = new MaterialContainerTransform();
            transform.setStartView(searchCardView);
            transform.setEndView(destinationSearchBar);
            transform.addTarget(destinationSearchBar);
            transform.setPathMotion(new MaterialArcMotion());
            transform.setScrimColor(Color.TRANSPARENT);

            TransitionManager.beginDelayedTransition(coordinatorLayout, transform);
            searchCardView.setVisibility(View.GONE);

            destinationSearchBar.setText(searchEditText.getText());
            destinationSearchBar.setVisibility(View.VISIBLE);
        }
    }

    private void setChipInteraction(ChipGroup chipGroup, List<Integer> list){
        Chip chip;
        Chip firstChip = chipGroup.findViewById(0);
        List<String> filterTags = new ArrayList<>();

        if(firstChipIsOn){
            for(int id:list){
                chip = chipGroup.findViewById(id);

                // Turn off first tag when selecitng other tag
                if((id != 0 && chip.isChecked()))
                {
                    firstChip.setChecked(false);
                    firstChip.setClickable(true);
                    firstChipIsOn = false;

                    filterTags.add(chip.getText().toString());
                }
            }
        }
        else
        {
            if(firstChip.isChecked()){
                for(int id:list) {
                    chip = chipGroup.findViewById(id);
                    chip.setChecked(false);
                }
                firstChip.setChecked(true);
                firstChip.setClickable(false);
                firstChipIsOn = true;
            }
            else{
                for(int id:list){
                    chip = chipGroup.findViewById(id);
                    if(chip.isChecked()) {filterTags.add(chip.getText().toString());}
                }
            }

        }

        // When nothing is checked, turn on first tag
        if(filterTags.size() == 0){
            firstChip.setChecked(true);
            firstChip.setClickable(false);
            firstChipIsOn = true;
        }

        // Load filtered search result
        filterSearch(searchEditText.getText().toString(),filterTags);
    }

    private void startSearch(String keyword){
        List<Recipe> resultRecipe = Services.getSearchRecipes().searchByName(keyword);
        List<String> resultTags = Services.getSearchRecipes().getTagsOfSearchByName(keyword);

        if(searchEditText.getText().length() > 0)
        {
            collapseSearch();
            // Display result recipe on screen
            updateChips(resultTags);
            showResultRecipe(resultRecipe);
        }

        tempSaveKeyword = searchEditText.getText().toString();
    }

    // Apply tag filter
    private void filterSearch(String keyword, List<String> filterTags){
        //List<Recipe> resultRecipe = new ArrayList<>(); //replace with recipe getter method
        List<Recipe> resultRecipe = Services.getSearchRecipes().searchByNamePlusTag(keyword, filterTags);

        // Display filtered recipe on screen
        showResultRecipe(resultRecipe);
    }


    private void showResultRecipe(List<Recipe> recipes){
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        searchResultRecyclerView.setAdapter(new SearchResultAdapter(recipes));
        fadeIn(searchResultRecyclerView); // Fade in result view
    }

    // Dynamically change tags in search result depending on the resulting recipe list
    private void updateChips(List<String> chipList) {
        List<String> modList = chipList;
        modList.add(0, ALL_TAG);

        chipGroup.removeAllViews();
        int tagID = 0;
        for (String text : modList) {
            Chip chip = new Chip(requireActivity());
            chip.setText(text);
            chip.setId(tagID);
            chip.setCheckable(true);
            chipGroup.addView(chip);
            tagID++;
        }

        Chip firstChip = (Chip) chipGroup.getChildAt(0);
        firstChip.setChecked(true);
        firstChipIsOn = true;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper Method
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean isTouchOutsideView(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getWidth();
        int bottom = top + view.getHeight();

        return !(event.getRawX() > left && event.getRawX() < right && event.getRawY() > top && event.getRawY() < bottom);
    }

    private void fadeIn(View view){
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1f)
                .setDuration(FADE_IN_DURATION)
                .setListener(null);
    }

}
