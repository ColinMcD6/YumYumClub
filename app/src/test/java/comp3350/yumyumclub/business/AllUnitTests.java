package comp3350.yumyumclub.business;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
        AccessRecipeTests.class,
        LogInUnitTest.class,
        CreateRecipeTests.class,
        SavedFoldersTests.class,
        SearchTests.class,
        ShoppingCartTest.class,
        ReviewTests.class
        }
)
public class AllUnitTests
{

}
