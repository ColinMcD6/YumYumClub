package recipebook.business;

import java.util.List;

import recipebook.objects.Recipe;

public interface AccessRecipeInterface
{

    /*
    returns a list of recipes that are associated with the inputted tag
     */
     List<Recipe> getRecipesByTag(String tag);

     List<Recipe> getAllRecipes();

    List<String> getTags();

     Recipe getRecipeByID(int ID);

     List<Recipe> getCurrentCreatorRecipes();
}
