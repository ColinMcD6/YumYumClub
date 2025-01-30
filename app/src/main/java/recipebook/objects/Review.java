package recipebook.objects;

public class Review {
    private String comment;
    private int rating;
    private String username;
    private int recipeID;

    public Review(String username, int rating, String comment)
    {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    //idk if this is needed
    public Review(String username, int rating, String comment, int recipeID)
    {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

}
