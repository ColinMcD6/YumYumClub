package recipebook.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import recipebook.objects.Recipe;
import recipebook.objects.Review;
import recipebook.persistence.RecipePersistence;

public class RecipePersistenceHSQLDB implements RecipePersistence{

    private final String dbPath;

    public RecipePersistenceHSQLDB(String dbPath)
    {
        this.dbPath = dbPath;
    }

    private Recipe fromResultSet( ResultSet rs ) throws SQLException
    {
        String name = rs.getString("Name");

        String test1 = rs.getString("Steps");
        List<String> steps = Arrays.asList(rs.getString("Steps").split(","));

        List<String> ingredients = Arrays.asList(rs.getString("Ingredients").split(","));

        List<String> tags = Arrays.asList(rs.getString("Tags").split(","));

        String creatorID = rs.getString("CreatorID");
        int id = rs.getInt("RecipeID");
        String imageURL = rs.getString("imageURL");
        return new Recipe( id,name,steps,ingredients,tags,creatorID,imageURL );
    }

    private Connection connection() throws SQLException
    {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    @Override
    public List<Recipe> getAllRecipes()
    {
        List<Recipe> recipes = new ArrayList<>();
         try( final Connection c = connection() )
         {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM Recipes");
            while( rs.next() )
            {
                Recipe recipe = fromResultSet( rs );
                recipes.add( recipe );
            }
            rs.close();
            st.close();

            return recipes;

        } catch ( final SQLException e )
        {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Recipe getRecipeByID( int id )
    {
        Recipe recipe = null;
        try( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM Recipes WHERE RECIPEID = ?");
            st.setInt(1,id);
            final ResultSet rs = st.executeQuery();

            if ( rs.next() )
                recipe = fromResultSet( rs );
            rs.close();
            st.close();

            return recipe;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public List<String> getTags()
    {
        List<String> tags = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM Tags");
            while( rs.next() )
            {
                String tag = rs.getString("TagName");
                tags.add(tag);
            }
            rs.close();
            st.close();

            return tags;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public List<Recipe> getRecipesByTag( String tag ) {
        List<Recipe> recipes = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            if ( getTags().contains( tag ) )
            {
                final PreparedStatement st = c.prepareStatement("SELECT * FROM Recipes WHERE TAGS LIKE ?");
                st.setString(1,"%" + tag+ "%");
                final ResultSet rs = st.executeQuery();

                while( rs.next() )
                {
                    Recipe recipe = fromResultSet( rs );
                    recipes.add( recipe );
                }
                rs.close();
                st.close();
            }

            return recipes;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public Recipe insertRecipe(String name, List<String> steps, List<String> ingredients, List<String> tags, String creatorId, String url)
    {
        try ( final Connection c = connection() )
        {

            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT MAX(RECIPEID) AS MAXID FROM Recipes");
            int maxID = 0;
            if( rs.next() )
                maxID = rs.getInt("MAXID");
            rs.close();
            st.close();


            final PreparedStatement st1 = c.prepareStatement("INSERT INTO Recipes VALUES(?, ?, ?, ?, ?, ?, ?)");
            st1.setInt(1,maxID +1);
            st1.setString(2,name);
            st1.setString(3,listToString(steps));
            st1.setString(4,listToString(ingredients));
            st1.setString(5,listToString(tags));
            st1.setString(6,creatorId);
            st1.setString(7,url);

            st1.executeUpdate();

            return getRecipeByID( maxID+1 );

        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }

    }

    private String listToString(List<String> input)
    {
        String result = "";

        for (String s: input)
        {
            result = result + ","+ s;
        }
        result = result.substring(1);

        return result;
    }

    @Override
    public List<String> getSavedFolderNames( String username )
    {
        List<String> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT DISTINCT FOLDERNAME FROM SavedFolders WHERE Username = ?");
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
            {
                String folderName = rs.getString("FOLDERNAME");
                result.add( folderName );
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
    public List<Recipe> getRecipesInSavedFolder( String username, String folderName )
    {
        List<Recipe> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT r.RECIPEID, r.NAME, r.STEPS, r.INGREDIENTS, r.TAGS, r.CREATORID, r.IMAGEURL  FROM SavedFolders s INNER JOIN Recipes r ON s.RECIPEID = r.RECIPEID  WHERE Username = ? AND FOLDERNAME = ?");
            st.setString(1, username);
            st.setString(2, folderName);
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
            {
                Recipe recipe = fromResultSet( rs );
                result.add( recipe );
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
    public void saveRecipeToFolder( String username, int recipeID, String foldername )
    {
        try( final Connection c = connection() )
        {

            final PreparedStatement st = c.prepareStatement("INSERT INTO SavedFolders VALUES(?, ?, ?)");
            st.setString(1,username);
            st.setInt(2,recipeID);
            st.setString(3,foldername);
            st.executeUpdate();


        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }

    }

    @Override
    public List<Recipe> searchRecipeByName( String name )
    {
        List<Recipe> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM Recipes WHERE UCASE(NAME) LIKE ?");
            st.setString(1,"%"+name.toUpperCase()+"%");
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
                result.add( fromResultSet( rs ) );
            rs.close();
            st.close();

            return result;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public List<String> getTagsOfSearchRecipeByName( String name )
    {
        List<String> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT DISTINCT TAGS FROM Recipes WHERE UCASE(NAME) LIKE ?");
            st.setString(1,"%"+name.toUpperCase()+"%");
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
            {
                List<String> tags = Arrays.asList(rs.getString("Tags").split(","));
                for ( String t: tags )
                {
                    if( !result.contains( t ) )
                        result.add( t );
                }
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
    public List<Recipe> searchByNameAndTags( String name, List<String> tags )
    {
        List<Recipe> result = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            String statement = "";
            if ( tags.size()>0 )
            {
                statement = " AND (";
                int index = 0;
                for ( String t:tags )
                {
                    if( index == 0 )
                    {
                        statement = statement + "TAGS LIKE ?";
                        index++;
                    } else {
                        statement = statement + " OR TAGS LIKE ?";
                    }
                }
                statement = statement + ")";
            }

            final PreparedStatement st = c.prepareStatement("SELECT * FROM Recipes WHERE UCASE(NAME) LIKE ?" + statement);
            st.setString(1,"%"+name.toUpperCase()+"%");

            int i = 2;
            for( String t:tags )
            {
                st.setString( i,"%" + t + "%" );
                i++;
            }
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
                result.add( fromResultSet( rs ) );
            rs.close();
            st.close();

            return result;

        } catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public boolean checkFolderHasRecipe( String username, int recipeID, String foldername )
    {
        boolean result = false;
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM SavedFolders WHERE Username = ? AND RecipeID = ? AND FolderName = ?");
            st.setString(1, username);
            st.setInt(2,recipeID);
            st.setString(3,foldername);

            final ResultSet rs = st.executeQuery();

            if( rs.next() )
            {
                result = true;
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
    public void addFolder( String username, String foldername )
    {
        try( final Connection c = connection() )
        {

            final PreparedStatement st = c.prepareStatement("INSERT INTO SavedFolders VALUES(?, ?, ?)");
            st.setString(1,username);
            st.setInt(2,0);
            st.setString(3,foldername);
            st.executeUpdate();


        } catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

    @Override
    public List<Recipe> getRecipesByCreator(String creator) {
        List<Recipe> recipes = new ArrayList<>();
        try ( final Connection c = connection() )
        {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM Recipes WHERE CreatorID LIKE ?");
            st.setString(1, creator );
            final ResultSet rs = st.executeQuery();

            while( rs.next() )
            {
                Recipe recipe = fromResultSet( rs );
                recipes.add( recipe );
            }
            rs.close();
            st.close();

            return recipes;

        }catch ( final SQLException e )
        {
            throw new PersistenceException( e );
        }
    }

}
