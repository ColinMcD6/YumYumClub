package recipebook.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import recipebook.application.Services;
import recipebook.objects.Recipe;
import recipebook.persistence.RecipePersistence;
import recipebook.persistence.ShoppingCartPersistence;

public class ShoppingCartPersistenceHSQLDB implements ShoppingCartPersistence {

    private final String dbPath;
    private RecipePersistence recipePersistence;
    public ShoppingCartPersistenceHSQLDB(String dbPath)
    {
        this.dbPath = dbPath;
        this.recipePersistence = Services.getRecipePersistence();
    }

    private Connection connection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public List<Recipe> getShoppingCart(String username)
    {
        List<Recipe> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT DISTINCT RecipeID FROM ShoppingCart WHERE Username = ?");
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();

            while(rs.next())
            {
                int recipeId = rs.getInt("RecipeID");
                Recipe recipe = recipePersistence.getRecipeByID(recipeId);
                result.add(recipe);
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
    public int getNumOfRecipeInShoppingCart(String username, int recipeId)
    {
        int result = 0;
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT count(*) AS NumAdded FROM ShoppingCart WHERE Username = ? AND RecipeID = ?");
            st.setString(1, username);
            st.setInt(2,recipeId);
            final ResultSet rs = st.executeQuery();

            if (rs.next())
            {
                result = rs.getInt("NumAdded");
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
    public void addToShoppingCart(String username, int recipeID)
    {
        try( final Connection c = connection() )
        {

            final PreparedStatement st = c.prepareStatement("INSERT INTO ShoppingCart VALUES(?, ?)");
            st.setString(1,username);
            st.setInt(2,recipeID);
            st.executeUpdate();


        } catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public void emptyShoppingCart(String username)
    {
        try( final Connection c = connection() )
        {

            final PreparedStatement st = c.prepareStatement("DELETE FROM ShoppingCart WHERE Username = ?");
            st.setString(1,username);
            st.executeUpdate();


        } catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public void removeRecipeFromShoppingCart(String username, int recipeID)
    {
        try( final Connection c = connection() )
        {

            final PreparedStatement st = c.prepareStatement("DELETE FROM ShoppingCart WHERE Username = ? AND RecipeID = ?");
            st.setString(1,username);
            st.setInt(2,recipeID);
            st.executeUpdate();


        } catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }
}
