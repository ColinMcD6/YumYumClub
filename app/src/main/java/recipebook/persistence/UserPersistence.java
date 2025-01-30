package recipebook.persistence;

import java.util.LinkedList;

import recipebook.objects.User;

public interface UserPersistence
{
    LinkedList<User> getUsers();
    void addUser(String userName, String password, String email);
    int logIn(String userName, String password);
    int logOut(String userName);
    User getCurrentUser();
    boolean checkUser(String userName, String email);
    void updateUser(String userName,String newPassword);
}

