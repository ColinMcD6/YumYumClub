package comp3350.yumyumclub.business;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.application.Main;
import recipebook.business.AccessUser;
import recipebook.business.ShoppingList;
import recipebook.objects.Recipe;
import recipebook.objects.User;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.hsqldb.ShoppingCartPersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class ShoppingCartTest {

    private File tempDB;

    @Mock
    ShoppingCartPersistenceHSQLDB persistence;

    ShoppingList shoppingList;

    @Mock
    AccessUser accessUser;

    @Mock
    RecipePersistence recipePersistence;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        shoppingList = new ShoppingList(persistence, accessUser,recipePersistence);
    }

    @Test
    public void testShoppingCart()
    {
        Recipe r1 = new Recipe("30 minute Beef Tacos", Arrays.asList("Heat up beef", "Cut onions"),Arrays.asList("Beef", "Tortilla"), Arrays.asList("Quick", "Cooking"), "YumYumBookOriginal" , "taco");
        Recipe r2 = new Recipe("30 minute Beef Tacos", Arrays.asList("Heat up beef", "Cut onions"),Arrays.asList("Beef", "Tortilla"), Arrays.asList("Quick", "Cooking"), "YumYumBookOriginal" , "taco");
        when(recipePersistence.getRecipeByID(1)).thenReturn(r1);
        when(recipePersistence.getRecipeByID(2)).thenReturn(r2);
        User mockUser = new User("Seyi","123", "123@123.com");
        when(accessUser.getCurrentUser()).thenReturn(mockUser);

        when(persistence.getShoppingCart("Seyi")).thenReturn(Arrays.asList(r1,r2));

        List<Recipe> recipeCart = shoppingList.getCartRecipes();
        verify(persistence).getShoppingCart("Seyi");
        assert(recipeCart.size()==2);

        //add to cart
        shoppingList.addRecipeToCart(2);
        verify(persistence).addToShoppingCart("Seyi", 2);

        //verifying it allows it being added more than once
        shoppingList.addRecipeToCart(2);
        verify(persistence, times(2)).addToShoppingCart("Seyi", 2);

        //verifying it DOESNT allow adding recipes that dont exist
        when(recipePersistence.getRecipeByID(8)).thenReturn(null);
        shoppingList.addRecipeToCart(8);

        //get frequency of a recipe in the cart
        when(persistence.getNumOfRecipeInShoppingCart("Seyi", 1)).thenReturn(1);
        when(persistence.getNumOfRecipeInShoppingCart("Seyi", 2)).thenReturn(2);

        int numR = shoppingList.getFrequency(1);
        verify(persistence).getNumOfRecipeInShoppingCart("Seyi",1);
        assert(numR == 1);

        numR = shoppingList.getFrequency(2);
        verify(persistence).getNumOfRecipeInShoppingCart("Seyi", 2);
        assert(numR ==2);

        //remove recipe
        shoppingList.removeRecipeFromCart(1);
        verify(persistence).removeRecipeFromShoppingCart("Seyi",1);

        when(persistence.getNumOfRecipeInShoppingCart("Seyi",1)).thenReturn(0);
        numR = shoppingList.getFrequency(1);
        assert(numR == 0);

        //empty the cart
        shoppingList.clearCart();
        verify(persistence).emptyShoppingCart("Seyi");
        when(persistence.getShoppingCart("Seyi")).thenReturn(new ArrayList<Recipe>());
        recipeCart = shoppingList.getCartRecipes();
        assert(recipeCart.isEmpty());

    }

}