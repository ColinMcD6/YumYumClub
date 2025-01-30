package comp3350.yumyumclub.business;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import comp3350.yumyumclub.utils.TestUtils;
import recipebook.application.Main;
import recipebook.business.AccessReview;
import recipebook.business.AccessUser;
import recipebook.objects.Recipe;
import recipebook.objects.Review;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.UserPersistence;
import recipebook.persistence.hsqldb.RecipePersistenceHSQLDB;
import recipebook.persistence.hsqldb.ReviewPersistenceHSQLDB;
import recipebook.persistence.hsqldb.UserPersistenceHSQLDB;

public class ReviewsIT {
    private File tempDB;
    private ReviewPersistenceHSQLDB persistence;
    private AccessReview access;

    private RecipePersistence recipePersistence;
    private UserPersistence userPersistence;
    private  AccessUser accessUser;

    @Before
    public void setUp() throws IOException {
        tempDB = TestUtils.copyDB();
        persistence = new ReviewPersistenceHSQLDB(Main.getDBPathName());
        recipePersistence = new RecipePersistenceHSQLDB(Main.getDBPathName());
        userPersistence = new UserPersistenceHSQLDB(Main.getDBPathName());
        accessUser = new AccessUser(userPersistence);

        access = new AccessReview(persistence, recipePersistence,accessUser);
    }

    @Test
    public void testLogic()
    {
        //average rating of recipe with ID 1 is 1.5, and has 2 ratings
        List<Review> reviews = access.getReviews(1);
        assert(reviews.size() ==2);

        //testing average function
        float average = access.getAvgRating(1);
        assert(average == 1.5);

        //testing getReviews on an empty list
        reviews = access.getReviews(4);
        assert(reviews.isEmpty());

        //testing inserting a review
        accessUser.logIn("Seyi", "123");
        //testing 2 valid cases:
        access.insertReview(4,null,4,"Seyi");
        access.insertReview(4, "Veryy yummy!!", 5, "Seyi");

        reviews = access.getReviews(4);
        assert(reviews.size()==2);
    }

    @After
    public void tearDown()
    {
        tempDB.delete();
    }


}

