package comp3350.yumyumclub.business;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;



import java.util.Arrays;
import java.util.List;


import recipebook.business.AccessRecipe;
import recipebook.business.AccessUser;
import recipebook.business.CreateRecipe;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;


public class CreateRecipeTests {

    CreateRecipe createRecipe;

    //@Mock
    AccessRecipe accessRecipe;
    @Mock
    RecipePersistence persistence;
    @Mock
    AccessUser accessUser;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createRecipe = new CreateRecipe(persistence);
        accessRecipe = new AccessRecipe(persistence);
    }

    @Test
    public void testCreateRecipe()
    {
        Recipe mockRecipe = new Recipe(1, "Macaroni and Cheese", Arrays.asList("Boil pasta", "Mix with cheese", "Serve!"), Arrays.asList("pasta", "cheese"), Arrays.asList("Cooking"), "username1", "brownies");
        when(persistence.insertRecipe(anyString(), anyList(), anyList(), anyList(), anyString(), anyString())).thenReturn(mockRecipe);
        when(persistence.getTags()).thenReturn(Arrays.asList("Cooking", "Baking", "Vegetarian"));

        // Act
        Recipe r1 = createRecipe.createRecipe("Macaroni and Cheese", Arrays.asList("Boil pasta", "Mix with cheese", "Serve!"), Arrays.asList("pasta", "cheese"), Arrays.asList("Cooking"), "username1", "brownies");
        String s1 = createRecipe.validInput("Macaroni and Cheese", Arrays.asList("Boil pasta", "Mix with cheese", "Serve!"), Arrays.asList("pasta", "cheese"), Arrays.asList("Cooking"));

        when(persistence.getRecipeByID(r1.getID())).thenReturn(r1);
        Recipe r2 = accessRecipe.getRecipeByID(r1.getID());


        // Assert
        assertNotNull(r1);
        assertEquals("Macaroni and Cheese", r1.getName());
        assertNotNull(r2);
        assertEquals(r1.getName(), r2.getName());
        assertEquals("Success!", s1);

        verify(persistence, times(1)).insertRecipe("Macaroni and Cheese", Arrays.asList("Boil pasta", "Mix with cheese", "Serve!"), Arrays.asList("pasta", "cheese"), Arrays.asList("Cooking"), "username1", "cooking_pot");

        // invalid input tests

        String valid_name = "Macaroni and Cheese";
        List<String> valid_steps =  Arrays.asList("Boil pasta", "Mix with cheese", "Serve!");
        List<String> valid_ingredients = Arrays.asList("pasta", "cheese");
        List<String> valid_tags = Arrays.asList("Cooking");
        String valid_creatorid = "username1";
        String valid_url = "brownies";

        //testing invalid tag
        List<String> invalid_tags = Arrays.asList("Macaroni");
        createRecipe.createRecipe(valid_name,valid_steps,valid_ingredients, invalid_tags,valid_creatorid,valid_url);
        verify(persistence, never()).insertRecipe(valid_name,valid_steps,valid_ingredients, invalid_tags,valid_creatorid,valid_url);

        String r3 = createRecipe.validInput(valid_name,valid_steps,valid_ingredients,invalid_tags);
        assertEquals("List of tags is not valid \n", r3);

        // Test no tags
        invalid_tags = Arrays.asList();
        createRecipe.createRecipe(valid_name,valid_steps,valid_ingredients, invalid_tags,valid_creatorid,valid_url);
        verify(persistence, never()).insertRecipe(valid_name,valid_steps,valid_ingredients, invalid_tags,valid_creatorid,valid_url);

        String r5 = createRecipe.validInput(valid_name,valid_steps,valid_ingredients,invalid_tags);
        assertEquals("List of tags is missing \n", r5);

        // Test missing title
        createRecipe.createRecipe("",valid_steps,valid_ingredients, valid_tags, valid_creatorid,valid_url);
        verify(persistence, never()).insertRecipe("",valid_steps,valid_ingredients, valid_tags, valid_creatorid,valid_url);

        String r6 = createRecipe.validInput("",valid_steps,valid_ingredients, valid_tags);
        assertEquals("Recipe title is missing \n", r6);

        // Test no ingredients
        List<String> invalid_ingredients = Arrays.asList();
        createRecipe.createRecipe(valid_name,valid_steps,invalid_ingredients, valid_tags, valid_creatorid,valid_url);
        verify(persistence, never()).insertRecipe(valid_name,valid_steps,invalid_ingredients, valid_tags, valid_creatorid,valid_url);

        String r7 = createRecipe.validInput(valid_name,valid_steps,invalid_ingredients, valid_tags);
        assertEquals("List of ingredients is missing \n", r7);

        // Test no steps
        List<String> invalid_steps = Arrays.asList();
        createRecipe.createRecipe(valid_name,invalid_steps,valid_ingredients, valid_tags, valid_creatorid,valid_url);
        verify(persistence, never()).insertRecipe(valid_name,invalid_steps,valid_ingredients, valid_tags, valid_creatorid,valid_url);

        String r8 = createRecipe.validInput(valid_name,invalid_steps,valid_ingredients, valid_tags);
        assertEquals("List of steps is missing \n", r8);
    }


}
