package comp3350.yumyumclub.business;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.application.Main;
import recipebook.business.AccessReview;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.objects.Review;
import recipebook.objects.User;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.ReviewPersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class ReviewTests {

    @Mock
    private ReviewPersistenceHSQLDB persistence;
    private AccessReview access;

    @Mock
    private RecipePersistence recipePersistence;
    @Mock
    private UserPersistence userPersistence;
    @Mock
    private  AccessUser accessUser;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        access = new AccessReview(persistence, recipePersistence, accessUser);
    }

    @Test
    public void testLogic()
    {
        //average of recipe 1 is 1.5, has 2 ratings
        Review r1 = new Review("Seyi", 1, "Not good :(");
        Review r2 = new Review("Basma", 2, "Bland");
        when(persistence.getReviews(1)).thenReturn(Arrays.asList(r1,r2));

        List<Review> reviews = access.getReviews(1);
        verify(persistence).getReviews(1);
        assert(reviews.size() ==2);

        //testing average function
        when(persistence.getAvgRating(1)).thenReturn(1.5f );
        float average = access.getAvgRating(1);
        verify(persistence).getAvgRating(1);
        assert(average == 1.5);

        //testing getReviews on an empty list
        when(persistence.getReviews(4)).thenReturn(Arrays.asList());
        reviews = access.getReviews(4);
        verify(persistence).getReviews(4);
        assert(reviews.isEmpty());

        //testing inserting a review
        User mockUser = new User("Seyi","123", "123@123.com");
        when(accessUser.getCurrentUser()).thenReturn(mockUser);

        //testing 2 valid cases:
        Recipe recipe1 = new Recipe("Carrot Cake", Arrays.asList("Add flour, baking soda, and salt to a bowl", "In another bowl combine wet ingredients with the sugar, Carrot", "Combine wet and dry ingredients", "Bake at 350 for 30 minutes"),Arrays.asList("Flour", "Egg", "Carrot"), Arrays.asList("Baking", "Vegetarian"), "YumYumBookOriginal" , "carrotcake");
        when(recipePersistence.getRecipeByID(4)).thenReturn(recipe1);
        access.insertReview(4,null,4,"Seyi");
        verify(persistence).insertReview(4,null, 4,"Seyi");

        access.insertReview(4, "Veryy yummy!!", 5, "Seyi");
        verify(persistence).insertReview(4,"Veryy yummy!!", 5,"Seyi");

        //testing invalid cases:
        //testing invalid recipeId
        when(recipePersistence.getRecipeByID(0)).thenReturn(null);
        access.insertReview(0,"Yay", 5, "Seyi");
        verify(persistence, never()).insertReview(0, "Yay", 5, "Seyi");

        //testing invalid rating
        access.insertReview(4,"amazing", 6, "Seyi");
        verify(persistence, never()).insertReview(4, "amazing", 6, "Seyi");

        //testing invalid username
        access.insertReview(4,"amazing", 5, null);
        access.insertReview(4,"amazing", 5, "Collin");
        verify(persistence, never()).insertReview(4, "amazing", 5, null);
        verify(persistence, never()).insertReview(4, "amazing", 5, "Collin");

    }



}
