package recipebook.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import comp3350.yumyumclub.R;
import recipebook.objects.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private List<Review> reviews;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameView;
        private TextView commentView;
        private RatingBar rating;
        public ViewHolder(View view) {
            super(view);
            usernameView = view.findViewById(R.id.review_username);
            commentView = view.findViewById(R.id.comment);
            rating = view.findViewById(R.id.ratingBar);
        }

        public TextView getUsernameView() { return usernameView; }
        public TextView getCommentView() { return commentView; }
        public RatingBar getRating() { return rating; }
    }

    public ReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder viewHolder, final int position)
    {
        Review review = reviews.get(position);
        String uniqueContentDescription = String.format("%s%s%d", review.getUsername(), review.getComment(), review.getRating());
        viewHolder.getUsernameView().setText(review.getUsername());
        viewHolder.getUsernameView().setContentDescription(String.format("USERNAME:%s", uniqueContentDescription));
        viewHolder.getCommentView().setText(review.getComment());
        viewHolder.getCommentView().setContentDescription(String.format("COMMENT:%s", uniqueContentDescription));
        viewHolder.getRating().setRating(review.getRating());
        viewHolder.getRating().setContentDescription(String.format("RATING:%s", uniqueContentDescription));
    }

    public int getItemCount() { return reviews.size(); }

    public void addReview(Review review) {
        reviews.add(review);
        notifyItemInserted(reviews.size()-1);
    }
}

