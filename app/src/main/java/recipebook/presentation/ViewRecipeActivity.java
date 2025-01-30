package recipebook.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.business.AccessRecipe;
import recipebook.business.ShoppingListInterface;
import recipebook.objects.Recipe;

public class ViewRecipeActivity extends AppCompatActivity {
    private AccessRecipe accessRecipe;
    private ArrayAdapter<String> ingredientArrayAdapter;
    private ArrayAdapter<String> stepsArrayAdapter;
    private List<String> ingredientList;
    private List<String> stepsList;
    private int recipeID;
    String recipeName;
    private ImageButton addToFolderBtn;
    private ImageButton addToShoppingListBtn;
    private ShoppingListInterface shoppingListInterface;
    ImageView recipeImage;

    @Override
    protected void onCreate(Bundle savedInstanceData)
    {
        super.onCreate(savedInstanceData);
        setContentView(R.layout.activity_view_recipe);

        // Remove top bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        accessRecipe = new AccessRecipe();
        recipeID = getIntent().getIntExtra("ID", -1);

        Recipe targetRecipe = accessRecipe.getRecipeByID(recipeID);
        ingredientList = targetRecipe.getIngredients();
        stepsList = targetRecipe.getSteps();

        //display recipe name
        TextView recipeTitle = findViewById(R.id.recipe_name);
        recipeName = targetRecipe.getName();
        recipeTitle.setText(recipeName);

        ingredientArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, ingredientList)
        { //displays list of ingredients in ListView
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text1.setText(ingredientList.get(position));
                return view;
            }
        };
        stepsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, stepsList)
        { //display list of numbered recipe steps in ListView
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                text2.setText(stepsList.get(position));
                text1.setText(String.valueOf(position +1));
                return view;
            }
        };

        final ListView listView = (ListView) findViewById(R.id.ingredient_list);
        listView.setAdapter(ingredientArrayAdapter);

        final ListView listViewSteps = (ListView) findViewById(R.id.steps_list);
        listViewSteps.setAdapter(stepsArrayAdapter);

        //display image
        recipeImage = findViewById(R.id.image_box);
        int resourceId = getResources().getIdentifier(targetRecipe.getImage_URL(), "drawable", getPackageName());
        if (resourceId != 0) {
            recipeImage.setImageResource(resourceId);
        }

        addToFolderBtn = findViewById(R.id.add_recipe_bt);
        addToShoppingListBtn = findViewById(R.id.add_shopping_list_bt);
        shoppingListInterface = Services.getShoppingList();

        addToFolderBtn.setOnClickListener(v -> {
            AddRecipeFragment addRecipeBottomSheetFragment = new AddRecipeFragment(recipeID );
            addRecipeBottomSheetFragment.show(getSupportFragmentManager(), AddRecipeFragment.TAG);
        });

        addToShoppingListBtn.setOnClickListener(v -> {
            shoppingListInterface.addRecipeToCart(recipeID);
            Snackbar snackbar = Snackbar.make(v, "Recipe added to Shopping List!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {  }
                    });

            snackbar.show();
        });
    }
    public void buttonOnClick(View view) { finish(); }

    public void commentsOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), ViewCommentsActivity.class).putExtra("ID", recipeID).putExtra("Name", recipeName));
    }
}
