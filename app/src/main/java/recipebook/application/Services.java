package recipebook.application;

import recipebook.business.AccessReview;
import recipebook.business.AccessFolder;
import recipebook.business.AccessRecipe;
import recipebook.business.CreateRecipe;
import recipebook.business.SearchRecipes;
import recipebook.business.ShoppingList;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.ReviewPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.ReviewPersistenceHSQLDB;
import recipebook.persistence.ShoppingCartPersistence;

import recipebook.persistence.hsqldb.ShoppingCartPersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;
import recipebook.persistence.UserPersistence;

public class Services {
    
    private static RecipePersistence recipePersistence = null;

    private static AccessRecipe accessRecipe = null;

    private static AccessReview accessComment = null;

    private static UserPersistence userPersistence = null;

    private static CreateRecipe createRecipe = null;

    private static AccessFolder accessFolder = null;

    private static ReviewPersistence reviewPersistence = null;

    private static ShoppingList shoppingList = null;

    private static ShoppingCartPersistence shoppingCartPersistence = null;

    private static SearchRecipes searchRecipes;

    public static synchronized RecipePersistence getRecipePersistence() {
        if (recipePersistence == null)
        {
            recipePersistence = new RecipePersistenceHSQLDB(Main.getDBPathName());
        }

        return recipePersistence;
    }

    public static synchronized ReviewPersistence getReviewPersistence()
    {
        if(reviewPersistence ==null)
        {
            reviewPersistence = new ReviewPersistenceHSQLDB(Main.getDBPathName());
        }

        return reviewPersistence;
    }

    public static synchronized AccessRecipe getAccessRecipe()
    {
        if (accessRecipe == null)
        {
            accessRecipe = new AccessRecipe();
        }

        return accessRecipe;
    }

    public static synchronized AccessReview getAccessReview()
    {
        if (accessComment == null)
        {
            accessComment = new AccessReview();
        }

        return accessComment;
    }

    public static synchronized CreateRecipe getCreateRecipe()
    {
        if (createRecipe == null)
        {
           createRecipe = new CreateRecipe();
        }

        return createRecipe;
    }

    public static synchronized AccessFolder getAccessFolder()
    {
        if (accessFolder == null)
        {
            accessFolder = new AccessFolder();
        }

        return accessFolder;
    }

    public static synchronized UserPersistence getUserPersistence(){
        if (userPersistence == null)
        {
            userPersistence = new UserPersistenceHSQLDB(Main.getDBPathName());
        }

        return userPersistence;
    }

    public static synchronized ShoppingList getShoppingList()
    {
        if (shoppingList == null)
        {
            shoppingList = new ShoppingList();
        }

        return shoppingList;
    }


    public static synchronized ShoppingCartPersistence getShoppingCartPersistence(){
        if (shoppingCartPersistence == null)
        {
            shoppingCartPersistence = new ShoppingCartPersistenceHSQLDB(Main.getDBPathName());
        }

        return shoppingCartPersistence;
    }

    public static synchronized SearchRecipes getSearchRecipes()
    {
        if(searchRecipes ==null)
        {
            searchRecipes = new SearchRecipes();
        }

        return searchRecipes;
    }



}
