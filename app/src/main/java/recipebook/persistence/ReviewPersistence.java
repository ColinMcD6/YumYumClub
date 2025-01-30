package recipebook.persistence;

import java.util.List;

import recipebook.objects.Review;

public interface ReviewPersistence {

    void insertReview(int recipeID, String comment, int rating, String username);

    List<Review> getReviews(int recipeID);

    float getAvgRating(int recipeID);
}
