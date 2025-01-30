package recipebook.business;


import recipebook.objects.User;

public interface AccessUserInterface
{

    boolean logIn(String userName, String passWord);
    void logOut();
    void UpdateUser(String newPassword);
    void addUser(String userName, String password, String email);
    User getCurrentUser();
}

