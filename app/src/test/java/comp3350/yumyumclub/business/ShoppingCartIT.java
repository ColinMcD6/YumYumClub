package comp3350.yumyumclub.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.application.Main;
import recipebook.business.AccessUser;
import recipebook.business.ShoppingList;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.ShoppingCartPersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class ShoppingCartIT {

    private File tempDB;
    ShoppingCartPersistenceHSQLDB persistence;
    ShoppingList shoppingList;
    AccessUser accessUser;
    RecipePersistence recipePersistence;

    @Before
    public void setUp() throws IOException {
        tempDB = TestUtils.copyDB();
        persistence = new ShoppingCartPersistenceHSQLDB(Main.getDBPathName());
        accessUser = new AccessUser(new UserPersistenceHSQLDB(Main.getDBPathName()));
        recipePersistence = new RecipePersistenceHSQLDB(Main.getDBPathName());
        shoppingList = new ShoppingList(persistence, accessUser, recipePersistence);
    }

    @Test
    public void testShoppingCart()
    {
        accessUser.logIn("Seyi", "123");

        List<Recipe> recipeCart = shoppingList.getCartRecipes();
        assert(recipeCart.size()==4);

        //add to cart - we know recipeID 1 isnt in this
        shoppingList.addRecipeToCart(1);

        //get frequency of a recipe in the cart
        int numR = shoppingList.getFrequency(1);
        assert(numR == 1);
        shoppingList.addRecipeToCart(1);
        numR = shoppingList.getFrequency(1);
        assert(numR == 2);

        //remove recipe
        shoppingList.removeRecipeFromCart(1);
        numR = shoppingList.getFrequency(1);
        assert(numR == 0);
        recipeCart = shoppingList.getCartRecipes();
        assert(recipeCart.size() == 4 );


        //empty the cart
        shoppingList.clearCart();
        recipeCart = shoppingList.getCartRecipes();
        assert(recipeCart.isEmpty());


    }


    @After
    public void tearDown()
    {
        tempDB.delete();
    }
}