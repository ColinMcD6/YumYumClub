package recipebook.business;


import recipebook.application.Services;
import recipebook.objects.User;
import recipebook.persistence.UserPersistence;


public class AccessUser implements AccessUserInterface
{


    private UserPersistence userList;
    private User currentUser;
    private static AccessUser instance;




    public AccessUser()
    {
        userList = Services.getUserPersistence();
        currentUser = null;
    }

    public AccessUser(UserPersistence userList)
    {
        this.userList = userList;
        currentUser = null;
        instance = this;
    }


    // Public method to provide access to the instance
    public static synchronized AccessUser getInstance()
    {
        if (instance == null)
        {
            instance = new AccessUser();
        }
        return instance;
    }


    public User getCurrentUser()
    {
        return currentUser;
    }



    @Override
    public boolean logIn(String userName, String passWord)
    {
        User current = userList.getCurrentUser();
        if(current!=null)
        {
            userList.logOut(current.getUserName());
        }
        // Log the user in. Takes in username and password and returns
        // a boolean result if successful or not.
        int rowsAffected = userList.logIn(userName,passWord);
        boolean loggedIn = rowsAffected > 0;
        if(loggedIn){
            currentUser = userList.getCurrentUser();
        }
        return loggedIn;
    }


    @Override
    public void logOut()
    {
        // Logs the current logged in user out
        int loggedOut = userList.logOut(currentUser.getUserName());
        if(loggedOut > 0)
        {
            this.currentUser = null;
        }
    }


    @Override
    public void UpdateUser( String newPassword)
    {
        // Updates the information for the currently logged in user.
        User current = getCurrentUser();
        userList.updateUser(current.getUserName(),newPassword);
    }


    @Override
    public void addUser(String userName, String password, String email)
    {
        userList.addUser(userName, password, email);
    }


    public boolean checkUser(String userName, String password, String email)
    {
        return userList.checkUser(userName,email);
    }

    public boolean checkStringLength(String user, String password, String email )
    {
        return user.length() <= 50 && password.length() <= 50 && email.length() <= 50;
    }
    public boolean checkEmpty(String userName, String passWord, String email)
    {
        // Check if any of the fields are left empty.
        boolean isEmpty = false;


        if (userName.isEmpty() || passWord.isEmpty() || email.isEmpty())
        {
            isEmpty = true;
        }
        return isEmpty;

    }
}

