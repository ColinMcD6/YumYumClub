package recipebook.business;

import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;

public class CreateRecipe implements CreateRecipeInterface
{
    private RecipePersistence persistence;
    public CreateRecipe()
    {
        this.persistence = Services.getRecipePersistence();
    }

    public CreateRecipe(RecipePersistence persistence)
    {
        this.persistence = persistence;
    }

    @Override
    public Recipe createRecipe( String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url )
    {
        Recipe newRecipe = null;
        String defaultURL = "cooking_pot";
        String isSuccess = validInput( name, steps, ingredients, tags );
        if ( isSuccess.equals("Success!") )
        {
            newRecipe = persistence.insertRecipe( name, steps, ingredients, tags, creatorId, defaultURL);
        }
        return newRecipe;
    }
    @Override
    public String validInput(String name, List<String> steps, List<String> ingredients, List<String> tags)
    {
        String returnMsg = "";

        if ( !validRecipeTitle( name ) )
            returnMsg += "Recipe title is missing \n";
        if ( !validSteps( steps ) )
            returnMsg += "List of steps is missing \n";
        if ( !validIngredients( ingredients ) )
            returnMsg += "List of ingredients is missing \n";
        if ( tags == null || tags.isEmpty() )
            returnMsg += "List of tags is missing \n";
        else if ( !validateTags( tags ) )
            returnMsg += "List of tags is not valid \n";
        if ( returnMsg.isEmpty() )
            returnMsg = "Success!";

        return returnMsg;
    }


    private boolean validRecipeTitle( String title )
    {
        return title != null && !title.isEmpty();
    }
    private boolean validIngredients(List<String> ingredients)
    {
        boolean result = true;
        if ( ingredients.size() == 1 && ingredients.get(0).isEmpty() )
        {
            result = false;
        }
        if ( ingredients == null || ingredients.isEmpty() )
        {
            result = false;
        }
        return result;
    }

    private boolean validSteps(List<String> steps)
    {
        boolean result = true;
        if ( steps.size() == 1 && steps.get(0).isEmpty() )
        {
            result = false;
        }
        if ( steps == null || steps.isEmpty() )
        {
            result = false;
        }
        return result;
    }

    //checks if the tags are in the list of accepted tags
    private boolean validateTags( List<String> tags )
    {
        boolean valid = true;
        if ( tags != null && !tags.isEmpty() ) {
            for ( String tag : tags )
            {
                if ( !persistence.getTags().contains(tag) )
                    valid = false;
            }
        }
        else
        {
            valid = false;
        }

        return valid;
    }
}
