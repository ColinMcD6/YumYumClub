package recipebook.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;
public class Recipe implements Serializable
{
    private String name;
    private List<String> steps;
    private List<String> ingredients;
    private List<String> tags;
    private String creatorId;
    private int ID;
    private String image_URL;

    private static int id_counter = 0;

    //is used for the persistencestub class
    public Recipe(String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url)
    {
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
        this.tags = tags;
        this.creatorId = creatorId;
        this.ID = id_counter;
        this.image_URL = url;
        id_counter++;
    }

    //is used for the hsqldb version
    public Recipe(int id, String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url)
    {
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients;
        this.tags = tags;
        this.creatorId = creatorId;
        this.ID = id;
        this.image_URL = url;
    }

    public void updateRecipe(String name, List<String> steps, List<String> ingredients, List<String> tags, String image_URL)
    {
        if(name != null)
            this.name = name;
        if(steps != null)
            this.steps = steps;
        if(ingredients != null)
            this.ingredients = ingredients;
        if(tags != null)
            this.tags = tags;
        if( image_URL != null)
            this.image_URL = image_URL;
    }

    public boolean containsTag(String tag)
    {
        boolean found = false;
        for(String t:tags)
        {
            if(t.equals(tag))
            {
                found = true;
                break;
            }
        }

        return found;
    }

    public String getName()
    {
        return name;
    }

    public List<String> getSteps()
    {
        return steps;
    }

    public List<String> getIngredients()
    {
        return ingredients;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public String getCreatorId()
    {
        return creatorId;
    }

    public String getImage_URL()
    {
        return image_URL;
    }

    public int getID() { return ID; }

}