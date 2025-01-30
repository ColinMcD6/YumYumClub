package recipebook.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import recipebook.objects.Review;
import recipebook.persistence.ReviewPersistence;

public class ReviewPersistenceHSQLDB implements ReviewPersistence {

    private final String dbPath;

    public ReviewPersistenceHSQLDB(String dbPath)
    {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private Review fromResultSet(ResultSet rs) throws SQLException
    {
        String username = rs.getString("UserName");
        String comment = rs.getString("Comment");
        int rating = rs.getInt("Rating");
        //int recipeID = rs.getInt("RecipeID");

        return new Review(username,rating,comment);
    }

    @Override
    public void insertReview(int recipeID, String comment, int rating, String username) {
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("INSERT INTO Reviews VALUES(?, ?, ?, ?)");
            st.setString(1,username);
            st.setInt(2,recipeID);
            st.setString(3,comment);
            st.setInt(4,rating);

            st.executeUpdate();

        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }

    }

    @Override
    public List<Review> getReviews(int recipeID) {
        List<Review> result = new ArrayList<Review>();
        try( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM Reviews WHERE RECIPEID = ?");
            st.setInt(1,recipeID);
            final ResultSet rs = st.executeQuery();

            while ( rs.next() )
            {
                Review review = fromResultSet(rs);
                result.add(review);
            }

            rs.close();
            st.close();

            return result;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public float getAvgRating(int recipeID)
    {
        float result = 0;
        try( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT SUM(Rating) as TotalRating, COUNT(Rating) AS NumReviews FROM Reviews WHERE RECIPEID = ?");
            st.setInt(1,recipeID);
            final ResultSet rs = st.executeQuery();

            if(rs.next())
            {
                int sum = rs.getInt("TotalRating");
                int denom = rs.getInt("NumReviews");
                result = sum/ (float)denom;
            }

            rs.close();
            st.close();

            return result;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }
}
