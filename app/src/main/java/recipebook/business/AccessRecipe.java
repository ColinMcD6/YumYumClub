package recipebook.business;

//imports

import java.util.ArrayList;
import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;

public class AccessRecipe implements AccessRecipeInterface
{
    private RecipePersistence persistence;

    public AccessRecipe()
    {
        this.persistence = Services.getRecipePersistence();
    }

    public AccessRecipe(RecipePersistence persistence)
    {
        this.persistence = persistence;
    }

    @Override
    public List<Recipe> getRecipesByTag(String tag)
    {
        return persistence.getRecipesByTag(tag);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return persistence.getAllRecipes();
    }

    @Override
    public List<String> getTags() {
        return persistence.getTags();
    }

    @Override
    public Recipe getRecipeByID(int ID) {
        return persistence.getRecipeByID(ID);
    }

    @Override
    public List<Recipe> getCurrentCreatorRecipes() {
        String username = AccessUser.getInstance().getCurrentUser().getUserName();
        if(username!=null)
            return  persistence.getRecipesByCreator(username);
        else
            return new ArrayList<Recipe>();
    }

}
