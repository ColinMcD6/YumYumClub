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

public class BrowseRecipeAdapter extends RecyclerView.Adapter<BrowseRecipeAdapter.ViewHolder>
{
    private List<Recipe> recipes;
    private String categoryName;
    private OnRecipeClickListener onRecipeClickListener;
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.textView);
            imageView = view.findViewById(R.id.image_box);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() { return imageView; }
    }

    public BrowseRecipeAdapter(List<Recipe> recipes, String categoryName) {
        this.categoryName = categoryName;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.browse_recipe_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        Context context = viewHolder.getTextView().getContext();
        viewHolder.getTextView().setText(recipes.get(position).getName());
        int resId = context.getResources().getIdentifier(recipes.get(position).getImage_URL(), "drawable", context.getPackageName());
        viewHolder.getImageView().setImageResource(resId);
        viewHolder.itemView.setContentDescription(recipes.get(position).getName() + categoryName);
        viewHolder.itemView.setOnClickListener(view -> onRecipeClickListener.onClick(recipes.get(position).getID()));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setOnRecipeClickListener(OnRecipeClickListener onRecipeClickListener)
    {
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public interface OnRecipeClickListener
    {
        void onClick(int recipeID);
    }

}
