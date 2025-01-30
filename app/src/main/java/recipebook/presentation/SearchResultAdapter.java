package recipebook.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.objects.Recipe;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private BrowseRecipeAdapter.OnRecipeClickListener onRecipeClickListener;
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView recipeImage;
        private final TextView recipeTitle;
        private final TextView recipeDescripiton;


        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View

            recipeImage = view.findViewById(R.id.recipe_image);
            recipeTitle = view.findViewById(R.id.recipe_title);
            recipeDescripiton = view.findViewById(R.id.recipe_description);
        }

        public ImageView getRecipeImageView() { return recipeImage; }
        public TextView getRecipeTitleView() {
            return recipeTitle;
        }
        public TextView getRecipeDescriptionView() {
            return recipeDescripiton;
        }
    }

    public SearchResultAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_recipe_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        Context context = viewHolder.getRecipeTitleView().getContext();
        int resId = context.getResources().getIdentifier(recipes.get(position).getImage_URL(), "drawable", context.getPackageName());

        viewHolder.getRecipeImageView().setImageResource(resId);
        viewHolder.getRecipeTitleView().setText(recipes.get(position).getName());
        //viewHolder.getRecipeDescriptionView().setText(recipes.get(position).getDescription()); //When description is ready

        viewHolder.itemView.setOnClickListener(view ->
            {
                Intent intent= new Intent(context, ViewRecipeActivity.class);
                intent.putExtra("ID", recipes.get(position).getID());
                context.startActivity(intent);
            }
        );
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
