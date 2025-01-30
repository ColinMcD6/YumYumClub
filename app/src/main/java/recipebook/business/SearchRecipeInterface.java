package recipebook.business;

import java.util.List;

import recipebook.objects.Recipe;

public interface SearchRecipeInterface {
    List<Recipe> searchByName(String input);

    List<String> getTagsOfSearchByName(String input);

    List<Recipe> searchByNamePlusTag(String input, List<String> tags);
}
