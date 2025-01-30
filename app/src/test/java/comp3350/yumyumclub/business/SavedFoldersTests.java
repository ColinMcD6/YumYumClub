package comp3350.yumyumclub.business;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import recipebook.business.AccessFolder;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.objects.User;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;


public class SavedFoldersTests {
    AccessFolder accessFolder;
    @Mock
    RecipePersistenceHSQLDB recipePersistence;

    @Mock
    AccessUser accessUser;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        User mockUser = new User("Seyi", "123", "123@123.com");
        when(accessUser.getCurrentUser()).thenReturn(mockUser);
        accessFolder = new AccessFolder(recipePersistence,accessUser);

    }

    @Test
    public void testSavedFolders()
    {
       /* //logging in
        User mockUser = new User("Seyi", "123", "123@123.com");
        when(accessUser.getCurrentUser()).thenReturn(mockUser);
        //when(accessUser.logIn("Seyi","123")).thenReturn(AccessUser.getInstance().logIn("Seyi","123"));
        */

        Recipe recipe1 = new Recipe(1, "Recipe1", Arrays.asList("Step1"), Arrays.asList("Ingredient1"), Arrays.asList("Tag1"), "Seyi", "URL1");
        Recipe recipe2 = new Recipe(2, "Recipe2", Arrays.asList("Step2"), Arrays.asList("Ingredient2"), Arrays.asList("Tag2"), "Seyi", "URL2");

        when(recipePersistence.getSavedFolderNames("Seyi")).thenReturn(Arrays.asList("Dinner Ideas", "Recipes I want to make"));
        when(recipePersistence.getRecipesInSavedFolder("Seyi", "Dinner Ideas")).thenReturn(Arrays.asList(recipe1, recipe2));
        when(recipePersistence.getRecipesInSavedFolder("Seyi", "Recipes I want to make")).thenReturn(Arrays.asList(recipe1));


        //testing accessing existing folders and the values it returns is as expected
        HashMap<String, List<Recipe>> allFolders = accessFolder.getAllFolders();
        assert(allFolders.size()==2);
        assert(allFolders.get("Dinner Ideas").size()==2);
        assert(allFolders.get("Recipes I want to make").size()==1);

        // Mock adding a folder
        doAnswer(invocation -> {
            String folderName = invocation.getArgument(1);
            List<String> updatedFolders = Arrays.asList("Dinner Ideas", "Recipes I want to make", folderName);
            when(recipePersistence.getSavedFolderNames("Seyi")).thenReturn(updatedFolders);
            return null;
        }).when(recipePersistence).addFolder(anyString(), anyString());

        //testing making a new folder

        //invalid input
        accessFolder.addFolder("");
        verify(recipePersistence, never()).addFolder("Seyi", "");

        accessFolder.addFolder(null);
        verify(recipePersistence, never()).addFolder("Seyi", null);

        //valid input
        accessFolder.addFolder("test1");
        verify(recipePersistence).addFolder("Seyi", "test1");

        //testing addRecipeToFolder and getRecipesByFolder
        int validID = allFolders.get("Recipes I want to make").get(0).getID();
        when(recipePersistence.getRecipeByID(validID)).thenReturn(recipe1);
        when(recipePersistence.checkFolderHasRecipe("Seyi",validID,"test1")).thenReturn(false);
        accessFolder.addRecipeToFolder("test1", validID);
        verify(recipePersistence).saveRecipeToFolder("Seyi", validID, "test1");

        when(recipePersistence.getRecipesInSavedFolder("Seyi", "test1")).thenReturn(Arrays.asList(recipe1));
        List<Recipe> recipesInFolder = accessFolder.getRecipesByFolder("test1");
        assertEquals(1, recipesInFolder.size());
        assertEquals(validID, recipesInFolder.get(0).getID());

        // Test addRecipeToFolder with invalid info
        when(recipePersistence.getRecipeByID(8)).thenReturn(null);
        accessFolder.addRecipeToFolder("test1", 8); // recipe ID doesn't exist
        verify(recipePersistence, never()).saveRecipeToFolder("Seyi",8,"test1");

        // Test checkFolderHasRecipe with duplicate recipe
        when(recipePersistence.checkFolderHasRecipe("Seyi", validID, "test1")).thenReturn(true);
        assertTrue(accessFolder.checkFolderHasRecipe("test1", validID));

        accessFolder.addRecipeToFolder("test1", validID);
        verify(recipePersistence, times(1)).saveRecipeToFolder("Seyi", validID, "test1"); //verifying this is the only call

        // Test isFolderNameUnique
        when(recipePersistence.getSavedFolderNames("Seyi")).thenReturn(Arrays.asList("test1"));
        assertFalse(accessFolder.isFolderNameUnique("test1"));
        assertTrue(accessFolder.isFolderNameUnique("test2"));
    }

}
