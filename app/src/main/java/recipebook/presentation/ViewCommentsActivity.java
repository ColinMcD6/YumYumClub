package recipebook.presentation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import comp3350.yumyumclub.R;
import recipebook.application.Services;
import recipebook.business.AccessReviewInterface;
import recipebook.objects.Review;

public class ViewCommentsActivity extends AppCompatActivity {
    private int recipeID;
    private FloatingActionButton addCommentFAB;
    private AccessReviewInterface accessReview;
    private RecyclerView reviewRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private int dynamicColumns;
    private TextView avgRating;
    private RatingBar avgStars;
    private LinearLayout reviewsSummary;

    public ViewCommentsActivity() { accessReview = Services.getAccessReview(); }
    @Override
    protected void onCreate(Bundle savedInstanceData) {
        super.onCreate(savedInstanceData);
        setContentView(R.layout.activity_view_comments);

        // Remove top bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        calculateRowNumber();

        recipeID = getIntent().getIntExtra("ID", -1);
        String recipeName = getIntent().getStringExtra("Name");
        List<Review> reviews = accessReview.getReviews(recipeID);
        if (recipeName != null)
        {
            TextView reviewTitle = findViewById(R.id.review_section_title);
            reviewTitle.setText("Reviews for " + recipeName);
        }

        avgRating = findViewById(R.id.avg_rating);
        avgStars = findViewById(R.id.avg_stars);
        reviewsSummary = findViewById(R.id.summary);

        if (reviews.size() > 0) {
            float avg = accessReview.getAvgRating(recipeID);
            avgRating.setText(String.format("%.1f (%d)", avg, reviews.size()));
            avgStars.setRating(avg);
        }
        else
        {
            reviewsSummary.setVisibility(View.GONE);
        }


        reviewRecyclerView = findViewById(R.id.review_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, dynamicColumns);
        reviewsAdapter = new ReviewsAdapter(reviews);
        reviewRecyclerView.setAdapter(reviewsAdapter);
        reviewRecyclerView.setLayoutManager(gridLayoutManager);

        addCommentFAB = findViewById(R.id.add_comment);
        addCommentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { createReviewDialog(); }
        });
    }

    public void recipeOnClick(View view) {
        finish();
    }

    private void createReviewDialog() {

        // Create the dialog, Inflate the dialog layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_review, null);

        builder.setTitle("Add Review");
        builder.setView(dialogView);

        // Set up the EditText and Button inside the dialog
        EditText editTextComment = dialogView.findViewById(R.id.edit_review);
        Button buttonSubmit = dialogView.findViewById(R.id.create_review_btn);
        Button cancel = dialogView.findViewById(R.id.cancel_btn);
        RatingBar rating = dialogView.findViewById(R.id.review_rating);

        // Add TextWatcher to the EditText
        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable/disable button based on text input
                String comment = s.toString().trim();
                buttonSubmit.setEnabled(!comment.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });

        AlertDialog dialog = builder.create();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextComment.getText().toString().trim();
                int star_rating = (int) rating.getRating();
                if (!comment.isEmpty()) {
                    if (reviewsSummary.getVisibility() == View.GONE)
                    {
                        reviewsSummary.setVisibility(View.VISIBLE);
                    }
                    accessReview.insertReview(recipeID, comment, star_rating, Services.getUserPersistence().getCurrentUser().getUserName());
                    dialog.dismiss();
                    reviewsAdapter.addReview(new Review(Services.getUserPersistence().getCurrentUser().getUserName(), star_rating, comment));
                    float avg = accessReview.getAvgRating(recipeID);
                    avgRating.setText(String.format("%.1f (%d)", avg, reviewsAdapter.getItemCount()));
                    avgStars.setRating(avg);

                    String msg = "You submitted a review of " + star_rating + " stars";
                    Toast.makeText(ViewCommentsActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void calculateRowNumber(){
        int displayWidth = getDisplayWidthDp();
        dynamicColumns = displayWidth < 400 ? 1 : displayWidth/400;
    }

    private int getDisplayWidthDp(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        return Math.round(widthPixels / density);
    }
}
