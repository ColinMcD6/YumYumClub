package recipebook.business;

import java.util.List;

import recipebook.objects.Recipe;

public interface ShoppingListInterface
{
    void addRecipeToCart(int recipe);

    int getFrequency(int recipeId);

    List<Recipe> getCartRecipes();

    void removeRecipeFromCart(int recipeId);

    void clearCart();
}
