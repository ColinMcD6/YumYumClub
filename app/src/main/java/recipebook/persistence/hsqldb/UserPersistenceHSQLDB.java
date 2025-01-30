package recipebook.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import recipebook.objects.User;
import recipebook.persistence.UserPersistence;

public class UserPersistenceHSQLDB implements UserPersistence {


    private final String dbPath;
    public UserPersistenceHSQLDB(String dbPath){
        this.dbPath = dbPath;
    }
    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }


    private User fromResultSet(ResultSet rs) throws SQLException
    {
        String username = rs.getString("USERNAME");
        String email = rs.getString("EMAIL");
        String password = rs.getString("PASSWORD");
        boolean loggedIn = rs.getBoolean("LOGGEDIN");
        return new User(username,password,email,loggedIn);
    }
    @Override
    public LinkedList<User> getUsers() {
        LinkedList<User> users = new LinkedList<>();
        try(final Connection c = connection()){
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM Users");
            while(rs.next())
            {
                User user = fromResultSet(rs);
                users.add(user);
            }
            rs.close();
            st.close();


            return users;


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }


    @Override
    public void addUser(String userName, String password, String email) {
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement("INSERT INTO Users VALUES(?,?,?, FALSE)");
            st.setString(1,userName);
            st.setString(2,email);
            st.setString(3,password);
            st.executeUpdate();


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }


    }


    public User getCurrentUser()
    {
        User result = null;
        try(final Connection c = connection()){
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM Users WHERE loggedIn = TRUE");
            if(rs.next())
            {
                result = fromResultSet(rs);
            }
            rs.close();
            st.close();


            return result;


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }


    public int logIn(String username, String password)
    {
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement("UPDATE Users SET LoggedIn = TRUE WHERE username = ? AND password = ?");
            st.setString(1,username);
            st.setString(2,password);
            return st.executeUpdate();

        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }


    }


    public int logOut(String username)
    {
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement("UPDATE Users SET LoggedIn = FALSE WHERE username = ?");
            st.setString(1,username);
            return st.executeUpdate();


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }


    }


    public User getUserByUsername(String username)
    {
        User result = null;
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement("SELECT * FROM Users WHERE Username = ?");
            st.setString(1, username);
            final ResultSet rs = st.executeQuery();


            if(rs.next())
            {
                result = fromResultSet(rs);
            }
            rs.close();
            st.close();


            return result;


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }


    @Override
    public boolean checkUser(String userName, String email) {
        boolean isSignedUp = false;
        LinkedList<User> users = getUsers();
        for(User user : users)
        {
            if(user.getUserName().equalsIgnoreCase(userName)|| user.getEmail().equalsIgnoreCase(email))
            {
                isSignedUp = true;
                break;
            }
        }
        return isSignedUp;
    }


    @Override
    public void updateUser(String userName, String newPassword) {
        try(final Connection c = connection()){
            final PreparedStatement st = c.prepareStatement("UPDATE Users SET Password = ? WHERE username = ?");
            st.setString(1,newPassword);
            st.setString(2,userName);
            st.executeUpdate();


        }catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }
}

