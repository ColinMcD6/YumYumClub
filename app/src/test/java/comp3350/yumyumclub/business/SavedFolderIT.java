package comp3350.yumyumclub.business;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.business.AccessFolder;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class SavedFolderIT {
    RecipePersistence recipePersistence;

    AccessFolder accessFolder;
    AccessUser accessUser;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        UserPersistence persistence = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.accessUser = new AccessUser(persistence);
        this.recipePersistence = new RecipePersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.accessFolder = new AccessFolder(recipePersistence,accessUser);
    }

    @Test
    public void testSavedFolders(){
        //logging in
        accessUser.logIn("Seyi","123");

        //testing accessing existing folders and the values it returns is as expected
        HashMap<String, List<Recipe>> allFolders = accessFolder.getAllFolders();
        assert(allFolders.size()==2);
        assert(allFolders.get("Dinner Ideas").size()==2);
        assert(allFolders.get("Recipes I want to make").size()==1);

        //testing adding a folder
        accessFolder.addFolder("test1");
        allFolders = accessFolder.getAllFolders();
        assert(allFolders.size()==3);
        assert(allFolders.get("test1")!=null); //asserting the folder has been added

        //testing adding a recipe to a folder
        int validID = allFolders.get("Recipes I want to make").get(0).getID();
        accessFolder.addRecipeToFolder("test1", validID);

        List<Recipe> recipesInFolder = accessFolder.getRecipesByFolder("test1");
        assert(recipesInFolder.size()==1);
        assert(recipesInFolder.get(0).getID()==validID);

        //testing checkFolderHasRecipe
        assert(accessFolder.checkFolderHasRecipe("test1",validID));
        accessFolder.addRecipeToFolder("test1",validID);
        assert(recipesInFolder.size()==1);
        assert(recipesInFolder.get(0).getID()==validID);

        //tesing isFolderNameUnique
        assert(!accessFolder.isFolderNameUnique("test1"));
        assert(accessFolder.isFolderNameUnique("test2"));
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }
    
}
