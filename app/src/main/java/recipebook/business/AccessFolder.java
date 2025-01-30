package recipebook.business;

import java.util.HashMap;
import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;

public class AccessFolder implements AccessFolderInterface{
    private RecipePersistence persistence;
    private AccessUser accessUser;

    public AccessFolder()
    {
        this.persistence = Services.getRecipePersistence();
        this.accessUser = AccessUser.getInstance();
    }

    public AccessFolder(RecipePersistence persistence, AccessUser accessUser)
    {
        this.persistence = persistence;
        this.accessUser = accessUser;
    }
    @Override
    public boolean checkFolderHasRecipe(String folderName, int recipeID) {
        if(folderName!=null && recipeID>0)
            return persistence.checkFolderHasRecipe(accessUser.getCurrentUser().getUserName(),recipeID,folderName);
        else
            return false;
    }

    @Override
    public void addRecipeToFolder(String folderName, int recipe) {
        //step 1: check if the recipe id exists
        if(persistence.getRecipeByID(recipe)!=null && folderName!=null && !folderName.isEmpty() && !checkFolderHasRecipe(folderName,recipe))
        {
            persistence.saveRecipeToFolder(accessUser.getCurrentUser().getUserName(), recipe, folderName);
        }

    }

    @Override
    public boolean isFolderNameUnique(String folderName) {
        boolean result = false;
        if( folderName!=null && !persistence.getSavedFolderNames( accessUser.getCurrentUser().getUserName() ).contains(folderName) )
            result = true;
        return result;
    }

    @Override
    public void addFolder(String folderName) {
        //step 1: assert that the folder doesnt already exist
        if(!persistence.getSavedFolderNames(accessUser.getCurrentUser().getUserName()).contains(folderName))
        {
            if(folderName!=null && !folderName.isEmpty())
            {
                persistence.addFolder(accessUser.getCurrentUser().getUserName(), folderName);
            }
        }

    }

    @Override
    public HashMap<String, List<Recipe>> getAllFolders() {
        HashMap<String, List<Recipe>> result = new HashMap<String, List<Recipe>>();
        String currentUser = accessUser.getCurrentUser().getUserName();
        List<String> folderNames = persistence.getSavedFolderNames(currentUser);
        for(String f:folderNames)
        {
            List<Recipe> folderContent = persistence.getRecipesInSavedFolder(currentUser,f);
            result.put(f,folderContent);
        }

        return result;
    }

    @Override
    public List<Recipe> getRecipesByFolder(String folderName) {
        return persistence.getRecipesInSavedFolder(accessUser.getCurrentUser().getUserName(), folderName);
    }
}
