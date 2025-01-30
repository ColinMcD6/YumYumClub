package recipebook.presentation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.objects.Recipe;

public class BrowseCategoryAdapter extends RecyclerView.Adapter<BrowseCategoryAdapter.ViewHolder>
{
    private List<String> tags;

    private static int nextTagId = 0;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final RecyclerView recyclerView;
        private final TextView textView;
        public ViewHolder(View view)
        {
            super(view);
            // Define click listener for ViewHolder's View
            textView = view.findViewById(R.id.category_title);
            recyclerView = view.findViewById(R.id.category_recycler_view);
        }
        public RecyclerView getRecyclerView() { return recyclerView; }
        public TextView getTextView() { return textView; };
    }

    public BrowseCategoryAdapter()
    {
        tags = Services.getAccessRecipe().getTags();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.browse_recipes, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position)
    {
        Context context = viewHolder.getRecyclerView().getContext();
        List<Recipe> recipes = Services.getAccessRecipe().getRecipesByTag(tags.get(position));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(recipes.size());
        BrowseRecipeAdapter browseRecipeAdapter = new BrowseRecipeAdapter(recipes, tags.get(position));
        nextTagId += recipes.size();
        browseRecipeAdapter.setOnRecipeClickListener(new BrowseRecipeAdapter.OnRecipeClickListener() {
            @Override
            public void onClick(int recipeID) {
                context.startActivity(new Intent(context, ViewRecipeActivity.class).putExtra("ID", recipeID));
            }
        });
        viewHolder.getTextView().setText(tags.get(position));
        viewHolder.getRecyclerView().setAdapter(browseRecipeAdapter);
        viewHolder.getRecyclerView().setLayoutManager(linearLayoutManager);
        viewHolder.getRecyclerView().setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
