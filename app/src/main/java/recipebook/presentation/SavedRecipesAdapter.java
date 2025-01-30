package recipebook.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.objects.Recipe;

public class SavedRecipesAdapter extends RecyclerView.Adapter<SavedRecipesAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private SavedRecipesAdapter.OnRecipeClickListener onRecipeClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.recipe_title);
            imageView = view.findViewById(R.id.recipe_image);
        }

        public TextView getTextView() { return textView; }
        public ImageView getImageView() { return imageView; }
    }

    public SavedRecipesAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public SavedRecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_recipe_item, viewGroup, false);
        return new SavedRecipesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedRecipesAdapter.ViewHolder viewHolder, final int position)
    {
        Context context = viewHolder.getImageView().getContext();

        int resId = viewHolder.getImageView().getResources().getIdentifier(recipes.get(position).getImage_URL(), "drawable", context.getPackageName());

        viewHolder.getImageView().setImageResource(resId);
        viewHolder.getTextView().setText(recipes.get(position).getName());

        viewHolder.itemView.setOnClickListener(view -> onRecipeClickListener.onClick(recipes.get(position).getID()));
    }

    public int getItemCount() { return recipes.size(); }

    public void setOnRecipeClickListener(SavedRecipesAdapter.OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public interface OnRecipeClickListener {
        void onClick(int recipeID);
    }
}

