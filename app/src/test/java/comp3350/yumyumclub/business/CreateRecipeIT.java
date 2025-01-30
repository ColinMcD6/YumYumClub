package comp3350.yumyumclub.business;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;


import comp3350.yumyumclub.utils.TestUtils;
import recipebook.business.AccessRecipe;
import recipebook.business.AccessUser;
import recipebook.business.CreateRecipe;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;


public class CreateRecipeIT {
    CreateRecipe createRecipe;
    AccessRecipe accessRecipe;
    AccessUser accessUser;
    private File tempDB;

    @Before
    public void setup() throws IOException {
        this.tempDB = TestUtils.copyDB();
        RecipePersistence persistence = new RecipePersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        UserPersistence userPersistence = new UserPersistenceHSQLDB(tempDB.getAbsolutePath().replace(".script", ""));
        this.accessUser = new AccessUser(userPersistence);
        this.accessRecipe= new AccessRecipe(persistence);
        this.createRecipe = new CreateRecipe(persistence);
    }

    @Test
    public void testCreateRecipe()
    {
        Recipe r1 = createRecipe.createRecipe("Macaroni and Cheese", Arrays.asList("Boil pasta", "Mix with cheese", "Serve!"), Arrays.asList("pasta", "cheese"),Arrays.asList("Cooking"), "username1", "brownies");
        assert (r1!=null);

        Recipe r2 = accessRecipe.getRecipeByID(r1.getID());
        assert(r2!=null);
        assert(r2.getName().equals(r1.getName()));
    }

    @After
    public void tearDown() {
        this.tempDB.delete();
    }

}
