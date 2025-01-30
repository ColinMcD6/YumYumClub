package recipebook.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.business.AccessFolderInterface;
import recipebook.objects.Recipe;

public class SavedRecipesFragment extends Fragment {
    private RecyclerView savedRecipesRecyclerView;
    private List<Recipe> recipes;
    private int dynamicColumns;

    private ImageButton goBackBtn;
    private TextView folderNameBox;

    private String folderName;

    private AccessFolderInterface accessFolder;

    public SavedRecipesFragment() {
        accessFolder = Services.getAccessFolder();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside_saved, container, false);
        savedRecipesRecyclerView = view.findViewById(R.id.saved_recipe_recycler);
        goBackBtn = view.findViewById(R.id.go_back_folders_bt);
        folderNameBox = view.findViewById(R.id.comment_section_title);

        String folderName = SavedRecipesFragmentArgs.fromBundle(getArguments()).getFolderName();
        this.folderName = folderName;

        recipes = accessFolder.getRecipesByFolder(folderName);
        //recipes  = Services.getAccessRecipe().getRecipesByTag("Quick");


        calculateRowNumber();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), dynamicColumns);
        SavedRecipesAdapter savedRecipesAdapter = new SavedRecipesAdapter(recipes);


        folderNameBox.setText(folderName);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireView());

                navController.popBackStack();
            }
        });
        savedRecipesAdapter.setOnRecipeClickListener(new SavedRecipesAdapter.OnRecipeClickListener() {
            @Override
            public void onClick(int recipeID) {
                getContext().startActivity(new Intent(getContext(), ViewRecipeActivity.class).putExtra("ID", recipeID));
            }
        });

        savedRecipesRecyclerView.setAdapter(savedRecipesAdapter);
        savedRecipesRecyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }


    private void calculateRowNumber(){
        int displayWidth = getDisplayWidthDp();

        dynamicColumns = displayWidth/300;
    }

    private int getDisplayWidthDp(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        return Math.round(widthPixels / density);
    }
}

