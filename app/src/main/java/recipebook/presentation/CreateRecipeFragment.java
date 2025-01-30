package recipebook.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.objects.Recipe;

public class CreateRecipeFragment extends Fragment {

    private FloatingActionButton createRecipeFAB;

    private RecyclerView customRecipeRecyclerView;

    List<Recipe> recipes = new ArrayList<>();

    int dynamicColumns;

    public CreateRecipeFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        // Recall recipes everytime this screen is up
        recipes = Services.getAccessRecipe().getCurrentCreatorRecipes();

        customRecipeRecyclerView = view.findViewById(R.id.custom_recipes_recycler);
        createRecipeFAB = view.findViewById(R.id.create_recipe_FAB);

        calculateRowNumber();

        displayCustomRecipes();

        createRecipeFAB.setOnClickListener(v -> {
            requireContext().startActivity(new Intent(requireContext(), CreateRecipeActivity.class));
        });



        return view;
    }

    private void displayCustomRecipes(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), dynamicColumns);
        SavedRecipesAdapter savedRecipesAdapter = new SavedRecipesAdapter(recipes);
        customRecipeRecyclerView.setLayoutManager(gridLayoutManager);
        customRecipeRecyclerView.setAdapter(savedRecipesAdapter);
        savedRecipesAdapter.setOnRecipeClickListener(new SavedRecipesAdapter.OnRecipeClickListener() {
            @Override
            public void onClick(int recipeID) {
                getContext().startActivity(new Intent(getContext(), ViewRecipeActivity.class).putExtra("ID", recipeID));
            }
        });
    }

    private void calculateRowNumber(){
        int displayWidth = getDisplayWidthDp();

        dynamicColumns = displayWidth < 300 ? 1: displayWidth/300;
    }

    private int getDisplayWidthDp(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        return Math.round(widthPixels / density);
    }


    @Override
    public void onResume() {
        super.onResume();
        recipes = Services.getAccessRecipe().getCurrentCreatorRecipes();
        displayCustomRecipes();
    }
}
