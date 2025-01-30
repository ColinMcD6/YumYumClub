package comp3350.yumyumclub.business;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        LogInIT.class,
        AcessRecipeIT.class,
        CreateRecipeIT.class,
        SavedFolderIT.class,
        SearchIT.class,
        ShoppingCartIT.class,
        ReviewsIT.class
})

public class AllIntegrationTests {

}
