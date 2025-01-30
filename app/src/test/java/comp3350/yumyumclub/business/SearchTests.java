package comp3350.yumyumclub.business;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recipebook.business.SearchRecipeInterface;
import recipebook.business.SearchRecipes;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;


public class SearchTests {


    SearchRecipeInterface searchRecipe;
    @Mock
    RecipePersistence persistence;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        searchRecipe = new SearchRecipes(persistence);
    }

    @Test
    public void testSearch()
    {
        Recipe recipe1 = new Recipe(1, "Chocolate Cake", Arrays.asList("Step1"), Arrays.asList("Ingredient1"), Arrays.asList("Baking"), "User1", "URL1");
        Recipe recipe2 = new Recipe(2, "Vanilla Cake", Arrays.asList("Step2"), Arrays.asList("Ingredient2"), Arrays.asList("Baking"), "User2", "URL2");


        when(persistence.searchRecipeByName("cake")).thenReturn(Arrays.asList(recipe1, recipe2));
        when(persistence.searchRecipeByName("dne")).thenReturn(new ArrayList<>());
        when(persistence.searchRecipeByName("")).thenReturn(Arrays.asList(recipe1, recipe2, recipe1, recipe2, recipe1, recipe2));
        when(persistence.searchRecipeByName(null)).thenReturn(new ArrayList<>());


        List<Recipe> searchResults = searchRecipe.searchByName("cake");
        assert(searchResults.size()==2);


        searchResults = searchRecipe.searchByName("dne");
        assert(searchResults!=null);
        assert (searchResults.size()==0);

        searchResults = searchRecipe.searchByName(""); //should return all recipes
        assert(searchResults!=null);
        assert(searchResults.size()==6);

        searchResults = searchRecipe.searchByName(null);
        assert(searchResults!=null);
        assert(searchResults.size()==0);

        when(persistence.getTagsOfSearchRecipeByName("cake")).thenReturn(Arrays.asList("Baking"));
        when(persistence.getTagsOfSearchRecipeByName("")).thenReturn(Arrays.asList("Baking", "Cooking", "Dessert", "Vegan"));
        when(persistence.getTagsOfSearchRecipeByName(null)).thenReturn(new ArrayList<>());

        List<String> tagsOfResults = searchRecipe.getTagsOfSearchByName("cake");
        assert(tagsOfResults!=null);
        assert(tagsOfResults.contains("Baking"));

        tagsOfResults = searchRecipe.getTagsOfSearchByName("");
        assert(tagsOfResults.size()==4);

        tagsOfResults = searchRecipe.getTagsOfSearchByName(null);
        assert(tagsOfResults.isEmpty());

        when(persistence.searchByNameAndTags("ca", Arrays.asList("Baking"))).thenReturn(Arrays.asList(recipe1, recipe2));
        when(persistence.searchByNameAndTags("", Arrays.asList("Baking"))).thenReturn(Arrays.asList(recipe1, recipe2, recipe1));
        when(persistence.searchByNameAndTags("", null)).thenReturn(new ArrayList<>());

        List<Recipe> searchByNamePlusTag = searchRecipe.searchByNamePlusTag("ca", Arrays.asList("Baking"));
        assert(searchByNamePlusTag.size() ==2);

        searchByNamePlusTag = searchRecipe.searchByNamePlusTag("", Arrays.asList("Baking"));
        assert(searchByNamePlusTag.size()==3);

        searchByNamePlusTag = searchRecipe.searchByNamePlusTag("", null);
        assert(searchByNamePlusTag.isEmpty());
    }

}
