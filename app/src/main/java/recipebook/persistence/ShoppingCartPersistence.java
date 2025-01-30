package recipebook.persistence;

import java.util.List;

import recipebook.objects.Recipe;

public interface ShoppingCartPersistence {
    List<Recipe> getShoppingCart(String username);

    int getNumOfRecipeInShoppingCart(String username, int recipeId);

    void addToShoppingCart(String username, int recipeID);

    void emptyShoppingCart(String username);

    void removeRecipeFromShoppingCart(String username, int recipeID);
}
