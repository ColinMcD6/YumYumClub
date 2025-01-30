package recipebook.business;

import java.util.List;

import recipebook.objects.Recipe;

public interface CreateRecipeInterface
{
    Recipe createRecipe( String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url );

    String validInput(String name, List<String> steps, List<String> ingredients, List<String> tags);
}
