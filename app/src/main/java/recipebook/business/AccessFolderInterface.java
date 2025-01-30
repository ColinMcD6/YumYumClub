package recipebook.business;

import java.util.HashMap;
import java.util.List;

import recipebook.objects.Recipe;

public interface AccessFolderInterface {

    // Check if the folder has specific recipes
    boolean checkFolderHasRecipe(String folderName, int recipeID);

    // Add recipe to a folder
    void addRecipeToFolder(String folderName, int recipe);

    // check if folder name is unique (among user saved folders)
    boolean isFolderNameUnique(String folderName);

    // Add folder
    void addFolder(String folderName);

    // return all folder user has. In the case of empty folder, return with List<Recipe> with size 0; not null;
    HashMap<String, List<Recipe>> getAllFolders();

    // return specific folder. In the case of empty folder, return with List<Recipe> with size 0; not null;
    List<Recipe> getRecipesByFolder(String folderName);
}
