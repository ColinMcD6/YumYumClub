package recipebook.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.business.ShoppingListInterface;
import recipebook.objects.Recipe;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private boolean deleteBtnVisible;

    private int uniqueID = 0;

    ShoppingListInterface shoppingListInterface;


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView recipeName;
        private final TextView frequnecy;
        private final RecyclerView ingredientRecycler;

        private final Button deleteBtn;

        public ViewHolder(View view)
        {
            super(view);

            recipeName = view.findViewById(R.id.recipe_text);
            frequnecy = view.findViewById(R.id.frequency);
            ingredientRecycler = view.findViewById(R.id.ingredient_recycler);
            deleteBtn = view.findViewById(R.id.delete_btn);
        }
        public TextView getRecipeName() {
            return recipeName;
        }
        public TextView getFrequency() {return frequnecy;}
        public RecyclerView getIngredientRecycler() {return ingredientRecycler; }
        public Button getDeleteBtn() {return deleteBtn;}
    }

    public ShoppingListAdapter() {
        shoppingListInterface = Services.getShoppingList();
        recipeList = shoppingListInterface.getCartRecipes();
        deleteBtnVisible = false;
    }

    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_list_item, viewGroup, false);

        return new ShoppingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.getRecipeName().setText(recipeList.get(position).getName());

        viewHolder.getFrequency().setText("x "+ String.valueOf(shoppingListInterface.getFrequency(recipeList.get(position).getID())));

        ShoppingListIngredientAdapter ingredientsAdapter = new ShoppingListIngredientAdapter(recipeList.get(position).getIngredients());
        viewHolder.getIngredientRecycler().setAdapter(ingredientsAdapter);
        viewHolder.getIngredientRecycler().setLayoutManager(new LinearLayoutManager(viewHolder.itemView.getContext(), LinearLayoutManager.VERTICAL, false));

        if(deleteBtnVisible){
            viewHolder.getDeleteBtn().setVisibility(View.VISIBLE);
        }else{
            viewHolder.getDeleteBtn().setVisibility(View.GONE);
        }

        viewHolder.getDeleteBtn().setTag(++uniqueID);

        viewHolder.getDeleteBtn().setOnClickListener(v -> {
            shoppingListInterface.removeRecipeFromCart(recipeList.get(position).getID());
            recipeList = shoppingListInterface.getCartRecipes();
            notifyDataSetChanged();
        });
    }

        @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setDeleteBtnVisible(boolean visible){
        deleteBtnVisible = visible;
        notifyDataSetChanged();
    }

    public void clearList(){
        shoppingListInterface.clearCart();
        recipeList = shoppingListInterface.getCartRecipes();
        notifyDataSetChanged();
    }

}
