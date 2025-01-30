package comp3350.yumyumclub.business;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import recipebook.business.AccessRecipe;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.objects.User;
import recipebook.persistence.RecipePersistence;



public class AccessRecipeTests
{

    AccessRecipe accessRecipe;
    @Mock
    RecipePersistence persistence;
    @Mock
    AccessUser user;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.accessRecipe = new AccessRecipe(persistence);
    }


    @Test
    public void testRecipe()
    {
        // Mock data
        Recipe mockRecipe = new Recipe(1, "Mock Recipe",
                Arrays.asList("Step 1", "Step 2"),
                Arrays.asList("Ingredient 1", "Ingredient 2"),
                Arrays.asList("tag1", "tag2"),
                "creator1", "url");
        List<Recipe> mockRecipes = Arrays.asList(mockRecipe);
        List<String> mockTags = Arrays.asList("tag1", "tag2");

        // Define mock behavior
        when(persistence.getRecipeByID(1)).thenReturn(mockRecipe);
        when(persistence.getRecipesByTag("tag1")).thenReturn(mockRecipes);
        when(persistence.getRecipesByTag("tag2")).thenReturn(mockRecipes);
        when(persistence.getRecipesByTag("bad food")).thenReturn(Collections.emptyList());
        when(persistence.getTags()).thenReturn(mockTags);
        when(persistence.getAllRecipes()).thenReturn(Arrays.asList(mockRecipe));

        // Test getRecipeByID
        Recipe r1 = accessRecipe.getRecipeByID(1);
        assertNotNull(r1);
        assertEquals(1, r1.getID());

        // Test getRecipesByTag
        for (String tag : accessRecipe.getTags()) {
            for (Recipe recipe : accessRecipe.getRecipesByTag(tag)) {
                assertTrue(recipe.containsTag(tag));
            }
        }

        // Test getRecipesByTag with invalid data
        List<Recipe> r2 = accessRecipe.getRecipesByTag("bad food");
        assertNotNull(r2);
        assertTrue(r2.isEmpty());

        //Test getAllRecipes
        List<Recipe> allRecipes = accessRecipe.getAllRecipes();
        verify(persistence).getAllRecipes();
        assert(allRecipes.size()==1);

        // Verify interactions with the mock
        verify(persistence, times(1)).getRecipeByID(1);
        verify(persistence, times(1)).getRecipesByTag("tag1");
        verify(persistence, times(1)).getRecipesByTag("tag2");
        verify(persistence, times(1)).getRecipesByTag("bad food");
        verify(persistence, times(1)).getTags();

        // Test getCurrentCreatorRecipes()
        // Mock current user
        User mockUser = new User("Seyi", "123", "123@123.com");
        when(user.getCurrentUser()).thenReturn(mockUser);
    }


}
