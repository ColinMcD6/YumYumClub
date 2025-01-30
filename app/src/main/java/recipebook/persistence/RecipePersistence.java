package recipebook.persistence;

import java.util.List;

import recipebook.objects.Recipe;
import recipebook.objects.Review;

public interface RecipePersistence {
    List<Recipe> getAllRecipes();

    Recipe getRecipeByID(int id);

    List<String> getTags();

    //void insertRecipe(Recipe newRecipe);
    List<Recipe> getRecipesByTag(String tag);

    Recipe insertRecipe(String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url);

    List<Recipe> searchRecipeByName(String name);

    List<String> getTagsOfSearchRecipeByName(String name);

    List<Recipe> searchByNameAndTags(String name, List<String> tags);


    List<String> getSavedFolderNames(String username);

    List<Recipe> getRecipesInSavedFolder(String username, String folderName);

    void saveRecipeToFolder(String username, int recipeID, String foldername);

    boolean checkFolderHasRecipe(String username, int recipeID, String foldername);

    void addFolder(String username, String foldername);

    List<Recipe> getRecipesByCreator(String creator);


}
