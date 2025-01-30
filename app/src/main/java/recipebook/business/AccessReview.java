package recipebook.business;

import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Review;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.ReviewPersistence;
import recipebook.persistence.UserPersistence;

public class AccessReview implements AccessReviewInterface {
    private ReviewPersistence reviewPersistence;
    private RecipePersistence recipePersistence;
    private AccessUser accessUser;
    public AccessReview()
    {
        this.reviewPersistence = Services.getReviewPersistence();
        this.accessUser = AccessUser.getInstance();
        this.recipePersistence = Services.getRecipePersistence();
    }

    public AccessReview(ReviewPersistence reviewPersistence, RecipePersistence recipePersistence, AccessUser accessUser)
    {
        this.reviewPersistence = reviewPersistence;
        this.recipePersistence = recipePersistence;
        this.accessUser = accessUser;
    }

    @Override
    public List<Review> getReviews(int recipeID) {
        return reviewPersistence.getReviews(recipeID);
    }

    @Override
    public float getAvgRating(int recipeID)
    {
        return reviewPersistence.getAvgRating(recipeID);
    }

    public void insertReview(int recipeID, String comment, int rating, String username)
    {
        if(verifyReview(recipeID,comment,rating,username))
            reviewPersistence.insertReview(recipeID,comment,rating,username);
    }

    private boolean verifyReview(int recipeID, String comment, int rating, String username)
    {
        boolean result = true;
        if(recipePersistence.getRecipeByID(recipeID)==null)
        {
            result = false;
        }
        if(comment!=null && comment.length()>1000 )
        {
            result = false;
        }
        if(rating<1 || rating>5 )
        {
            result = false;
        }
        if(!accessUser.getCurrentUser().getUserName().equals(username))
        {
            result = false;
        }
        if(username ==null || username.length()>50 || username.isEmpty())
        {
            result = false;
        }

        return result;
    }
}
