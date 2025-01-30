package recipebook.business;

import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.ShoppingCartPersistence;

public class ShoppingList implements ShoppingListInterface
{
    private ShoppingCartPersistence shoppingCartPersistence;
    private AccessUser accessUser;
    private RecipePersistence recipePersistence;

    public ShoppingList()
    {
        this.shoppingCartPersistence = Services.getShoppingCartPersistence();
        this.accessUser = AccessUser.getInstance();
        this.recipePersistence = Services.getRecipePersistence();
    }

    public ShoppingList(ShoppingCartPersistence persistence, AccessUser accessUser, RecipePersistence recipePersistence)
    {
        this.shoppingCartPersistence = persistence;
        this.accessUser = accessUser;
        this.recipePersistence = recipePersistence;
    }

    @Override
    public void addRecipeToCart(int recipeId)
    {
        if(recipePersistence.getRecipeByID(recipeId)!=null)
            shoppingCartPersistence.addToShoppingCart( accessUser.getCurrentUser().getUserName(), recipeId );
    }

    @Override
    public int getFrequency(int recipeId)
    {
        return shoppingCartPersistence.getNumOfRecipeInShoppingCart( accessUser.getCurrentUser().getUserName(), recipeId );
    }

    @Override
    public List<Recipe> getCartRecipes()
    {
        return shoppingCartPersistence.getShoppingCart( accessUser.getCurrentUser().getUserName() );
    }

    @Override
    public void removeRecipeFromCart(int recipeId)
    {
        shoppingCartPersistence.removeRecipeFromShoppingCart( accessUser.getCurrentUser().getUserName(), recipeId );
    }

    @Override
    public void clearCart()
    {
        shoppingCartPersistence.emptyShoppingCart( accessUser.getCurrentUser().getUserName() );
    }
}
