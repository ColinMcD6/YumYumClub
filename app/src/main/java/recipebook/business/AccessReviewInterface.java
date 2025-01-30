package recipebook.business;

import java.util.List;

import recipebook.objects.Review;

public interface AccessReviewInterface {
    List<Review> getReviews(int recipeID);

    public void insertReview(int recipeID, String comment, int rating, String username);

    float getAvgRating(int recipeID);
}
