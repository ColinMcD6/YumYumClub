package recipebook.business;

import java.util.ArrayList;
import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;

public class SearchRecipes implements SearchRecipeInterface{

    private RecipePersistence persistence;

    public SearchRecipes()
    {
        this.persistence = Services.getRecipePersistence();
    }
    @Override
    public List<Recipe> searchByName(String input) {
        if(input!=null)
            return persistence.searchRecipeByName(input);
        else
            return new ArrayList<Recipe>();
    }

    public SearchRecipes(RecipePersistence persistence)
    {
        this.persistence = persistence;
    }

    @Override
    public List<String> getTagsOfSearchByName(String input) {
        if(input!=null)
        {
            return persistence.getTagsOfSearchRecipeByName(input);
        }else{
            return new ArrayList<String>();
        }
    }

    @Override
    public List<Recipe> searchByNamePlusTag(String input, List<String> tags) {
        if(tags!= null && input != null)
        {
            return persistence.searchByNameAndTags(input, tags);
        }else{
            return new ArrayList<Recipe>();
        }

    }
}
