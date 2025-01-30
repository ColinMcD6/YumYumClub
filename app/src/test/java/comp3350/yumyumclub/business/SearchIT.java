package comp3350.yumyumclub.business;

import recipebook.business.SearchRecipeInterface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.business.AccessRecipe;
import recipebook.business.SearchRecipes;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;


public class SearchIT {
    SearchRecipeInterface search;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        RecipePersistence persistence = new RecipePersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.search= new SearchRecipes(persistence);

    }

    @Test
    public void testSearch() {
        //testing searchByName
        List<Recipe> otherRecipes = search.searchByName("Chewy Brownies");
        assertNotNull("Recipes list should not be null", otherRecipes);
        assertFalse("Recipes list should not be empty", otherRecipes.isEmpty());
        assert(otherRecipes.get(0).getName().equals("Chewy Brownies"));

        //testing getTagsOfSearchByName
        List<String> moreTags = search.getTagsOfSearchByName("Chewy Brownies");
        assertNotNull("Tags list should not be null", moreTags);
        assertFalse("Tags list should not be empty", moreTags.isEmpty());
        assert(moreTags.size()==3);

        //testing SearchByNamePlusTag
        List<String> newTags = new ArrayList<>();
        newTags.add("Baking");
        newTags.add("Vegetarian");
        List<Recipe> evenMoreRecipes = search.searchByNamePlusTag("Cake", newTags);
        assertNotNull("Recipes list should not be null", evenMoreRecipes);
        assertFalse("Recipes list should not be empty", evenMoreRecipes.isEmpty());
        assert(evenMoreRecipes.size()==2);

    }


    @After
    public void tearDown(){
        // reset DB
        this.tempDB.delete();
    }

}
