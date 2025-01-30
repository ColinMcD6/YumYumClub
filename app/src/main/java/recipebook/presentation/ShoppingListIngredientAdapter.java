package recipebook.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.yumyumclub.R;


public class ShoppingListIngredientAdapter extends RecyclerView.Adapter<ShoppingListIngredientAdapter.ViewHolder> {

    List<String> ingredients;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView ingredient; // transition start view

        public ViewHolder(View view)
        {
            super(view);

            ingredient = view.findViewById(R.id.ingredient_text);

        }
        public TextView getIngredient() {
            return ingredient;
        }
    }



    public  ShoppingListIngredientAdapter(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    @NonNull
    @Override
    public ShoppingListIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_list_ingredients_item, viewGroup, false);

        return new ShoppingListIngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListIngredientAdapter.ViewHolder viewHolder,final int position) {
        viewHolder.getIngredient().setText(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}
