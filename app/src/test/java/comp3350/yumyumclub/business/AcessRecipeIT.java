package comp3350.yumyumclub.business;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.business.AccessRecipe;
import recipebook.business.AccessRecipeInterface;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class AcessRecipeIT {
    AccessRecipeInterface accessRecipe;
    AccessUser user ;
    private File tempDB;

    @Before
    public void setUp() throws IOException {
        this.tempDB = TestUtils.copyDB();
        UserPersistence persistence1 = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.user = new AccessUser(persistence1);
        RecipePersistence persistence = new RecipePersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.accessRecipe= new AccessRecipe(persistence);
    }

    @Test
    public void testRecipe()
    {
        Recipe r1 = accessRecipe.getRecipeByID(1);
        //testing getRecipeByID
        assert(r1.getID()==1);

        //testing getRecipesByTag
        for(String tag:accessRecipe.getTags())
        {
            for(Recipe recipe:accessRecipe.getRecipesByTag(tag))
            {
                assert(recipe.containsTag(tag));
            }
        }

        //testing getTags()
        List<String> tags = accessRecipe.getTags();
        assertNotNull("Tags list should not be null", tags);
        assertFalse("Tags list should not be empty", tags.isEmpty());
        assertEquals(tags.size(),4);

        //testing getAllRecipes
        List<Recipe> recipes = accessRecipe.getAllRecipes();
        assertNotNull("Recipes list should not be null", recipes);
        assertFalse("Recipes list should not be empty", recipes.isEmpty());
        assertEquals(recipes.size(),6);

        //testing getCurrentCreatorRecipes()
        user.logIn("Seyi","123");
        List<Recipe> newRecipes = accessRecipe.getCurrentCreatorRecipes();
        assertNotNull("Recipes list should not be null", newRecipes);
        assertTrue(newRecipes.isEmpty());
    }


    @After
    public void tearDown() {
        // reset DB
        this.tempDB.delete();
    }
}
